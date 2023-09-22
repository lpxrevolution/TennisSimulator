package com.example.simuladortennis

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.fromHtml
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlin.random.Random

class frgResultado : Fragment() {
    private var prcPLayer1 : Int = 0    //Creamos las variables que necesitamos globales, privadas para no ser accesibles desde otros lugares
    private var prcPLayer2 : Int = 0
    private var nomPlayer1 : String = ""
    private var nomPlayer2 : String = ""
    private var categoria : Int = 0
    lateinit var txtPlayer1 : TextView  //lateinit nos permite crear las variables globales y darles un valor cuando creemos el view en "onCreateView"
    lateinit var txtPlayer2 : TextView
    lateinit var txtSet1 : TextView
    lateinit var txtSet2 : TextView
    lateinit var txtSet3 : TextView
    lateinit var txtSet4 : TextView
    lateinit var txtSet5 : TextView
    lateinit var txtGanador : TextView
    lateinit var botonReset : Button
    lateinit var botonRejugada : Button
    lateinit var swMusica : Switch
    lateinit var mp : MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* Inflate the layout for this fragment */
        recuperarDatosBundle()  //Recuperamos los datos del bundle que hemos recibido del fragment anterior
        val view = inflater.inflate(R.layout.fragment_frg_resultado, container, false) //Creamos la variable view
        cargarDatosView(view)   //Creamos los datos ayudandonos del view
        simularPartido(categoria)   //Simulamos partido enviando la categoria seleccionada por el user
        mp.start() // Ejecutamos la música de forma predeterminada
        return view;
    }

    fun cargarDatosView(view: View) //Función para asignar variables
    {
        mp = MediaPlayer.create(activity, R.raw.fanfare)
        txtSet1 = view.findViewById(R.id.txtSet1)
        txtSet2 = view.findViewById(R.id.txtSet2)
        txtSet3 = view.findViewById(R.id.txtSet3)
        txtSet4 = view.findViewById(R.id.txtSet4)
        txtSet5 = view.findViewById(R.id.txtSet5)
        txtGanador = view.findViewById(R.id.txtGanadorGrande)
        txtPlayer1 = view.findViewById(R.id.txtPlayer1)
        txtPlayer2 = view.findViewById(R.id.txtPlayer2)
        botonReset = view.findViewById(R.id.btnReset)
        botonRejugada = view.findViewById(R.id.btnRejugada)
        swMusica = view.findViewById(R.id.swMusic)
        txtPlayer1.text = nomPlayer1
        txtPlayer2.text = nomPlayer2
        botonRejugada.setOnClickListener() //Función para el botón "Rejugada"
        {
            mp.stop() //Paramos la música
            val fragmentManager: FragmentManager = parentFragmentManager //Creamos el FragmentManager, al ser un Fragment lo cogeremos del Activity al que pertenece
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction() //Transacción de Fragment
            val fragMascFem = frgMascFem() //Creammos el Fragment de destino
            var bundle = Bundle() //Creamos Bundle para pasar los datos necesarios
            bundle.putInt("cat",categoria) //Pasamos la categoria para el Fragment destino
            fragMascFem.arguments = bundle //Añadimos el Bundle al Fragment destino
            fragmentTransaction.replace(R.id.fragment_Container, fragMascFem) //Preparamos el nuevo Fragment
            fragmentTransaction.disallowAddToBackStack()    //
            fragmentTransaction.commit()                    //Realisamos el cambio de Fragment
        }
        botonReset.setOnClickListener() //Funcion para resetear la app
        {
            reset()
            mp.stop() //Paramos la música
        }
        swMusica.setOnCheckedChangeListener { _, isChecked -> //Listener para el cambio de estado del Switch
            if (isChecked) {
                mp.start() //Si esta activado inicia la música (Estado predeterminado), si hubiese sido parada, la música continuará desde su estado anterior
            }
            else        //Si cambia de estado..
                mp.pause()  //La música se detiene
        }
    }
    fun recuperarDatosBundle() //Reperar datos del Bundle recibido desde el Activity/Fragment anterior
    {
        val bundle = arguments
        prcPLayer1 = bundle!!.getInt("prcPlayer1")
        prcPLayer2 = bundle.getInt("prcPlayer2")
        nomPlayer1 = bundle.getString("nomPlayer1").toString()
        nomPlayer2 = bundle.getString("nomPlayer2").toString()
        categoria = bundle.getInt("cat")
    }
    fun simularPartido(categoria : Int) //Función para simular el partido
    {
        var puntosPlayer1 : Int = 0 //Puntos de cada jugador en cada set
        var puntosPlayer2 : Int = 0 //
        var totalSets : Int = 0   //Cantidad de sets máximos que se pueden jugar
        var setsPlayer1 : Int = 0   //Sets que lleva ganados cada jugador
        var setsPlayer2 : Int = 0   //
        var winner : Int = 0    //Indica que jugador es el ganador
        var setsWin : Int = 0   //Indica el número de sets que debe ganar un jugador para ganar el partido
        var set : Boolean = false   //Indica si se ha terminado el set actual
        var loop : Int = 1  //Indica el set que se esta jugando actualmente
        var tempNum : Int   //Variable temporal para el numero aleatorio
        when (categoria) {
            1 -> {setsWin = 3   //Si la categoria es 1 (Masculina) se deben ganar 3 para ganar y como máximo habrá 5 sets
                totalSets = 5}
            2 -> {setsWin = 2   //Si la categoria es 2 (Femenina) se deben ganar 2 para ganar y como máximo habrá 3 sets
                totalSets = 3}
        }
        var r : Random = Random //Variable tipo Random para el número aleatorio
        while (loop<=totalSets) //Empezamos el loop del partido, cada vuelta será un set
        {
            puntosPlayer1 = 0   //Iniciamos el set con puntuaciones a 0
            puntosPlayer2 = 0
            set = false //indicamos que el set actual no ha terminado
            while (!set) //Mientras no termine el set...
            {
                tempNum = r.nextInt(100-1)+1 //Creamos un número aleatorio entre 1 y 100
                if (tempNum<=prcPLayer1){   //Si el numero aletario está entre 1 y el numero de porcentaje ganador del jugador 1...
                    puntosPlayer1++ //Jugador 1 gana punto
                    if (puntosPlayer1==7) { //Si el jugador 1 tiene 7 puntos...
                        setsPlayer1++   //Gana el set
                        set=true        //Damos por termminado el set
                        winner = 1      // Damos como ganador del set al jugador 1
                    }
                    else if (puntosPlayer1==6) { //Si el jugador 1 tiene 6 puntos...
                        if (puntosPlayer2<5) {  //Si el jugador 2 tiene menos de 5 puntos..
                            setsPlayer1++   //Gana el set
                            set=true        //Damos por termminado el set
                            winner = 1      // Damos como ganador del set al jugador 1
                        }
                    }
                }
                else{
                    puntosPlayer2++     //Mismo caso anterior pero con jugador 2
                    if (puntosPlayer2==7) {
                        setsPlayer2++
                        set=true
                        winner = 2
                    }
                    else if (puntosPlayer2==6) {
                        if (puntosPlayer1<5) {
                            setsPlayer2++
                            set=true
                            winner = 2
                        }
                    }
                }
            }
            escribirSet(loop, puntosPlayer1, puntosPlayer2, winner) //Escribimos el set en el TextView (numero de set, puntos del jugador 1, puntos del jugador 2, quien es el ganador)
            if (setsPlayer1==setsWin || setsPlayer2==setsWin) //Si algun jugador ha llegado al número de seta para ganar el partido
                loop = totalSets+1 //Paramos el loop
            else
                loop++ //Si no, continuamos al siguiente set
        }
        if (winner==1)  //Si el ganador del ultimo set es el jugador 1
            txtGanador.text = txtPlayer1.text   //Marcamos como ganador del partido
        else
            txtGanador.text = txtPlayer2.text
    }
    fun escribirSet (numSet: Int, pPlayer1: Int, pPlayer2: Int, winner: Int) //Funcion para escribir acada set en su respectivo TextView
    {
            when (numSet)
            {
                1 ->{
                    if(winner == 1)
                        txtSet1.text = (fromHtml("<b>$pPlayer1</b><br>$pPlayer2",HtmlCompat.FROM_HTML_MODE_LEGACY))
                    else
                        txtSet1.text = (fromHtml("$pPlayer1<br><b>$pPlayer2</b>",HtmlCompat.FROM_HTML_MODE_LEGACY))

                }
                2 ->{
                    if(winner == 1)
                        txtSet2.text = (fromHtml("<b>$pPlayer1</b><br>$pPlayer2",HtmlCompat.FROM_HTML_MODE_LEGACY))
                    else
                        txtSet2.text = (fromHtml("$pPlayer1<br><b>$pPlayer2</b>",HtmlCompat.FROM_HTML_MODE_LEGACY))
                }
                3 ->{
                    if(winner == 1)
                        txtSet3.text = (fromHtml("<b>$pPlayer1</b><br>$pPlayer2",HtmlCompat.FROM_HTML_MODE_LEGACY))
                    else
                        txtSet3.text = (fromHtml("$pPlayer1<br><b>$pPlayer2</b>",HtmlCompat.FROM_HTML_MODE_LEGACY))

                }
                4 ->{
                    if(winner == 1)
                        txtSet4.text = (fromHtml("<b>$pPlayer1</b><br>$pPlayer2",HtmlCompat.FROM_HTML_MODE_LEGACY))
                    else
                        txtSet4.text = (fromHtml("$pPlayer1<br><b>$pPlayer2</b>",HtmlCompat.FROM_HTML_MODE_LEGACY))

                }
                5 ->{
                    if(winner == 1)
                        txtSet5.text = (fromHtml("<b>$pPlayer1</b><br>$pPlayer2",HtmlCompat.FROM_HTML_MODE_LEGACY))
                    else
                        txtSet5.text = (fromHtml("$pPlayer1<br><b>$pPlayer2</b>",HtmlCompat.FROM_HTML_MODE_LEGACY))
                }
            }
    }
    fun reset () //Funcion reset
    {
        val intent = Intent(activity, MainActivity::class.java).apply {  } //creamos llamada al MainActivity
        startActivity(intent) //Realizamos la llamada
    }
}