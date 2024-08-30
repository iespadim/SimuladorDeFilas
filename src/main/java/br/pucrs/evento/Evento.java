package br.pucrs.evento;

public interface Evento {
    double initTime = 0;

    enum tipo {
        CHEGADA, SAIDA
    }


    default double getTime(){
        return initTime;
    }

}
