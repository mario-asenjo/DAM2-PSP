import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class LanzadorProcesos {
    public static int ejecutarYRedirigir(List<String> comando, File salida, File errores, File directorio) throws
            IOException, NullPointerException, IndexOutOfBoundsException, SecurityException, UnsupportedOperationException, InterruptedException {
        ProcessBuilder  pb;
        int             exitCode;
        Process         proceso;

        /* Inicializamos en estado de error por si no se modificase, que saltase el error. */
        exitCode = -1;
        pb = new ProcessBuilder(comando);
        if (directorio != null) pb.directory(directorio);
        pb.redirectOutput(salida);
        pb.redirectError(errores);
        proceso = pb.start();
        exitCode = proceso.waitFor();
        System.out.println("Comando: " + String.join(" ", comando) + " → Código de salida: " + exitCode);
        return (exitCode);
    }

    public static int ejecutarYMostrar(List<String> comando) throws
            IOException, NullPointerException, IndexOutOfBoundsException, SecurityException, UnsupportedOperationException, InterruptedException {
        String          linea;
        ProcessBuilder  pb;
        Process         proceso;
        BufferedReader  br;
        int             exitCode;

        exitCode = -1;
        pb = new ProcessBuilder(comando);
        pb.redirectErrorStream(true);
        proceso = pb.start();
        br = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
        while ((linea = br.readLine()) != null) {
            System.out.println(linea);
        }
        br.close();
        exitCode = proceso.waitFor();
        System.out.println("Código de salida: " + exitCode);
        return (exitCode);
    }

    public static void ejecutarSecuencial(List<List<String>> comandos) throws
            IOException, NullPointerException, IndexOutOfBoundsException, SecurityException, UnsupportedOperationException, InterruptedException {
        for (List<String> cmd : comandos) {
            ejecutarYMostrar(cmd);
        }
    }

    /*
    * Método que separa la lógica de lectua en hilos para no bloquear la lectura sobre cada proceso
    * de forma que conseguimos paralelismo en la ejecución sin esperar a que el primer lector termine
    * para empezar a leer con el segundo lector, cada uno sobre un proceso.
    *
    public static void ejecutarEnParalelo(List<List<String>> comandos) throws
            IOException, NullPointerException, IndexOutOfBoundsException, SecurityException, UnsupportedOperationException, InterruptedException, IllegalThreadStateException {
        List<Process>   procesos;
        ProcessBuilder  pb;
        AtomicReference<BufferedReader> br;

        procesos = new ArrayList<>();
        br = null;
        for (List<String> cmd : comandos) {
            pb = new ProcessBuilder(cmd);
            pb.redirectErrorStream(true);
            procesos.add(pb.start());
        }
        // Leer salidas en hilos separados
        for (Process p : procesos) {
            new Thread(() -> {
                br.set(new BufferedReader(new InputStreamReader(p.getInputStream())));
                String linea;
                while ((linea = br.get().readLine()) != null) {
                    System.out.println(linea);
                }
            }).start();
        }

        // Esperar a que terminen todos
        for (Process p : procesos) {
            p.waitFor();
        }
        System.out.println("Todos los procesos en paralelo han finalizado.");
    }
    */

}
