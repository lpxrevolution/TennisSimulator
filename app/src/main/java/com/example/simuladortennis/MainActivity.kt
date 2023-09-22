package com.example.simuladortennis

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    var categoria : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var botonSiguiente : Button = findViewById(R.id.btnSiguiente)
    }
    fun siguienteAct (view: View)
    {
        val etNombre : EditText = findViewById(R.id.tbNombre)
        val etEdad : EditText = findViewById(R.id.tbEdad)
        if (categoria != 0 && etNombre.text.isNotEmpty() && etEdad.text.isNotEmpty()) //If para controlar que los campos Nombre, Edad y Categoría no queden vacíos
        {
            val intent = Intent(this, MatchActivity::class.java).apply { } //Creamos el Intent de la siguiente Activity
            intent.putExtra("cat", categoria) //Cargamos la categoria com Extra
            startActivity(intent) //Iniciamos Activity
        }
        else if (categoria == 0) //Si alguno de los campos está vacío
            Toast.makeText(this,"Debe seleccionar una categoria", Toast.LENGTH_SHORT).show()
        else if (etNombre.text.isEmpty())
            Toast.makeText(this,"Debe introducir un nombre", Toast.LENGTH_SHORT).show()
        else if (etNombre.text.isEmpty())
            Toast.makeText(this,"Debe introducir su edad", Toast.LENGTH_SHORT).show()
    }
    fun onRadioButtonClicked(view: View) //Funcion para indicar en "cagoria" el RadioButton seleccionado
    {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.rbMasculino ->
                    if (checked) {
                        categoria = 1
                    }
                R.id.rbFemenino ->
                    if (checked) {
                        categoria = 2
                    }
            }
        }
    }
}