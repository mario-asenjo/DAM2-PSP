package contadorVocalesHilos;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Padre {
    private final static int NUM_HILOS = 5;
    private final static char[] VOCALES = {'A', 'B', 'C', 'D', 'E'};
    private List<Hijo> listaHilos;
    private Map<Character, Integer> resultadosCuentas;
    private final String TEXTO = """
            In this reprint several minor inaccuracies, most of them noted by readers, have\n
            been corrected. For example, the rune text now corresponds exactly with the runes\n
            on  Thror's  Map.  More  important  is  the  matter  of  Chapter  Five.  There  the  true\n
            story  of  the  ending  of  the  Riddle  Game,  as  it  was  eventually  revealed  (under\n
            pressure)  by Bilbo  to Gandalf,  is  now  given  according  to  the Red Book,  in  place\n
            of  the  version  Bilbo  first  gave  to  his  friends,  and  actually  set  down  in  his  diary.\n
            This  departure  from  truth  on  the  part  of  a  most  honest  hobbit  was  a  portent  of\n
            great  significance.  It  does  not,  however,  concern  the  present  story,  and  those who\n
            in this edition make their first acquaintance with hobbit-lore need not troupe about\n
            it.  Its  explanation  lies in the history of the Ring, as it was set out in the chronicles\n
            of the Red Book of Westmarch, and is now told in The Lord of the Rings.\n
            \n
            A final note may be added, on a point raised by several students of  the  lore of\n
            the  period.  On  Thror's  Map  is  written  Here  of  old  was  Thrain  King  under  the\n
            Mountain;  yet  Thrain  was  the  son  of  Thror,  the  last  King  under  the  Mountain\n
            before  the  coming  of  the  dragon.  The  Map,  however,  is  not  in  error.  Names  are\n
            often  repeated  in  dynasties,  and  the  genealogies  show  that  a  distant  ancestor  of\n
            Thror  was  referred  to,  Thrain  I,  a  fugitive  from  Moria,  who  first  discovered  the\n
            Lonely Mountain, Erebor, and ruled there for a while, before his people moved on\n
            to the remoter mountains of the North.
            """;

    public Padre() {
        inicializarMapa();
        inicializarHijos();
    }

    private void inicializarMapa() {
        resultadosCuentas = new LinkedHashMap<>(NUM_HILOS);
        for (int i = 0; i < NUM_HILOS; i++) {
            resultadosCuentas.put(VOCALES[i], 0);
        }
    }

    private void inicializarHijos() {
        listaHilos = new ArrayList<>(NUM_HILOS);
        for (int i = 0; i < NUM_HILOS; i++) {
            listaHilos.add(new Hijo(String.format("Hilo para letra: %c", VOCALES[i]), TEXTO, VOCALES[i]));
        }
    }

    private void arrancarHijos() {
        for (Hijo hijo : listaHilos) {
            hijo.start();
        }
    }

    private void unirHijos() {
        for (Hijo hijo : listaHilos) {
            try {
                hijo.join();
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void leerResultados() {
        for (Hijo hijo : listaHilos) {

        }
    }
}
