package br.pucrs;

import br.pucrs.evento.IEvento;

import java.util.ArrayList;

public class EscalonadorDeEventos {

    static EscalonadorDeEventos instance;
    static ArrayList<Fila> filas;

    EscalonadorDeEventos() {
        if (instance != null) {
            throw new IllegalStateException("Já existe uma instância de EscalonadorDeEventos");
        } else {
            instance = this;
            filas = new ArrayList<>();
        }
    }


    public void adicionarEvento(Fila fila,IEvento evento) {
        System.out.println("agend: " + evento.getClass().getName() + " - para t=" + evento.getTime()+ " - na fila: " + fila.getClass().getName());

        //find fila in filas
        if (filas.contains(fila)) {
            //add evento to fila
            fila.eventos.add(evento);
        } else {
            throw new IllegalArgumentException("Fila não encontrada");
        }
    }



    public IEvento removeProximoEvento() {

        if(!filas.isEmpty()) {
            for (Fila fila : filas) {
                ordenaEventosPorTempo(fila);
            }
        }else {
            throw new IllegalArgumentException("Não há filas para processar");
        }


        //find nearest event in all filas
        IEvento closestEvent = null;
        Fila closestEventFila = null;

        for(Fila fila : filas){
            if(!fila.eventos.isEmpty()){
                IEvento evento = fila.eventos.get(0);

                if (closestEvent == null) {
                    closestEvent = evento;
                    closestEventFila = fila;
                } else {
                    if (evento.getTime() < closestEvent.getTime()) {
                        closestEvent = evento;
                        closestEventFila = fila;
                    }
                }
            }
        }
        return closestEventFila.eventos.remove(0);
    }

    private void ordenaEventosPorTempo(Fila fila) {
        ArrayList<IEvento> eventos = fila.eventos;
        //sort eventos by time
        for (int i = 0; i < eventos.size(); i++) {
               for (int j = 0; j < eventos.size(); j++) {
                    if (eventos.get(i).getTime() < eventos.get(j).getTime()) {
                        IEvento temp = eventos.get(i);
                        eventos.set(i, eventos.get(j));
                        eventos.set(j, temp);
                    }
                }
        }
    }

    public Fila getFilaById(int id){
        for(Fila fila : filas){
            if(fila.getId() == id){
                return fila;
            }
        }
        System.out.println("Fila não encontrada");
        return null;
    }

    public void adicionarFila(Fila fila1) {
        if (getFilaById(fila1.getId()) != null){
            throw new IllegalArgumentException("Fila já existe");
        }
        filas.add(fila1);
    }
}
