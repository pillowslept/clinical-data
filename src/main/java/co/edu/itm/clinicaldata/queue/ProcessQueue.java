package co.edu.itm.clinicaldata.queue;

import java.util.LinkedList;
import java.util.Queue;

public class ProcessQueue {

    private static ProcessQueue instance;
    private Queue<String> queve;

    private ProcessQueue() {
        this.queve = new LinkedList<>();
    }

    /**
     * Agrega a la cola de espera
     * 
     * @param identifier
     */
    public void add(String identifier) {
        getInstance().queve.add(identifier);
    }

    /**
     * Retorna y no remueve de la cola de espera
     * 
     * @return
     */
    public String get() {
        return getInstance().queve.peek();
    }

    /**
     * Retorna y remueve de la cola de espera
     * 
     * @return
     */
    public String remove() {
        return getInstance().queve.poll();
    }

    /**
     * Retorna instancia actual de la cola o crea una nueva de ser necesaria
     * 
     * @return
     */
    public static ProcessQueue getInstance() {
        if (instance == null) {
            instance = new ProcessQueue();
        }
        return instance;
    }

    /**
     * Retorna el tamaño de la cola de espera
     * 
     * @return
     */
    public int size() {
        return getInstance().queve.size();
    }

    /**
     * Determina si la cola esta vacía
     * @return
     */
    public boolean isEmpty() {
        return getInstance().queve.isEmpty();
    }
}
