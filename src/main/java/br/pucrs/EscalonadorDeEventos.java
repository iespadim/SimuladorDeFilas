package br.pucrs;

import br.pucrs.evento.IEvento;

import java.util.ArrayList;

public class EscalonadorDeEventos {

    static EscalonadorDeEventos instance;
    static ArrayList<IEvento> eventos;

    EscalonadorDeEventos() {
        if (instance != null) {
            throw new IllegalStateException("Já existe uma instância de EscalonadorDeEventos");
        } else {
            instance = this;
            eventos = new ArrayList<IEvento>();
        }
    }




    public void adicionarEvento(IEvento evento) {
        System.out.println("Adicionando evento: " + evento.getClass().getName() + " - agendado para t=" + evento.getTime());
        eventos.add(evento);
    }

    public IEvento proximoEvento() {
        ordenaEventosPorTempo();

        //System.out.println("Proximo evento: " + eventos.get(0).getClass().getName() + " - t=" + eventos.get(0).getTime());
        return eventos.remove(0);
    }

    private void ordenaEventosPorTempo() {
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
}
