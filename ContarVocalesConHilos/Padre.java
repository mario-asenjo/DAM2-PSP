package ContarVocalesConHilos;

/**
 * Contador de vocales en un texto
 * <p>
 * Crea varios (5) hijos para ayudarle en la tarea
 *
 */
public class Padre {

    Thread hijos[];
    int[] recuentoVocales; // array de resultados de recuento de vocales 0:A; 1;B ...
    final static int POS_A = 0;
    final static int POS_E = 1;
    final static int POS_I = 2;
    final static int POS_O = 3;
    final static int POS_U = 4;

    final static int NUM_HIJOS = 5;

    final static String TEXTO = Quijote.QUIJOTE;

    public Padre() {
        Hijo hijo;
        char[] vocales = {'a', 'e', 'i', 'o', 'u'};
        hijos = new Thread[NUM_HIJOS];
        recuentoVocales = new int[NUM_HIJOS];

        // creamos los hijos y los ubicamos en el array de hijos,
        // para poder recorrerlos sin necesidad de crear una variable para cada uno de ellos
        for (int i = 0; i < NUM_HIJOS; i++) {
            hijo = new Hijo("Hijo " + i, TEXTO, vocales[i], i, recuentoVocales);
            hijos[i] = hijo;
        }
    }

    public void iniciar() {
        // iniciamos los hijos
        for (int i = 0; i < NUM_HIJOS; i++) {
            hijos[i].start();
        }

        // esperamos a la muerte de todos nuestros hijos
        for (int i = 0; i < NUM_HIJOS; i++) {
            try {
                hijos[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // leemos los resultados
        for (int i = 0; i < NUM_HIJOS; i++) {
            System.out.println(recuentoVocales[i]);
        }
    }
}
