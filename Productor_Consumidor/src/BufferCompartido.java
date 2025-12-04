import java.util.LinkedList;
import java.util.Queue;

public class BufferCompartido {
    private final Queue<Patata> colaPatatas;
    private final int capacidad;

    public BufferCompartido(int capacidad) {
        this.colaPatatas = new LinkedList<Patata>();
        this.capacidad = capacidad;
    }

    public synchronized void addPatatas(Patata patata) throws InterruptedException {
        while (colaPatatas.size() == capacidad) {
            wait();
        }
        colaPatatas.add(patata);
        notifyAll();
    }

    public synchronized Patata consumirPatatas() throws InterruptedException {
        Patata patata = null;
        while (colaPatatas.isEmpty()) {
            wait();
        }
        patata = colaPatatas.poll();
        notifyAll();
        return patata;
    }
}
