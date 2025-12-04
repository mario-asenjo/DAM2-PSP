public class Productor implements Runnable {
    private BufferCompartido bufferCompartido;
    private int idProductor;
    private int numPatatas;
    private final int CANTIDAD_A_PRODUCIR;
    private final int NUM_MAXIMO = 1000;

    public Productor(BufferCompartido bufferCompartido, int idProductor, int numPatatas, int cantidadPatatasAProducir) {
        this.bufferCompartido = bufferCompartido;
        this.idProductor = idProductor;
        this.numPatatas = numPatatas;
        this.CANTIDAD_A_PRODUCIR = cantidadPatatasAProducir;
    }

    @Override
    public void run() {
        Patata patata = null;
        int idPatata;

        if (CANTIDAD_A_PRODUCIR >= NUM_MAXIMO) {
            System.err.println("Error, no puedes introducir tantas patatas.");
            return ;
        }
        try {
            for (int i = 0; i < numPatatas; i++) {
                idPatata = idProductor * NUM_MAXIMO + i;
                bufferCompartido.addPatatas(new Patata(idPatata, "rusa", "ovalada", "roja", "recien cogida"));
                RandomSleepExample.main(null);
                System.out.printf("Productor %d, ha insertado patata con id -> %d\n", idProductor, idPatata);
            }
            System.out.printf("Productor %d ha insertado todo correctamente.\n", idProductor);
        } catch (InterruptedException | IllegalArgumentException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(ex);
        }
    }
}
