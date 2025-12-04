public class Controlador {
    private final int CAPACIDAD_BUFFER;
    private final int CAPACIDAD_PRODUCTORES;
    private final int CAPACIDAD_CONSUMIDORES;
    private final int CANTIDAD_A_PRODUCIR;
    private final int CANTIDAD_A_CONSUMIR;

    private final BufferCompartido bufferCompartido;
    private Thread[] productores;
    private Thread[] consumidores;

    public Controlador(int cantidad_buffer, int cantidad_productores, int cantidad_consumidores, int cantidad_a_producir, int cantidad_a_consumir) {
        this.CAPACIDAD_BUFFER = cantidad_buffer;
        this.CAPACIDAD_PRODUCTORES = cantidad_productores;
        this.CAPACIDAD_CONSUMIDORES = cantidad_consumidores;
        this.CANTIDAD_A_PRODUCIR = cantidad_a_producir;
        this.CANTIDAD_A_CONSUMIR = cantidad_a_consumir;
        this.bufferCompartido = new BufferCompartido(CAPACIDAD_BUFFER);
        this.productores = new Thread[CAPACIDAD_PRODUCTORES];
        this.consumidores = new Thread[CAPACIDAD_CONSUMIDORES];
        crearProductores();
        ponerEnMarchaLosProductores();
        crearConsumidores();
        ponerEnMarchaLosConsumidores();
    }

    public int getCAPACIDAD_BUFFER() {
        return CAPACIDAD_BUFFER;
    }

    public int getCAPACIDAD_PRODUCTORES() {
        return CAPACIDAD_PRODUCTORES;
    }

    public int getCAPACIDAD_CONSUMIDORES() {
        return CAPACIDAD_CONSUMIDORES;
    }

    public int getCANTIDAD_A_PRODUCIR() {
        return CANTIDAD_A_PRODUCIR;
    }

    public int getCANTIDAD_A_CONSUMIR() {
        return CANTIDAD_A_CONSUMIR;
    }

    private void crearProductores() {
        for (int i = 0; i < CAPACIDAD_PRODUCTORES; i++) {
            productores[i] = new Productor(bufferCompartido, i,).run();
        }
    }

}
