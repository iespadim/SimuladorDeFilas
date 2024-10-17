package br.pucrs;

import br.pucrs.configuracao.Configuracao;
import br.pucrs.evento.Chegada;
import br.pucrs.evento.Passagem;
import br.pucrs.evento.Saida;
import br.pucrs.evento.IEvento;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {
    static List<Map<String, Object>> parametrosFilas;
    static long globalTime = 0;  // Tempo total da simulação
    static int quantidadeDeSimulacoes;
    static RNG rng;
    static int tempo_prmeiro_evento;
    static int fila_primeiro_evento;

    // Simulacao
    static EscalonadorDeEventos escalonadorDeEventos;

    public static void main(String[] args) {
        lerYaml();
        preparaSimulacao();

        // Main loop
        while (quantidadeDeSimulacoes > 0) {
            IEvento evento = escalonadorDeEventos.removeProximoEvento();
            sincronizaGlobalTimerComTempoDoEvento(evento);

            if (evento instanceof Chegada) {
                chegada(evento);
            }else if (evento instanceof Saida) {
                saida(evento);
            }else if (evento instanceof Passagem) {
                System.out.println("Evento de passagem de fila");
                passagem(evento);
            }
        }

        imprimeResultados();
    }

    private static void preparaSimulacao() {
        escalonadorDeEventos = new EscalonadorDeEventos();

        // instanciar filas
        for (Map<String, Object> paramFila : parametrosFilas) {
            int id = (int) paramFila.get("id");
            int tamanho_maximo = (int) paramFila.get("tamanho_max");
            int servidores = (int) paramFila.get("servidores");
            int chegadaMin = (int) paramFila.get("chegada_min");
            int chegadaMax = (int) paramFila.get("chegada_max");
            int saidaMin = (int) paramFila.get("saida_min");
            int saidaMax = (int) paramFila.get("saida_max");

            //cria o mapa de destinos para a fila
            HashMap<Integer, Double> destinos = new HashMap<>();
            for (List<Object> destino : (List<List<Object>>) paramFila.get("destinos")) {
                int idDestino = (int) destino.get(0);
                double probabilidade = ((Number) destino.get(1)).doubleValue();
                destinos.put(idDestino, probabilidade);
            }

            // Criar a fila com os parâmetros carregados
            Fila fila = new Fila(id, tamanho_maximo, servidores, chegadaMin, chegadaMax, saidaMin, saidaMax, destinos);
            escalonadorDeEventos.adicionarFila(fila);
        }

        rng = new RNG(1245, 1664525, 1013904223, Math.pow(2, 32));

        Fila filaInicial = escalonadorDeEventos.getFilaById(fila_primeiro_evento);


       escalonadorDeEventos.adicionarEvento(filaInicial, new Chegada(tempo_prmeiro_evento, filaInicial.getId()));

        filaInicial.setLastEventTime(tempo_prmeiro_evento);
    }

    private static void lerYaml() {
        // Carregar o arquivo YAML
        Map<String, Object> config = Configuracao.carregarConfiguracoes("src/config/configuracao.yaml");
        if (config == null) {
            System.out.println("Erro ao carregar o arquivo de configuração.");
            return;
        }

        // Ler as configurações
        Map<String, Object> simulacao = (Map<String, Object>) config.get("simulacao");
        quantidadeDeSimulacoes = (int) simulacao.get("quantidade_de_simulacoes");
        long sementeRng = (int) simulacao.get("semente_rng");
        rng = new RNG(sementeRng, 1664525, 1013904223, Math.pow(2, 32));


        tempo_prmeiro_evento = (int) simulacao.get("tempo_prmeiro_evento");
        fila_primeiro_evento = (int) simulacao.get("fila_primeiro_evento");

        parametrosFilas = (List<Map<String, Object>>) simulacao.get("parametros_filas");
    }

    private static void saida(IEvento evento) {
        System.out.println("Evento: Saida - t=" + evento.getTime() + " globalTime: " + globalTime);
        Fila fila = escalonadorDeEventos.getFilaById(evento.getIdFila());

        if (!fila.estaVazia()) {
            fila.removerCliente();
        }

        fila.totalTempoAtendimento += (evento.getTime() - fila.getLastEventTime());

        if (fila.temGenteEsperando()) {
            //sorteia destinos
            // faz o roteamento para filas aqui
            //escolhe destino
            double sum = 0;
            double prob = rng.nextRandonBetween(0, 1);

            for(Integer i: fila.destinos.keySet()){
                sum += fila.destinos.get(i);
                if(prob < sum){
                    System.out.println("Cliente encaminhado para fila "+i);
                    // se -1 nao faz nada pois saiu do ciclo
                    if (i != -1) {
                        escalonadorDeEventos.adicionarEvento(escalonadorDeEventos.getFilaById(i), new Passagem(globalTime,fila.getId(), i));
                        System.out.println("Cliente foi pra fila " + i);
                    } else {
                        escalonadorDeEventos.adicionarEvento(escalonadorDeEventos.getFilaById(i), new Saida(globalTime,fila.getId()));
                        System.out.println("Cliente saiu do sistema");
                    }
                    break;
                }
            }

            int rng_ = rng.nextRandonBetween(fila.saidaMin, fila.saidaMax);
            long proximaSaida = globalTime + rng_;
            escalonadorDeEventos.adicionarEvento(fila, new Saida(proximaSaida, fila.getId()));
            fila.totalClientesAtendidos++;
        }
    }

    private static void passagem(IEvento evento) {
        System.out.println("Evento: Saida - t=" + evento.getTime() + " globalTime: " + globalTime);
        Fila filaOrigem = escalonadorDeEventos.getFilaById(evento.getIdFila());
        Fila filaDestino = escalonadorDeEventos.getFilaById(((Passagem) evento).getIdDestino());

        //fila1 out
        if (!filaOrigem.estaVazia()) {
            filaOrigem.removerCliente();
        }

        if (filaOrigem.temGenteEsperando()) {
            double sum = 0;
            double prob = rng.nextRandonBetween(0, 1);

            for(Integer i: filaOrigem.destinos.keySet()){
                sum += filaOrigem.destinos.get(i);
                if(prob < sum){
                    System.out.println("Cliente encaminhado para fila "+i);
                    if (i != -1) {
                        escalonadorDeEventos.adicionarEvento(escalonadorDeEventos.getFilaById(i), new Passagem(globalTime,filaOrigem.getId(), i));
                        System.out.println("Cliente foi pra fila " + i);
                    } else {
                        escalonadorDeEventos.adicionarEvento(escalonadorDeEventos.getFilaById(i), new Saida(globalTime,filaOrigem.getId()));
                        System.out.println("Cliente saiu do sistema");
                    }
                    break;
                }
            }
            filaDestino.adicionarCliente();
            if (filaDestino.prontoParaAtendimentoImediato()) {
                escalonadorDeEventos.adicionarEvento(filaDestino, new Saida(globalTime,filaDestino.getId()));
            }
        }
    }

    private static void chegada(IEvento evento) {
        Fila fila = escalonadorDeEventos.getFilaById(evento.getIdFila());
        // Chegada
        fila.totalClientesChegaram++;

        fila.totalIntervaloEntreChegadas += (evento.getTime() - fila.tempoUltimaChegada);
        fila.setLastEventTime(evento.getTime());

        System.out.println("Evento: Chegada - t=" + evento.getTime() + " globalTime: " + globalTime);

        if (fila.temEspaco()) {
            fila.adicionarCliente();

            if (fila.prontoParaAtendimentoImediato()) {
                // faz o roteamento para filas aqui
                //escolhe destino
                double sum = 0;
                double prob = rng.nextRandonBetween(0, 1);

                for(Integer i: fila.destinos.keySet()){
                    sum += fila.destinos.get(i);
                    if(prob < sum){
                        System.out.println("Cliente encaminhado para fila "+i);
                        if (i != -1) {
                            escalonadorDeEventos.adicionarEvento(escalonadorDeEventos.getFilaById(i), new Passagem(globalTime,fila.getId(), i));
                            System.out.println("Cliente foi pra fila " + i);
                        } else {
                            System.out.println("Cliente saiu do sistema pela saida da fila " + fila.getId());
                        }
                        break;
                    }
                }
                fila.totalClientesAtendidos++;
            }
        } else {
            fila.totalClientesPerdidos++;
        }

        int rng_ = rng.nextRandonBetween(fila.chegadaMin, fila.chegadaMax);
        long proximaChegada = globalTime + rng_;
        escalonadorDeEventos.adicionarEvento(fila, new Chegada(proximaChegada, fila.getId()));
        quantidadeDeSimulacoes--;
    }

    private static void imprimeResultados() {
        System.out.println("\nResultados finais:");
        System.out.println("Tempo total: " + globalTime);

        System.out.println("\nResultados por fila:");

        for (Fila fila: escalonadorDeEventos.filas) {
            System.out.printf("Fila " + fila.getId() + ":\n");
            System.out.println(fila.tempos.size() + " tempos");
            for (int j = 0; j < fila.tempos.size(); j++) {
                System.out.printf("Tempo acumulado para " + j + " pessoas na fila: " + fila.tempos.get(j) + "\n");
            }

            double taxaPerdaClientes = (double) fila.totalClientesPerdidos / fila.totalClientesChegaram * 100;
            double intervaloMedioChegadas = (double) fila.totalIntervaloEntreChegadas / fila.totalClientesChegaram;
            double tempoMedioAtendimento = (double) fila.totalTempoAtendimento / fila.totalClientesAtendidos;

            System.out.println("\nMétricas da Simulação:");
            System.out.println("Total de clientes que chegaram: " + fila.totalClientesChegaram);
            System.out.println("Total de clientes perdidos por superlotação: " + fila.totalClientesPerdidos);
            System.out.println("Total de clientes atendidos: " + fila.totalClientesAtendidos);
            System.out.println("Taxa de perda de clientes: " + taxaPerdaClientes + "%");
            System.out.println("Intervalo médio entre chegadas: " + intervaloMedioChegadas);
            System.out.println("Tempo médio de atendimento: " + tempoMedioAtendimento);
        }
        System.out.println("\nFim de relatório");
    }

    private static void sincronizaGlobalTimerComTempoDoEvento(IEvento evento) {
        long timeToAdvance = evento.getTime();
        if (timeToAdvance > globalTime) {
            System.out.println("avançando de " + globalTime + " para " + timeToAdvance);
            globalTime = timeToAdvance;
        }

        Fila fila = escalonadorDeEventos.getFilaById(evento.getIdFila());

        long filaLastEventTime = fila.getLastEventTime();
        int tamanhoAtualFila = fila.getTamanhoAtual();

        fila.acumularTempo(tamanhoAtualFila, timeToAdvance - filaLastEventTime);
        fila.setLastEventTime(timeToAdvance);
    }
}