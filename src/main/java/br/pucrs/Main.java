package br.pucrs;

import br.pucrs.evento.Chegada;
import br.pucrs.evento.Saida;
import br.pucrs.evento.Evento;

public class Main {
    public static void main(String[] args) {
        //inicia o gerador de numeros aleatorios
        RNG rng = new RNG(1664525, 1013904223, 4294967296L);

        //int quantidade maxima de numeros a serem gerados
        int quantidade = 1000;

        //int quantidade de servidores
        int servidores = 1;

        //int tamanho maximo da fila
        int tamanhoFila = 10;

        //int inicia o escalonaor de eventos
        EscalonadorDeEventos escalonadorDeEventos = new EscalonadorDeEventos();



        while (quantidade>0){
            Evento evento = escalonadorDeEventos.proximoEvento();

           //se o evento for do tipo chegada
            if(evento instanceof Chegada){

            }
        }

    }



}