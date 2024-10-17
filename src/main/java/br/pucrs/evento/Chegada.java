package br.pucrs.evento;

public class Chegada implements IEvento{
    private long arrivalTime;
    private long saida;
    private int idFila;

    public Chegada(long time, int idFila) {
        this.arrivalTime = time;
        this.idFila = idFila;

    }

    @Override
    public int getIdFila() {
        return idFila;

    }

    public long getSaida() {
        return saida;
    }


    @Override
    public long getTime() {
        return arrivalTime;
    }

    @Override
    public void setSaida(int saida) {
        this.saida = saida;
    }
}
