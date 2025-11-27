package contadorVocalesHilos;

public class Hijo extends Thread {
    private String nombreHilo;
    private String texto;
    private char letraQueBuscaMinuscula;
    private char letraQueBuscaMayuscula;

    public Hijo(String nombreHilo, String texto, char letraQueBusca) throws IllegalArgumentException {
        this.nombreHilo = nombreHilo;
        this.texto = texto;
        if (letraQueBusca >= 65 && letraQueBusca <= 90) {
            letraQueBuscaMayuscula = letraQueBusca;
            letraQueBuscaMinuscula = (char) (letraQueBuscaMayuscula + 32);
        } else if (letraQueBusca >= 97 && letraQueBusca <= 122) {
            letraQueBuscaMinuscula = letraQueBusca;
            letraQueBuscaMinuscula = (char) (letraQueBuscaMinuscula - 32);
        } else {
            throw new IllegalArgumentException("La letra introducida no es valida!");
        }
    }

    @Override
    public void run() {
        int ret = contarLetras();
    }

    private int contarLetras() {
        int cuenta = 0;

        for (char c : texto.toCharArray()) {
            if (c == letraQueBuscaMinuscula || c == letraQueBuscaMayuscula) {
                cuenta++;
            }
        }
        return cuenta;
    }
}
