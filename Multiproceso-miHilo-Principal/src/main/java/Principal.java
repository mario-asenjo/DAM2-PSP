public class Principal {
    public static void main(String[] args) {
        MiHilo miHilo1 = new MiHilo(1, 3000, 10);
        MiHilo miHilo2 = new MiHilo(2, 1000, 10);

        miHilo1.start();
        miHilo2.start();
    }
}
