package br.pucrs;

import br.pucrs.evento.Chegada;
import br.pucrs.evento.Saida;
import br.pucrs.evento.IEvento;

public class Main {
    static long globalTime = 0;  // Tempo total da simulação
    static long lastEventTime = 0;  // Tempo do último evento processado
    static int quantidade, tamanhoFila, tamanhoMaxFila, servidores;
    static int chegadaMin, chegadaMax, saidaMin, saidaMax;

    static long[] tempos;  // Array que acumula o tempo para cada quantidade de pessoas na fila

    // Variáveis para cálculo das métricas
    static int totalClientesChegaram = 0;
    static int totalClientesPerdidos = 0;
    static int totalClientesAtendidos = 0;
    static long totalTempoAtendimento = 0;  // Tempo total gasto com atendimento
    static long totalIntervaloEntreChegadas = 0;  // Tempo total entre chegadas de clientes
    static long tempoUltimaChegada = 0;  // Tempo da última chegada

    public static void main(String[] args) {
        RNG rng = new RNG(1245, 1664525, 1013904223, Math.pow(2, 32));

        quantidade = 100000;

        servidores = 1;

         tamanhoMaxFila = 10;

         tamanhoFila = 0;

        chegadaMin = 2;
        chegadaMax = 5;
        saidaMin = 2;
        saidaMax = 5;
        tempos = new long[tamanhoMaxFila + 1];

        EscalonadorDeEventos escalonadorDeEventos = new EscalonadorDeEventos();
        escalonadorDeEventos.adicionarEvento(new Chegada(2));
        tempoUltimaChegada = 2;  // Primeiro tempo de chegada

        while (quantidade > 0) {
            IEvento evento = escalonadorDeEventos.proximoEvento();


            if (evento instanceof Chegada) {
                acumulaTempo(evento.getTime());
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
                        escalonadorDeEventos.adicionarEvento(new Saida(saida));
                        totalClientesAtendidos++;  // Cliente será atendido imediatamente
                    }
                } else {
                    totalClientesPerdidos++;  // Cliente perdido, fila cheia
                }

                int rng_ = rng.nextRandonBetween(chegadaMin, chegadaMax);
                long proximaChegada = globalTime + rng_;
                escalonadorDeEventos.adicionarEvento(new Chegada(proximaChegada));
                quantidade--;

            } else if (evento instanceof Saida) {
                acumulaTempo(evento.getTime());
                System.out.println("Evento: Saida - t=" + evento.getTime() + " globalTime: " + globalTime);

                if (tamanhoFila > 0) {
                    tamanhoFila--;
                }

                // Calcula o tempo médio de atendimento
                totalTempoAtendimento += (evento.getTime() - lastEventTime);

                if (tamanhoFila >= servidores) {
                    int rng_ = rng.nextRandonBetween(saidaMin, saidaMax);
                    long proximaSaida = globalTime + rng_;
                    escalonadorDeEventos.adicionarEvento(new Saida(proximaSaida));
                    totalClientesAtendidos++;  // Cliente será atendido posteriormente
                }
            }
        }

        // Exibe resultados finais
        System.out.println("Tempo total: " + globalTime);
        for (int i = 0; i < tempos.length; i++) {
            System.out.println("Tempo com " + i + " pessoas na fila: " + tempos[i]);
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

    private static void acumulaTempo(long timeToAdvance) {
        // Acumula o tempo global corretamente
        globalTime = timeToAdvance;

        if (tamanhoFila >= 0 && tamanhoFila < tempos.length) {
            // Acumula a diferença de tempo desde o último evento
            tempos[tamanhoFila] += (globalTime - lastEventTime);
        }

        lastEventTime = globalTime;
    }
}