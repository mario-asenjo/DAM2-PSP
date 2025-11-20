public class Main {
    public static void main(String[] args) {
        String nombre;
        int edad;

        if (args.length != 2) {
            System.out.println("ERROR en la cantidad de argumentos");
            return ;
        }
        nombre = args[0];
        edad = Integer.parseInt(args[1]);

        Saludo saludo = new Saludo(nombre, edad);
    }
}
