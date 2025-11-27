package cicloVidaHilos;

public class Main {
    public static void main(String[] args) {
        try {
            ProcesoHiloPadre procesoPadre = new ProcesoHiloPadre();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
}
