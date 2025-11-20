public class Saludo {
    String nombre;
    int edad;

    public Saludo(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;

        imprimirSaludo();
    }

    private void imprimirSaludo() {
        System.out.println("Nombre: " + this.nombre + "\nEdad: " + this.edad);
    }
}
