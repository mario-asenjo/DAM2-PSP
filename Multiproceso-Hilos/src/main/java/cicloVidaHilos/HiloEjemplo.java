package cicloVidaHilos;

public class HiloEjemplo extends Thread {

    private final long tiempoEspera;


    public HiloEjemplo(String nombre, long tiempoEspera) {
        super(nombre);
        this.tiempoEspera = tiempoEspera;
    }

    @Override
    public void run() {
        try {
            // 1. RUNNABLE/RUNNING (el hilo está ejecutándose
            System.out.println("Inicio de ejecución de " + getName() );

            // Detalles del hilo
            System.out.println("\n================================");
            System.out.println("ID: \t\t\t\t" + getId() );
            System.out.println("Nombre: \t\t\t" +  getName() );
            System.out.println("Prioridad inicial:\t" + getPriority() );
            System.out.println("Estado: \t\t\t" + getState() );
            System.out.println("================================\n");

            // Cambiamos la prioridad
            setPriority( MAX_PRIORITY );  // Min: 1 - Max: 10
            System.out.println("Nueva prioridad: " +  getPriority() );

            for (int i = 0; i < 10; i++) {
                if ( isInterrupted() ){
                    throw new InterruptedException("Hilo: "+ getName() +": Interrumpido en el paso " + i);
                }
                System.out.println("Hilo " + getName() + ": contador: " + i);

                // 2. TIMED_WAITING  (Hilo temporalmente inactivo
                Thread.sleep(tiempoEspera);
            }
            // toString de la clase Thread
            System.out.printf("\n\nInfo. detallada: %s \n\n", this.toString());
        } catch (InterruptedException e) {
            // Se ejecuta si el hilo es interrumpido mientras está en sleep/wait/join
            System.out.println(getName() + " se ha interrumpido. Mensaje: " + e.getMessage());
            // isInterrupted() será falso si la InterruptedException fue lanzada
            System.out.println("-> isInterrupted() después de excepción: " + isInterrupted());
        } finally {
            System.out.println("--- Fin de ejecución de " + getName() + " ---");
        }
    }
}
