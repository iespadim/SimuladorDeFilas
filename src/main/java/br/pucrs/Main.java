package br.pucrs;

import br.pucrs.evento.Chegada;
import br.pucrs.evento.Saida;
import br.pucrs.evento.IEvento;

public class Main {
    static long globalTime = 0;  // Tempo total da simulação
    static int quantidadeDeSimulacoes;

    // Simulacao
    static EscalonadorDeEventos escalonadorDeEventos;

    public static void main(String[] args) {
        // Config
        RNG rng = new RNG(1245, 1664525, 1013904223, Math.pow(2, 32));
        quantidadeDeSimulacoes = 50;
        int tempoDaPrimeiraChegada = 2;

        // Start filas
        Fila fila_1 = new Fila(0, 10, 1, 2, 5, 2, 5);

        // Start the event scheduler
        escalonadorDeEventos = new EscalonadorDeEventos();
        escalonadorDeEventos.adicionarFila(fila_1);
        escalonadorDeEventos.adicionarEvento(fila_1, new Chegada(tempoDaPrimeiraChegada, fila_1.getId()));

        fila_1.setLastEventTime(tempoDaPrimeiraChegada);

        // Main loop
        while (quantidadeDeSimulacoes > 0) {
            IEvento evento = escalonadorDeEventos.removeProximoEvento();
            Fila fila = escalonadorDeEventos.getFilaById(evento.getIdFila());

            sincronizaGlobalTimerComTempoDoEvento(evento);

            if (evento instanceof Chegada) {
                // Chegada
                fila.totalClientesChegaram++;

                fila.totalIntervaloEntreChegadas += (evento.getTime() - fila.tempoUltimaChegada);
                fila.setLastEventTime(evento.getTime());

                System.out.println("Evento: Chegada - t=" + evento.getTime() + " globalTime: " + globalTime);

                if (fila.temEspaco()) {
                    fila.adicionarCliente();

                    if (fila.prontoParaAtendimentoImediato()) {
                        int rng_ = rng.nextRandonBetween(fila.saidaMin, fila.saidaMax);
                        long saida = globalTime + rng_;
                        escalonadorDeEventos.adicionarEvento(fila, new Saida(saida, fila.getId()));
                        fila.totalClientesAtendidos++;
                    }
                } else {
                    fila.totalClientesPerdidos++;
                }

                int rng_ = rng.nextRandonBetween(fila.chegadaMin, fila.chegadaMax);
                long proximaChegada = globalTime + rng_;
                escalonadorDeEventos.adicionarEvento(fila, new Chegada(proximaChegada, fila.getId()));
                quantidadeDeSimulacoes--;
            }else if (evento instanceof Saida) {
                // Saída
                fila.removerCliente();
                System.out.println("Evento: Saida - t=" + evento.getTime() + " globalTime: " + globalTime);

                if (!fila.estaVazia()) {
                    fila.removerCliente();
                }

                fila.totalTempoAtendimento += (evento.getTime() - fila.getLastEventTime());

                if (fila.temGenteEsperando()) {
                    int rng_ = rng.nextRandonBetween(fila.saidaMin, fila.saidaMax);
                    long proximaSaida = globalTime + rng_;
                    escalonadorDeEventos.adicionarEvento(fila, new Saida(proximaSaida, fila.getId()));
                    fila.totalClientesAtendidos++;
                }

            }
        }

        imprimeResultados();
    }

    private static void imprimeResultados() {
        System.out.println("\nResultados finais:");
        System.out.println("Tempo total: " + globalTime);

        System.out.println("\nResultados por fila:");
        for (int i = 0; i < escalonadorDeEventos.filas.size(); i++) {
            Fila fila = escalonadorDeEventos.getFilaById(i);
            System.out.printf("Fila " + i + ":\n");
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