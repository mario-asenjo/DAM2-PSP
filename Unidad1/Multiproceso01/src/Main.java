import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

    }

    public static void multiProceso01() {
        // Variable que indica si el sistema es windows o no.
        boolean         isWindows;

        // Array que contendrá el comando con sus argumentos.
        List<String>    comando;

        // Constructor del proceso.
        ProcessBuilder pb;

        // Proceso creado con el constructor de procesos.
        Process proceso;

        // Código de salida del proceso.
        int exitCode;

        // Comprobamos sistema operativo en tiempo de ejecución.
        isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        // Dependiendo del sistema operativo detectado, el
        // comando cambia.
        comando = isWindows ? List.of("dir", "/a:h") : List.of("ls", "-l");

        // Creamos el proceso y redirigimos el stdout y el stderr
        // a un fichero en la ruta del ejecutable.
        pb = new ProcessBuilder(comando);
        pb.redirectOutput(new File("salida-ls.txt"));
        pb.redirectError(new File("salida-ls.txt"));
        // Se puede establecer la ruta sobre la cual actua el proceso
        // de la siguiente manera:
        // pb.directory(new File("ruta/nueva"));

        try {
            proceso = pb.start();
            exitCode = proceso.waitFor();
            System.out.printf("El proceso ha finalizado con código: %d\n", exitCode);
        } catch (IOException e) {
            System.err.println("I/O ERROR: Error inesperado de lectura o escritura.");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Error Comandos: Ningún elemento del comando (comando, argumentos) puede ser nulo.");
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error Comandos: La lista de comandos no puede tener 0 elementos.");
            e.printStackTrace();
        } catch (SecurityException e) {
            System.err.println("Error de SecutiryManager: Existe un gestor de seguridad y ha propagado una excepción.");
            e.printStackTrace();
        } catch (UnsupportedOperationException e) {
            System.err.println("Error de SO: Tu sistema no permite la creación de procesos.");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Error de Proceso: Hay otro proceso intentando acceder a este recurso mientras yo espero.");
        }
    }
    public static void multiProceso02() {
        // Variable que indica si el sistema es windows o no.
        boolean         isWindows;

        // Array que contendrá el comando con sus argumentos.
        List<String>    comando;

        // Constructor del proceso.
        ProcessBuilder pb;

        // Proceso creado con el constructor de procesos.
        Process proceso;

        // Código de salida del proceso.
        int exitCode;

        // Comprobamos sistema operativo en tiempo de ejecución.
        isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        // Dependiendo del sistema operativo detectado, el
        // comando cambia.
        comando = isWindows ? List.of("ping", "-n", "6", "google.com") : List.of("ping", "-c", "6", "google.com");

    }
}
