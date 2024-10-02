package br.pucrs.evento;

public class Saida implements IEvento {

    private long time;
    private long saida;

    public Saida(long time) {
        this.time = time;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public void setSaida(int saida) {
        this.saida = saida;
    }

}
