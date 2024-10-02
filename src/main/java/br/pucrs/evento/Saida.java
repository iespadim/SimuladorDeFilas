package br.pucrs.evento;

public class Saida implements IEvento {

    private long time;
    private long saida;
    private int idFila;

    public Saida(long time, int idFila) {
        this.time = time;
        this.idFila = idFila;
    }

    @Override
    public int getIdFila() {
        return idFila;
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
