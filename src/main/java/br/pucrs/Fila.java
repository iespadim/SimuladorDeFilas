package br.pucrs;

import br.pucrs.evento.IEvento;

import java.util.ArrayList;

public class Fila {
    private int tamanhoAtual;
    private int tamanhoMaximo;
    private int idFila;
    private long lastEventTime;
    public ArrayList<IEvento> eventos;

    public Fila(int idFila,int tamanhoMaximo, int servidores) {
        this.idFila = idFila;
        this.tamanhoMaximo = tamanhoMaximo;
        this.tamanhoAtual = 0;
        lastEventTime = 0;
    }

    public boolean adicionarCliente() {
        if (tamanhoAtual < tamanhoMaximo) {
            tamanhoAtual++;
            return true;  // Cliente adicionado com sucesso
        } else {
            return false;  // Fila cheia, cliente não pode ser adicionado
        }
    }

    public void removerCliente() {
        if (tamanhoAtual > 0) {
            tamanhoAtual--;
        }
    }

    public int getTamanhoAtual() {
        return tamanhoAtual;
    }

    public boolean estaVazia() {
        return tamanhoAtual == 0;
    }

    public boolean estaCheia() {
        return tamanhoAtual >= tamanhoMaximo;
    }

    public int getId() {
        return idFila;
    }

    public long getLastEventTime() {
        return lastEventTime;
    }

    public void setLastEventTime(long time) {
        lastEventTime = time;
    }

}