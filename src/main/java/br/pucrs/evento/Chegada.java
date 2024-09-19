package br.pucrs.evento;

public class Chegada implements IEvento{
    private int arrivalTime;

    public Chegada(int time) {
        this.arrivalTime = time;
    }

    @Override
    public int getTime() {
        return arrivalTime;
    }
}
