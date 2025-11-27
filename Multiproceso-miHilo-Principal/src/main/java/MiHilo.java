public class MiHilo extends Thread {
    private final int id;
    private final int tiempoEspera;
    private final int n;

    public MiHilo(int id, int tiempoEspera, int n) {
        this.id = id;
        this.tiempoEspera = tiempoEspera;
        this.n = n;
    }

    // Cuando se llama a start() sobre un objeto MiHilo se ejecuta el metodo run().
    @Override
    public void run() {
        try {
            contarHastaN();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void contarHastaN() throws InterruptedException {
        for (int i = 0; i < n; i++) {
            System.out.printf("%d : ID-> %d\n", i, id);
            Thread.sleep(tiempoEspera);
        }
    }
}
