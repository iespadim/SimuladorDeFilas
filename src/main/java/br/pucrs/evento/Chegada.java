package br.pucrs.evento;

public class Chegada implements IEvento{
    private long arrivalTime;
    private long saida;

    public Chegada(long time) {
        this.arrivalTime = time;
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
