package com.rubenbermejo.fml.ahorcado;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class principal extends AppCompatActivity {

    //ELEMENTOS DE LA ACTIVIDAD.

    Spinner spinnerCaracteres, spinnerPosiciones;
    Button jugarL, play, finish;
    CheckBox comodin, mostrarPalabraDBG;
    RadioGroup dificultad;
    TextView puntuacion, vidas, mascara, palabraSolucion;
    MecanicaJuego nuevoJuego;
    ArrayAdapter<Character> spinAdapterCh;
    ArrayAdapter<String> spinAdapterPs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*
        Se declaran todos los elementos y los unimos a la actividad.
         */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        jugarL = findViewById(R.id.jugarL);
        play = findViewById(R.id.play);
        finish = findViewById(R.id.finish);
        mascara = findViewById(R.id.mascara);
        comodin = findViewById(R.id.help);
        dificultad = findViewById(R.id.dificultad);
        puntuacion = findViewById(R.id.scoreView);
        vidas = findViewById(R.id.lifeView);
        dificultad.check(dificultad.getChildAt(0).getId());
        palabraSolucion = findViewById(R.id.palabraSolucion);
        mostrarPalabraDBG = findViewById(R.id.mostrarPalabraDBG);
        jugarL.setEnabled(false);
        finish.setEnabled(false);
        mostrarPalabraDBG.setEnabled(false);
        spinnerCaracteres = findViewById(R.id.spinnerChars);
        spinnerPosiciones = findViewById(R.id.spinnerPos);
        spinnerCaracteres.setEnabled(false);
        spinnerPosiciones.setEnabled(false);
    }

    public void empezarJuego(View v) {

        /*
        Este método, iniciado por el botón "Empezar" en la pantalla,
        cambiará el estado de la actividad de "En espera" a "Jugando".

        Aquí se iniciará una instancia del juego, y se preparan los
        Spinners, así como el resto de partes de la interfaz.
         */

        play.setEnabled(false);
        jugarL.setEnabled(true);
        finish.setEnabled(true);
        dificultad.getChildAt(0).setEnabled(false);
        dificultad.getChildAt(1).setEnabled(false);
        dificultad.getChildAt(2).setEnabled(false);
        comodin.setEnabled(false);
        mostrarPalabraDBG.setEnabled(true);
        spinnerCaracteres.setEnabled(true);
        spinnerPosiciones.setEnabled(true);

        nuevoJuego = new MecanicaJuego(comodin.isChecked());
        nuevoJuego.setDificultad(dificultad.getCheckedRadioButtonId());

        spinAdapterCh = new ArrayAdapter<>(principal.this, android.R.layout.simple_spinner_item, nuevoJuego.getListaCaracteres());
        spinnerCaracteres.setAdapter(spinAdapterCh);

        spinAdapterPs = new ArrayAdapter<>(principal.this, android.R.layout.simple_spinner_item, nuevoJuego.getListaPosiciones());
        spinnerPosiciones.setAdapter(spinAdapterPs);

        vidas.setText(String.valueOf(nuevoJuego.getVidas()));
        puntuacion.setText(String.valueOf(nuevoJuego.getPuntuacion()));
        mascara.setText(nuevoJuego.getCurrentMascara());

        //Este evento comprobará si el checkbox de mostrar palabra está activo o no.
        //Mostrará la palabra si está activo y si no la esconderá.

        mostrarPalabraDBG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    palabraSolucion.setText(nuevoJuego.getPalabra());
                } else {
                    palabraSolucion.setText("");
                }
            }
        });
    }

    public void finalizarJuego(View v) {
        /*
        En caso de que el botón "Finalizar" se pulse, la interfaz pasará del estado
        de "Jugando" al modo de espera. Se podrá volver a iniciar un juego.
         */

        dificultad.getChildAt(0).setEnabled(true);
        dificultad.getChildAt(1).setEnabled(true);
        dificultad.getChildAt(2).setEnabled(true);
        comodin.setEnabled(true);
        play.setEnabled(true);
        finish.setEnabled(false);
        jugarL.setEnabled(false);
        mostrarPalabraDBG.setEnabled(false);
        spinnerCaracteres.setEnabled(false);
        spinnerPosiciones.setEnabled(false);


        vidas.setText(String.valueOf(0));
        puntuacion.setText(String.valueOf(0));
    }

    public void jugarLetra(View v) {
        /*
        Este método, iniciado por el botón "Jugar Letra" en la interfaz,
        tomará los valores actuales del Spinner, y se los pasará a la instancia
        del juego para que los procese, y actualizará la información en pantalla sobre
        el juego.
         */

         char caracterSel = spinnerCaracteres.getSelectedItem().toString().toUpperCase().charAt(0);
         String posSel = spinnerPosiciones.getSelectedItem().toString();

         nuevoJuego.juegaLetra(caracterSel, posSel);

         mascara.setText(nuevoJuego.getCurrentMascara());
         vidas.setText(String.valueOf(nuevoJuego.getVidas()));
         puntuacion.setText(String.valueOf(nuevoJuego.getPuntuacion()));

         spinAdapterCh = new ArrayAdapter<>(principal.this, android.R.layout.simple_spinner_item, nuevoJuego.getListaCaracteres());
         spinnerCaracteres.setAdapter(spinAdapterCh);

         spinAdapterPs = new ArrayAdapter<>(principal.this, android.R.layout.simple_spinner_item, nuevoJuego.getListaPosiciones());
         spinnerPosiciones.setAdapter(spinAdapterPs);

         /*Este switch controla el estado del juego, a respecto de si las condiciones para ganar o perder
         se cumplen, para en ese caso actualizar o no la interfaz si las condiciones se cumplen.
          */

         switch (nuevoJuego.estadoJuego()) {
             case 1:
                 derrota(v);
                 break;
             case 2:
                 victoria(v);
                 break;
         }
    }

    /* Los siguientes métodos simplemente se encargan de mostrar el Toast que indica si el jugador ha ganado
       ó no, y cambiarán el estado del juego a modo espera otra vez.
     */

    private void victoria(View v){
        Toast.makeText(this, "¡Has ganado! Puntuación: " + nuevoJuego.getPuntuacion(), Toast.LENGTH_SHORT).show();
        finalizarJuego(v);

    }

    private void derrota(View v){
        Toast.makeText(this, "¡Has perdido! Puntuación: " + nuevoJuego.getPuntuacion(), Toast.LENGTH_SHORT).show();
        finalizarJuego(v);
    }


}