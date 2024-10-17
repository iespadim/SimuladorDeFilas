package br.pucrs.evento;

public class Passagem implements IEvento {
    private long arrivalTime;
    private long saida;
    private int idFila;
    private int idFilaDestino;

    public Passagem(long time, int idFilaOrigem, int idFilaDestino) {
        this.arrivalTime = time;
        this.idFila = idFilaOrigem;
        this.idFilaDestino = idFilaDestino;

    }

    @Override
    public int getIdFila() {
        return idFila;

    }

    @Override
    public long getTime() {
        return this.arrivalTime;
    }

    @Override
    public void setSaida(int saida) {
        this.saida = saida;
    }

    public long getSaida() {
        return saida;
    }

    public int getIdDestino() {
        return idFilaDestino;
    }
}
