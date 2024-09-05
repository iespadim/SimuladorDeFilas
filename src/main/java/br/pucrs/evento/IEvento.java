package br.pucrs.evento;

public interface IEvento {
    int initTime = 0;
    int endTime = 0;

    default double getTime(){
        return initTime;
    }
}
