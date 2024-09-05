package br.pucrs.evento;

public class Saida implements IEvento {

    private double time;

    public Saida(double time) {
        this.time = time;
    }

    public double getTime() {
        return time;
    }
}
