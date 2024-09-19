package br.pucrs.evento;

public class Saida implements IEvento {

    private int time;

    public Saida(int time) {
        this.time = time;
    }

    @Override
    public int getTime() {
        return time;
    }
}
