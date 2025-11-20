import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class ProcesoPadre {
    private final char[] VOCALES_LIST = { 'a', 'e', 'i', 'o', 'u'};
    private final String TEXTO;

    public ProcesoPadre(String texto_de_fichero) {
        this.TEXTO = texto_de_fichero;
    }

    public void ejecutar() {
        List<Process> listaProcesos = new ArrayList<>();

    }

    public void crearProcesosHijos(List<Process> listaProcesos) {
        char vocal;
        Process proceso;

        System.out.println("CREANDO PROCESOS");

        for (int i = 0; i < VOCALES_LIST.length; i++) {
            vocal = VOCALES_LIST[i];
            proceso = crearProcesoHijo(vocal);
            if (proceso != null) {
                listaProcesos.add(proceso);
            } else {
                System.out.println("Error creando proceso!");
            }
        }
    }

    private Process crearProcesoHijo(char vocal) {
        ProcessBuilder pb;
        String comando;
        String clase;
        String rutaClases;
        String directorioActual;
        String textoArgumento;
        String vocalArgumento;
        File archivoDirActual;

        comando = "java";
        clase = "ProcesoHijo";
        directorioActual = System.getProperty("user.dir");
        rutaClases = directorioActual + "/out/production/ContadorVocalesMultiproceso";
        textoArgumento = TEXTO;
        vocalArgumento = String.valueOf(vocal);

        archivoDirActual = new File(directorioActual);
        pb = new ProcessBuilder(comando, "-cp", rutaClases, clase, textoArgumento, vocalArgumento);
        pb.directory(archivoDirActual);
        pb.redirectErrorStream(true);

        return iniciarProceso(pb);
    }

    private Process iniciarProceso(ProcessBuilder pb) {

    }
}
