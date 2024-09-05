package br.pucrs.evento;

public class Chegada implements IEvento{
    private double time;

    public Chegada(double time) {
        this.time = time;
    }

    public double getTime() {
        return time;
    }
}
