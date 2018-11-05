package com.rubenbermejo.fml.ahorcado;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MecanicaJuego {

    //DECLARACIÓN DE VARIABLES Y OBJETOS NECESARIOS.

    List<Character> caracteres = new ArrayList<>();
    List<String> posiciones = new ArrayList<>();
    Random generador = new Random();

    private String palabra;
    private String[] palabras = new String[] {"SUSPENSO", "TIEMPO", "ABRAZO", "VOLAR", "GORRO", "JARDINERIA", "ANDROID", "JAVA", "WINDOWS", "AVE"};
    private char[] mascara = new char[20];
    private List<String[]> caracteresRepetidos = new ArrayList<>();
    private int vidas;
    private int puntuacion = 0;

    /*CONSTRUCTOR QUE CREA UNA NUEVA INSTANCIA DEL JUEGO
      CON UNA PALABRA COMPLETAMENTE ALEATORIA DEL ARRAY
      DE STRING DECLARADO ARRIBA.
     */

    public MecanicaJuego(boolean comodin) {
        palabra = palabras[generador.nextInt(9)];

        for (int i = 0; i<palabra.length();i++) {
            mascara[i] = '-';
        }

        estableceRepeticionesEnPalabra(palabra);

        for (int i = 97; i <= 122; i++) {
            caracteres.add(Character.valueOf((char) i));
            if (i == 110) {
                caracteres.add('ñ');
            }
        }

        for (int i = 0; i < palabra.length(); i++) {
            posiciones.add(String.valueOf(i + 1));
        }

        if (comodin) {
            posiciones.add("*");
        }

        vidas = 5;
    }

    /* CONSTRUCTOR QUE GENERA UNA NUEVA INSTANCIA DEL JUEGO CON UNA
       PALABRA EN CONCRETO, TENIENDO EN CUENTA EL INDICE ESPECIFICADO
       EN EL CONSTRUCTOR.
     */

    public MecanicaJuego(boolean comodin, int noPalabra) {
        palabra = palabras[noPalabra];

        for (int i = 0; i<palabra.length();i++) {
            mascara[i] = '-';
        }

        estableceRepeticionesEnPalabra(palabra);

        for (int i = 97; i <= 122; i++) {
            caracteres.add(Character.valueOf((char) i));
            if (i == 110) {
                caracteres.add(Character.valueOf((char) 164));
            }
        }

        for (int i = 0; i < palabra.length(); i++) {
            posiciones.add(String.valueOf(i+1));
        }

        if (comodin) {
            posiciones.add("*");
        }

        vidas = 5;
    }

    //GETTERS Y SETTERS

    public String getCurrentMascara() {
        String mascaraSt = "";
        StringBuilder stB = new StringBuilder(mascaraSt);
        for(int i = 0;i<palabra.length();i++) {
            stB.append(mascara[i]);
        }
        return stB.toString();
    }

    public String getPalabra() {
        return this.palabra;
    }

    public List<Character> getListaCaracteres(){
        return this.caracteres;
    }

    public List<String> getListaPosiciones(){
        return this.posiciones;
    }

    public int getVidas(){
        return this.vidas;
    }

    public int getPuntuacion() {
        return this.puntuacion;
    }


    public void setDificultad(int dificultad){

        /*
            Los valores presentes en el switch son
            los IDs que toman cada objeto de
            radiobutton cuando se crean. Están probados
            de que funcionan de manera consistente.
         */

        switch (dificultad) {
            case 2131165238:
                vidas = 15;
                break;
            case 2131165271:
                vidas = 10;
                break;
            case 2131165251:
                vidas = 5;
                break;
        }
    }

    //METODOS FUNCIONALES

    public void descubreMascara(char caracter, int posicion) {

        /*Este metodo cada vez que es llamado, cogerá la máscara de la
        palabra, y descubrirá en la posición especificada por parámetros,
        la letra especificada por parámetros.

        Este método sólo es llamado cuando se ha acertado una letra.
        */

        mascara[posicion] = caracter;
    }

    public void juegaLetra(char letra, String posicion) {

        /*
        Este método toma por parámetros un caracter y una posición, que
        vienen de los dos Spinners, por lo que no hay manera de que el
        usuario pueda introducir algo distinto.
        La posición puede ser también un asterisco, en caso de que
        se escoja el comodín.
        */

        /*
        Esta sentencia if se ejecutará si el usuario usa el asterisco,
        lo cual hará que el juego busque si hay algún caracter en la palabra
        que coincida con el caracter que el jugador ha escogido.
         */

        if (posicion.equals("*")){

            boolean letraHallada = false;

            /*
            Este bucle for será el que busque cualquier caracter que coincida
            con el especificado en la palabra escogida. Si encuentra algún caracter
            que coincida, lo desvelará en la máscara, eliminará el caracter del
            ArrayList de Character, y sumará uno (1) a la puntuación actual.
             */

            for (int i = 0; i<palabra.length(); i++) {
                if (palabra.charAt(i) == letra) {
                    descubreMascara(letra, i);
                    letraHallada = true;
                    eliminaLetraArray(letra);
                    puntuacion++;
                }
            }

            /*
            Si no hay ningún caracter que coincida con el especificado por el jugador,
            el booleano "letraHallada" será falso. Por consiguiente, ejecutará esta
            sentencia if, que restará uno (1) al valor de vidas actual.
             */

            if (!letraHallada) {
                vidas--;
            }

            /*
            Si el jugador no ha escogido el astersico, significa que han escogido
            una posición en la palabra. Esta sentencia else comprobará si el carácter
            y la posición escogidas por el usuario, coinciden con el caracter en esa
            posición en la palabra escogida. Si coincide, revelará ese caracter en esa
            posición en la mascara, y sumará cinco (5) a la puntuación actual.
            */
        } else {
            if (palabra.charAt(Integer.parseInt(posicion)-1) == letra) {
                descubreMascara(letra, Integer.parseInt(posicion)-1);

                /*
                Esta sentencia if controlará si ya se han usado un caracter en concreto
                todas las veces necesarias para poder descubrir la palabra. Si es así,
                este caracter será eliminado del ArrayList de caracteres, para no poder
                escogerla más.
                 */

                if (frecuenciaDescubiertasChar(letra) == devuelveRepeticionesEnPalabra(letra)){
                    eliminaLetraArray(letra);
                }

                eliminaPosicionArray(Integer.parseInt(posicion));
                puntuacion = puntuacion + 5;
                /*
                Si el caracter y la posición escogida por el jugador no coincide con el
                caracter en esa posición en la palabra escogida, se restará uno (1) al
                valor de vidas actual.
                 */
            } else {
                vidas--;
            }
        }
    }

    private void eliminaLetraArray(char letra) {

        //Este método se encarga de eliminar un carácter del ArrayList de caracteres.

        for (int i = 0; i < caracteres.size(); i++) {
            if (caracteres.get(i).toString().toUpperCase().charAt(0) == letra) {
                caracteres.remove(i);
                break;
            }
        }
    }

    private void eliminaPosicionArray(int valor){

        //Este método se encarga de eliminar una posición del ArrayList de posiciones.

        for (int i = 0; i < posiciones.size(); i++) {
            if (posiciones.get(i) == String.valueOf(valor)) {
                posiciones.remove(i);
                break;
            }
        }
    }


    public int estadoJuego() {

        /*
       Este método se encarga de devolverle al Activity (en este caso, la clase
       principal.java), si los requisitos para ganar, o perder, se cumplen, y
       que en el Activity se informe al jugador si ha ganado, si ha perdido, o si
       aun tiene que seguir jugando.

            ESTADOS DE JUEGO

            0 = EL JUEGO NO CUMPLE NINGUNO DE LOS ESTADOS DE VICTORIA O DERROTA
            1 = EL JUEGO CUMPLE CON LOS REQUISITOS PARA LA DERROTA
            2 = EL JUEGO CUMPLE CON LOS REQUISITOS PARA LA VICTORIA
         */

        int estado = 0;

        if (vidas < 1) {
            estado = 1;
        } else if (mascaraDescubierta()) {
            estado = 2;
        }

        return estado;
    }

    private boolean mascaraDescubierta() {

        /* Este método existe para poder determinar si la
        máscara está completamente desvelada. Este método
        se usa exclusivamente para usarlo en el método
        estadoJuego() en esta misma clase.

         */

        boolean descubierta = true;

        for (int i = 0; i < mascara.length; i++) {
            if (mascara[i] == '-') {
                descubierta = false;
                break;
            }
        }
        return descubierta;
    }

    /* Los tres métodos siguientes se encargan de poder eliminar un carácter del ArrayList
    de Character cuando ya se han usado todas las veces posibles para poder descubrir una
    palabra.
     */

    private void estableceRepeticionesEnPalabra(String palabra) {

        /* Este primer método se encarga de, cuando se genera una nueva instancia del juego,
        de almacenar dentro de un ArrayList de arrays de String, la cantidad de repeticiones
        de cada letra dentro de la palabra escogida. Para la palabra "JAVA", se almacenarían los
        siguientes arrays en el ArrayList:

                ["J", "1"]
                ["A", "2"]
                ["V", "1"]
                ["A", "2"]

        Las letras a repetir se volverían redundantes, pero esto a fecha de entrega no afecta a
        la funcionalidad de la aplicación, aunque sería recomendable solucionar esto en un futuro.
         */

        for (int i = 0; i < palabra.length(); i++) {
            char caracter = palabra.charAt(i);
            int repeticiones = 0;

            for (int j = 0; j < palabra.length()-1; j++){
                if (palabra.charAt(j) == caracter) {
                    repeticiones++;
                }
            }
            caracteresRepetidos.add(new String[] {String.valueOf(caracter), String.valueOf(repeticiones)});
        }
    }

    private int devuelveRepeticionesEnPalabra(char caracter) {

        /* Este método devolvería el numero de repeticiones de un caracter concreto de la
           palabra escogida, del ArrayList creado con estas repeticiones en el constructor
           (Véase el método estableceRepeticionesEnPalabra()).
         */

        int repeticiones = 0;

        for (int i = 0; i < caracteresRepetidos.size(); i++) {
            String[] caso = caracteresRepetidos.get(i);

            if (caso[0].charAt(0) == caracter) {
                repeticiones = Integer.parseInt(caso[1]);
            }
        }

        return repeticiones;
    }

    private int frecuenciaDescubiertasChar(char caracter) {

        /*Este método contará las veces que se han descubierto un carácter concreto en la
          máscara y lo devolverá.
         */

        int frecuencia = 0;

        for (int i = 0; i < palabra.length(); i++){
            if (caracter == mascara[i]) {
                frecuencia++;
            }
        }

        return frecuencia;
    }
}