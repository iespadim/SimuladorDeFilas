package br.pucrs;

import br.pucrs.evento.IEvento;

import java.util.ArrayList;

public class Fila {
    private final int sevidores;
    private int tamanhoAtual;
    private int tamanhoMaximo;
    private int idFila;
    private long lastEventTime;
    public ArrayList<IEvento> eventos;
    public ArrayList<Long> tempos;  // Lista de tempos por tamanho de fila

    // Variáveis para cálculo das métricas
    int totalClientesChegaram = 0;
    int totalClientesPerdidos = 0;
    int totalClientesAtendidos = 0;
    long totalTempoAtendimento = 0;  // Tempo total gasto com atendimento
    long totalIntervaloEntreChegadas = 0;  // Tempo total entre chegadas de clientes
    long tempoUltimaChegada = 0;  // Tempo da última chegada

    // Variáveis de simulação
    int chegadaMin;
    int chegadaMax;
    int saidaMin;
    int saidaMax;

    public Fila(int idFila, int tamanhoMaximo, int servidores, int chegadaMin, int chegadaMax, int saidaMin, int saidaMax) {
        this.idFila = idFila;
        this.tamanhoMaximo = tamanhoMaximo;
        this.tamanhoAtual = 0;
        this.chegadaMin = chegadaMin;
        this.chegadaMax = chegadaMax;
        this.saidaMin = saidaMin;
        this.saidaMax = saidaMax;
        this.sevidores = servidores;
        lastEventTime = 0;
        eventos = new ArrayList<>();
        tempos = new ArrayList<>();
        for (int i = 0; i <= tamanhoMaximo; i++) {
            tempos.add(0L);  // Inicializa a lista de tempos com zero
        }
    }

    public boolean adicionarCliente() {
        if (tamanhoAtual < tamanhoMaximo) {
            tamanhoAtual++;
            return true;  // Cliente adicionado com sucesso
        } else {
            return false;  // Fila cheia, cliente não adicionado
        }
    }

    public void removerCliente() {
        if (tamanhoAtual > 0) {
            tamanhoAtual--;
        }
    }

    public boolean temEspaco() {
        return tamanhoAtual < tamanhoMaximo;
    }

    public boolean prontoParaAtendimentoImediato() {
        return tamanhoAtual > 0 && tamanhoAtual <= sevidores;
    }

    public boolean estaVazia() {
        return tamanhoAtual == 0;
    }

    public boolean temGenteEsperando() {
        return tamanhoAtual > 0;
    }

    public int getTamanhoAtual() {
        return tamanhoAtual;
    }

    public int getId() {
        return idFila;
    }

    public void acumularTempo(int tamanho, long tempo) {
        long tempoAcumulado = tempos.get(tamanho);
        tempos.set(tamanho, tempoAcumulado + tempo);
    }

    public long getLastEventTime() {
        return lastEventTime;
    }

    public void setLastEventTime(long lastEventTime) {
        this.lastEventTime = lastEventTime;
        tempoUltimaChegada = lastEventTime;
    }
}