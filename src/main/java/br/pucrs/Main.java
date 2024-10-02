package br.pucrs;

import br.pucrs.evento.Chegada;
import br.pucrs.evento.Saida;
import br.pucrs.evento.IEvento;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    static long globalTime = 0;  // Tempo total da simulação
    static long lastEventTime = 0;  // Tempo do último evento processado
    static int quantidadeDeSimulacoes, tamanhoFila, tamanhoMaxFila, servidores;
    static int chegadaMin, chegadaMax, saidaMin, saidaMax;

    static long[][] tempos;  // Array que acumula o tempo para cada quantidade de pessoas na fila

    // Variáveis para cálculo das métricas
    static int totalClientesChegaram = 0;
    static int totalClientesPerdidos = 0;
    static int totalClientesAtendidos = 0;
    static long totalTempoAtendimento = 0;  // Tempo total gasto com atendimento
    static long totalIntervaloEntreChegadas = 0;  // Tempo total entre chegadas de clientes
    static long tempoUltimaChegada = 0;  // Tempo da última chegada

    //simulacao
    static EscalonadorDeEventos escalonadorDeEventos;

    public static void main(String[] args) {
        RNG rng = new RNG(1245, 1664525, 1013904223, Math.pow(2, 32));

        ArrayList<Fila> filas = new ArrayList<>();

        quantidadeDeSimulacoes = 100000;

        int numeroDeFilas = 1;
        //fila1
        int idFila1 = 0;
        servidores = 1;
        tamanhoMaxFila = 10;
        tamanhoFila = 0;

        // Min and max times for arrivals and departures
        chegadaMin = 2;
        chegadaMax = 5;
        saidaMin = 2;
        saidaMax = 5;
        tempos = new long[numeroDeFilas+1][tamanhoMaxFila + 1];

        //simulaçao 1
        // clientes -> fila1 -> saída
        //instancia uma fila
        Fila fila1 = new Fila(idFila1, tamanhoMaxFila,servidores);
        fila1.eventos = new ArrayList<>();
        filas.add(fila1);


        // Start the event scheduler
        escalonadorDeEventos = new EscalonadorDeEventos(filas);
        escalonadorDeEventos.adicionarEvento(fila1, new Chegada(2, idFila1));
        tempoUltimaChegada = 2;  // Primeiro tempo de chegada

        while (quantidadeDeSimulacoes > 0) {
            IEvento evento = escalonadorDeEventos.proximoEvento();


            if (evento instanceof Chegada) {
                fila1.adicionarCliente();
                acumulaTempo(evento);
                totalClientesChegaram++;  // Incrementa a quantidade de clientes que chegaram

                // Calcula o intervalo entre chegadas
                totalIntervaloEntreChegadas += (evento.getTime() - tempoUltimaChegada);
                tempoUltimaChegada = evento.getTime();

                System.out.println("Evento: Chegada - t=" + evento.getTime() + " globalTime: " + globalTime);

                if (tamanhoFila < tamanhoMaxFila) {
                    tamanhoFila++;

                    if (tamanhoFila <= servidores) {
                        int rng_ = rng.nextRandonBetween(saidaMin, saidaMax);
                        long saida = globalTime + rng_;
                        escalonadorDeEventos.adicionarEvento(fila1,new Saida(saida, idFila1));
                        totalClientesAtendidos++;  // Cliente será atendido imediatamente
                    }
                } else {
                    totalClientesPerdidos++;  // Cliente perdido, fila cheia
                }

                int rng_ = rng.nextRandonBetween(chegadaMin, chegadaMax);
                long proximaChegada = globalTime + rng_;
                escalonadorDeEventos.adicionarEvento(fila1,new Chegada(proximaChegada, idFila1));
                quantidadeDeSimulacoes--;

            } else if (evento instanceof Saida) {
                fila1.removerCliente();
                acumulaTempo(evento);
                System.out.println("Evento: Saida - t=" + evento.getTime() + " globalTime: " + globalTime);

                if (tamanhoFila > 0) {
                    tamanhoFila--;
                }

                // Calcula o tempo médio de atendimento
                totalTempoAtendimento += (evento.getTime() - lastEventTime);

                if (tamanhoFila >= servidores) {
                    int rng_ = rng.nextRandonBetween(saidaMin, saidaMax);
                    long proximaSaida = globalTime + rng_;
                    escalonadorDeEventos.adicionarEvento(fila1,new Saida(proximaSaida,idFila1));
                    totalClientesAtendidos++;  // Cliente será atendido posteriormente
                }
            }
        }

        // Exibe resultados finais
        System.out.println("Tempo total: " + globalTime);
        for (int i = 0; i < filas.size(); i++) {
            System.out.println("Fila " + i + ":");
            for (int j = 0; j < tempos[i].length; j++) {
                System.out.println("Tempo acumulado para " + j + " pessoas na fila: " + tempos[i][j]);
            }
        }

// Cálculo das métricas
        double taxaPerdaClientes = (double) totalClientesPerdidos / totalClientesChegaram * 100;
        double intervaloMedioChegadas = (double) totalIntervaloEntreChegadas / totalClientesChegaram;
        double tempoMedioAtendimento = (double) totalTempoAtendimento / totalClientesAtendidos;

        System.out.println("\nMétricas da Simulação:");
        System.out.println("Total de clientes que chegaram: " + totalClientesChegaram);
        System.out.println("Total de clientes perdidos por superlotação: " + totalClientesPerdidos);
        System.out.println("Total de clientes atendidos (que saíram do sistema): " + totalClientesAtendidos);
        System.out.println("Taxa de perda de clientes: " + taxaPerdaClientes + "%");
        System.out.println("Intervalo médio entre chegadas: " + intervaloMedioChegadas);
        System.out.println("Tempo médio de atendimento: " + tempoMedioAtendimento);
    }



    private static void acumulaTempo(IEvento evento) {
        long timeToAdvance = evento.getTime();

        // Obtenha a fila relacionada ao evento
        Fila fila = escalonadorDeEventos.getFilaById(evento.getIdFila());

        // Calcula a diferença de tempo desde o último evento específico para esta fila
        long filaLastEventTime = fila.getLastEventTime();  // Tempo do último evento desta fila
        int tamanhoAtualFila = fila.getTamanhoAtual();  // Obtém o tamanho atual da fila

        // Acumula o tempo de acordo com o tamanho da fila atual
        if (tamanhoAtualFila >= 0 && tamanhoAtualFila < tempos[fila.getId()].length) {
            // Acumula o tempo corretamente apenas para esta fila
            tempos[fila.getId()][tamanhoAtualFila] += (timeToAdvance - filaLastEventTime);
        }

        // Atualiza o tempo do último evento processado para esta fila
        fila.setLastEventTime(timeToAdvance);

        // Atualiza o tempo global
        globalTime = timeToAdvance;
    }
}