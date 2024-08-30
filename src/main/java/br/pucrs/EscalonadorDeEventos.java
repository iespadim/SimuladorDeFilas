package br.pucrs;

import br.pucrs.evento.Evento;

import java.util.ArrayList;

public class EscalonadorDeEventos {

    static EscalonadorDeEventos instance;
    static ArrayList<Evento> eventos;

    EscalonadorDeEventos() {
        if (instance != null) {
            throw new IllegalStateException("Já existe uma instância de EscalonadorDeEventos");
        } else {
            instance = this;
            eventos = new ArrayList<Evento>();
        }
    }


    public void adicionarEvento(Evento evento) {
    }

    public Evento proximoEvento() {
        ordenaEventosPorTempo();

        return eventos.get(0);
    }

    private void ordenaEventosPorTempo() {
        //sort eventos by time
        for (int i = 0; i < eventos.size(); i++) {
               for (int j = 0; j < eventos.size(); j++) {
                    if (eventos.get(i).getTime() < eventos.get(j).getTime()) {
                        Evento temp = eventos.get(i);
                        eventos.set(i, eventos.get(j));
                        eventos.set(j, temp);
                    }
                }
        }
    }
}
