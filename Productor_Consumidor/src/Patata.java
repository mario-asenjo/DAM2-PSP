public class Patata {
    int idPatata;
    String tipo;
    String forma;
    String color;
    String estado;

    public Patata(int id, String tipo, String forma, String color, String estado){
        this.tipo = tipo;
        this.forma = forma;
        this.color = color;
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getForma() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Patatas{" +
                "tipo='" + tipo + '\'' +
                ", forma='" + forma + '\'' +
                ", color='" + color + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
