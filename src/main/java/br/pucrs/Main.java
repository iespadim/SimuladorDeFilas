package br.pucrs;

import br.pucrs.evento.Chegada;
import br.pucrs.evento.Saida;
import br.pucrs.evento.IEvento;

public class Main {
    static int globalTime = 0;
    static int quantidade, tamanhoFila, tamanhoMaxFila, servidores;
    static int chegadaMin, chegadaMax, saidaMin, saidaMax;

    public static void main(String[] args) {
        //inicia o gerador de numeros aleatorios
        RNG rng = new RNG(1664525, 1013904223, 4294967296L);

        //int quantidade maxima de numeros a serem gerados
        quantidade = 10;

        //int quantidade de servidores
        servidores = 1;

        //int tamanho maximo da fila
        tamanhoMaxFila = 10;

        //int fila status - quantidade de pessoas na fila
        tamanhoFila = 0;

        //int tempos de chegada e saida min e max
        chegadaMin = 2;
        chegadaMax = 5;
        saidaMin = 2;
        saidaMax = 5;

        //int inicia o escalonaor de eventos
        EscalonadorDeEventos escalonadorDeEventos = new EscalonadorDeEventos();
        escalonadorDeEventos.adicionarEvento(new Chegada(2));

        while (quantidade>0){
            IEvento evento = escalonadorDeEventos.proximoEvento();

           //se o evento for do tipo chegada
            if(evento instanceof Chegada){
                //acumuula o tempo
                acumulaTempo(evento.getTime());
                //se a fila for menor que o tamanho maximo da fila
                if (tamanhoFila < tamanhoMaxFila) {
                    //fila in
                    tamanhoFila++;
                    //se fila <= servidores
                    if (tamanhoFila <= servidores){
                        //adiciona um evento de saida
                        //randon entre saidaMin e saidaMax
                        int rng_ = rng.nextRandonBetween(saidaMin, saidaMax);
                        escalonadorDeEventos.adicionarEvento(new Saida(globalTime + rng_));
                    }
                }else{
                    tamanhoFila--;
                }
                int rng_ = rng.nextRandonBetween(chegadaMin, chegadaMax);
                escalonadorDeEventos.adicionarEvento(new Saida(globalTime + rng_));


            }else if (evento instanceof Saida){
                acumulaTempo(evento.getTime());
                if(tamanhoFila>0){tamanhoFila--;}
                if (tamanhoFila >= servidores){
                    int rng_ = rng.nextRandonBetween(saidaMin, saidaMax);
                    escalonadorDeEventos.adicionarEvento(new Saida(globalTime + rng_));
                }
            }
        }

    }

    private static void acumulaTempo(double time) {
        globalTime += time;
    }
}