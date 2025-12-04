package ContarVocalesConHilos;

import java.util.Locale;

public class Hijo extends Thread {
    String nombre;
    String texto;
    char letraQueBusca;
    int posicion;
    int[] arrayResultados;

    public Hijo(String nombre, String texto, char letraQueBusca, int posicion, int [] arrayResultados) {
        this.nombre = nombre;
        this.texto = texto;
        this.letraQueBusca = letraQueBusca;
        this.posicion = posicion;
        this.arrayResultados = arrayResultados;
    }

    @Override
    public void run() {
        int resultadoContador;
        resultadoContador = contarLetras();
        arrayResultados[posicion] = resultadoContador;
    }

    private int contarLetras(){
        int contador = 0;
        char letraMayuscula = normalizarLetraBuscada(letraQueBusca);

        for (int i = 0; i < texto.length(); i++) {
            if (texto.charAt(i) == letraMayuscula ||
                texto.charAt(i) == (char)(letraMayuscula + 32) )  {
                contador++;
            }
        }
        return contador;
    }

    /**
     * Convertimos la letra a su expresión en mayúscula, basándonos en los códigos ASCII de las letras
     * @param letraQueBusca
     * @return
     */
    private char normalizarLetraBuscada(char letraQueBusca) {
        if( letraQueBusca >= 65 && letraQueBusca <= 90){
            // tengo la letra mayúscula
            return letraQueBusca;
        }
        else if ( letraQueBusca >= 97 && letraQueBusca <= 122){
            // tengo la letra minúscula
            return (char) (letraQueBusca - 32);
        }
        else {
            throw new IllegalArgumentException("Letra no valida");
        }
    }

}
