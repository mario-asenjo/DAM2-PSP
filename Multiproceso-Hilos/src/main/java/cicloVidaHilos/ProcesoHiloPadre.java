package cicloVidaHilos;

public class ProcesoHiloPadre {

    /**
     * Seguimiento del Ciclo de Vida del Thread
     * El código muestra los siguientes estados del ciclo de vida de un hilo de Java:
     *
     *      NEW (Nuevo):
     *          El hilo ha sido creado (new HiloEjemplo(...)) pero el metodo start() aún no ha sido invocado.
     *          Se ve justo antes de hilo1.start().
     *
     *      RUNNABLE (Ejecutable):
     *          El hilo ha llamado a start() y está listo para ejecutarse, o el planificador de la CPU lo está
     *          ejecutando actualmente. Se ve justo después de hilo1.start() y durante la mayor parte de su vida útil.
     *
     *      Nota: Cuando está activamente ejecutándose, a veces se le llama RUNNING, aunque Java no tiene un estado
     *          de enumeración separado para esto; se engloba en RUNNABLE.
     *
     *      TIMED_WAITING (Espera Temporizada):
     *          El hilo ha llamado a un metodo con tiempo de espera, como Thread.sleep(tiempoEspera) o thread.join(ms).
     *
     *      BLOCKED / WAITING:
     *          Estos estados son más comunes en escenarios de sincronización (por ejemplo, al esperar un bloqueo
     *          de monitor o llamar a wait()), pero no se ilustran directamente en este ejemplo sencillo.
     *
     *      TERMINATED (Terminado): El metodo run() del hilo ha completado su ejecución. Se muestra al final,
     *          después de que el hilo hilo2 ha finalizado.
     *
     *      Este ejemplo nos muestra cómo el metodo interrupt() del hilo principal puede sacar a hilo1 del
     *          estado TIMED_WAITING al lanzar una InterruptedException.
     * 
     * @param args
     * @throws InterruptedException
     */
    private Thread hiloPrincipal;
    private HiloEjemplo hilo1;
    private HiloEjemplo hilo2;

    public ProcesoHiloPadre() throws InterruptedException {
        crearCicloVidaHilosHijos();
    }

    private void crearCicloVidaHilosHijos() {
        // Obtenemos el hilo principal
        hiloPrincipal = Thread.currentThread();
        System.out.printf("Hilo principal actual: %d \n\n" , hiloPrincipal.getId() );

        // 1. Creamos el hilo 1
        hilo1 = new HiloEjemplo("Proceso 01", 500);

        // El hilo está creado, pero aún no se ha ejecutado
        consultarEstadoDelHilo("antes de start", hilo1);

        // 2. Iniciamos el hilo
        hilo1.start();

        // RUNNABLE (el hilo está listo para ejecutarse o ya se está ejecutando)
        // su estado puede cambiar muy rápido
        consultarEstadoDelHilo("después de start", hilo1);

        // 3. INTERRUPCIÓN DEL HILO 1 (después de un tiempo breve)
        // Usamos el hilo principal para interrumpir al hilo1
        Thread.sleep(1500);

        System.out.printf("\n---> Hilo Principal: voy a interrumpir a %s ::>\n", hilo1.getName());
        hilo1.interrupt();  // Marcamos el hilo para ser interrumpido (establecemos el flag isInterrupted a true)

        consultarEstadoDelHilo("Interrumpido por el hilo principal, posiblemente TIMED_WAITING/RUNNABLE", hilo1);
        //System.out.println("Estado de Hilo 1 (posiblemente TIMED_WAITING/RUNNABLE): " + hilo1.getState());

        // 4. CREACIÓN Y EJECUCIÓN DEL HILO 2
        hilo2 = new HiloEjemplo("Proceso 02", 1000);
        hilo2.setName("Hilo_2");
        hilo2.start();

        // 5. Esperamos a que el hilo1 (pese a la interrupción) termine su proceso
        try {
            System.out.println("Hilo principal esperando a que hilo1 termine (join)");
            hilo1.join();
            consultarEstadoDelHilo("después de join", hilo1);
        }
        catch (InterruptedException e) {
            System.out.println("El hilo principal se ha interrumpido mientras esperaba");
        }

        // 6. Monitorización final del hilo2
        // Forzamos al hilo principal a esperar a que hilo2 termine
        hilo2.join();

        // TERMINATED. El hilo ha terminado su ejecución
        consultarEstadoDelHilo("al final", hilo2);

        System.out.println("Fin del hilo principal");
    }

    /**
     * Metodo que nos muestra el estado del hilo recibido en cada momento
     * @param momento
     * @param hilo
     */
    public static void consultarEstadoDelHilo(String momento, Thread hilo){
        System.out.printf("\n\t\t\t\t\t\t---------------------------------------------------------");
        System.out.printf("\n\t\t\t\t\t\tEstado del %s ( %s ): %s",  hilo.getName(), momento, hilo.getState());
        System.out.printf("\n\t\t\t\t\t\t¿Vive %s ? >>> %s <<<", hilo.getName(), hilo.isAlive());
        System.out.printf("\n\t\t\t\t\t\t---------------------------------------------------------\n");
    }
}
