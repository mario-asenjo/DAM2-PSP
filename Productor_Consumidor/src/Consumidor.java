public class Consumidor implements Runnable {
    private BufferCompartido bufferCompartido;
    private int idConsumidor;
    private int cantidadAConsumir;

    public Consumidor(BufferCompartido bufferCompartido, int idConsumidor, int cantidadAConsumir) {
        this.bufferCompartido = bufferCompartido;
        this.idConsumidor = idConsumidor;
        this.cantidadAConsumir = cantidadAConsumir;
    }

    @Override
    public void run() {
        Patata patataConsumida;
        try {
            for (int i = 0; i < cantidadAConsumir; i++) {
                patataConsumida = bufferCompartido.consumirPatatas();
                System.out.println(patataConsumida.toString());
                RandomSleepExample.main(null);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
