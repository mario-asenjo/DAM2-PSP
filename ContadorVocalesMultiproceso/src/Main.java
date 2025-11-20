public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Error! Debes mandarme un fichero a leer!");
            System.exit(1);
        }
        ProcesoPadre app = new ProcesoPadre(args[0]);

        app.ejecutar();
    }
}
