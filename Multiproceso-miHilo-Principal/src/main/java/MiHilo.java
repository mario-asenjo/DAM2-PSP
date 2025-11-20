public class MiHilo extends Thread {
    // Cuando se llama a start() sobre un objeto MiHilo se ejecuta el metodo run().
    public void run() {
        try {
            contarHastaN();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void contarHastaN() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            if (i == 5) Thread.sleep(5000);
            System.out.println(i);
        }
    }
}
