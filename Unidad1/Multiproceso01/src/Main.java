import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    static boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

    public static void main(String[] args) {
        System.out.println("\n### Comienzo ejercicios MultiProceso: ###");
        try {
            multiProceso01();
            multiProceso02();
            multiProceso03();
            multiProceso04();
            multiProceso05();
        }  catch (IOException e) {
            System.err.printf("I/O ERROR: Error inesperado de lectura o escritura.\n%s\n", e.getMessage());
        } catch (NullPointerException e) {
            System.err.printf("Error Comandos: Ningún elemento del comando (comando, argumentos) puede ser nulo.\n%s\n", e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            System.err.printf("Error Comandos: La lista de comandos no puede tener 0 elementos.\n%s\n", e.getMessage());
        } catch (SecurityException e) {
            System.err.printf("Error de SecutiryManager: Existe un gestor de seguridad y ha propagado una excepción.\n%s\n", e.getMessage());
        } catch (UnsupportedOperationException e) {
            System.err.printf("Error de SO: Tu sistema no permite la creación de procesos.\n%s\n", e.getMessage());
        } catch (InterruptedException e) {
            System.err.printf("Error de Proceso: Hay otro proceso intentando acceder a este recurso mientras yo espero.\n%s\n", e.getMessage());
        }
        System.out.println("\n### Todos los ejercicios han finalizado. ###");
    }

    public static void multiProceso01() throws
            IOException, NullPointerException, IndexOutOfBoundsException, SecurityException, UnsupportedOperationException, InterruptedException{
        System.out.println("\n### EJERCICIO 1: Ejecutar dir/ls y guardar salida ###");
        List<String> comando1 = isWindows
                ? Arrays.asList("cmd.exe", "/c", "dir")
                : Arrays.asList("bash", "-c", "ls -l");

        File salida = new File("salida_ls.txt");
        File errores = new File("errores_ls.txt");
        LanzadorProcesos.ejecutarYRedirigir(comando1, salida, errores, null);
    }

    public static void multiProceso02() throws
            IOException, NullPointerException, IndexOutOfBoundsException, SecurityException, UnsupportedOperationException, InterruptedException {
        System.out.println("\n### EJERCICIO 2: Ejecutar y mostrar ping ###");
        List<String> comando2 = isWindows
                ? Arrays.asList("ping", "-n", "6", "google.com")
                : Arrays.asList("ping", "-c", "6", "google.com");
        LanzadorProcesos.ejecutarYMostrar(comando2);
    }

    public static void multiProceso03() throws
            IOException, NullPointerException, IndexOutOfBoundsException, SecurityException, UnsupportedOperationException, InterruptedException {
        System.out.println("\n### EJERCICIO 3: Ejecutar comandos secuenciales ###");
        List<List<String>> comandosSecuenciales = new ArrayList<>();
        if (isWindows) {
            comandosSecuenciales.add(Arrays.asList("cmd.exe", "/c", "echo", "Inicio de la tarea"));
            //comandosSecuenciales.add(Arrays.asList("cmd.exe", "/c", "timeout", "/t", "3"));
            comandosSecuenciales.add(Arrays.asList("cmd.exe", "/c", "timeout", "/t", "3", ">", "NULL"));
            //comandosSecuenciales.add(Arrays.asList("cmd.exe", "/c", "timeout", "/?"));
            comandosSecuenciales.add(Arrays.asList("cmd.exe", "/c", "echo", "%date% %time%"));
        } else {
            comandosSecuenciales.add(Arrays.asList("bash", "-c", "echo 'Inicio de la tarea'"));
            comandosSecuenciales.add(Arrays.asList("bash", "-c", "sleep 3"));
            comandosSecuenciales.add(Arrays.asList("bash", "-c", "date"));
        }
        LanzadorProcesos.ejecutarSecuencial(comandosSecuenciales);
    }

    public static void multiProceso04() throws
            IOException, NullPointerException, IndexOutOfBoundsException, SecurityException, UnsupportedOperationException, InterruptedException {
        System.out.println("\n### EJERCICIO 4: Ejecutar comandos en paralelo ###");
        List<List<String>> comandosSecuenciales = new ArrayList<>();
        if (isWindows) {
            comandosSecuenciales.add(Arrays.asList("cmd.exe", "/c", "ping", "localhost"));
            comandosSecuenciales.add(Arrays.asList("cmd.exe", "/c", "ping", "127.0.0.1"));
            comandosSecuenciales.add(Arrays.asList("cmd.exe", "/c", "ping", "127.0.0.1"));
        } else {
            comandosSecuenciales.add(Arrays.asList("bash", "-c", "ping", "-c", "4", "localhost"));
            comandosSecuenciales.add(Arrays.asList("bash", "-c", "ping", "-c", "4", "127.0.0.1"));
        }
        LanzadorProcesos.ejecutarSecuencial(comandosSecuenciales);
    }

    public static void multiProceso05() throws
            IOException, NullPointerException, IndexOutOfBoundsException, SecurityException, UnsupportedOperationException, InterruptedException {
        System.out.println("\n=== EJERCICIO 5: Control de errores ===");
        List<String> comandoErroneo = Arrays.asList("cmd.exe", "/c", "comando_que_no_existe");
        LanzadorProcesos.ejecutarYMostrar(comandoErroneo);
    }
}
