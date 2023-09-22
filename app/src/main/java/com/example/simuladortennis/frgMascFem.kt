package com.example.simuladortennis

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider

class frgMascFem : Fragment() {

    var player1 : Int = 0
    var player2 : Int = 0
    var prcPlayer1 : Int = 0
    var prcPlayer2 : Int = 0
    var nomPlayer1 : String = ""
    var nomPlayer2 : String = ""
    var categoria : Int = 0

    companion object {
    }

    private lateinit var viewModel: FrgMasculinoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_frg_masculino, container, false);
        val btnJugar : Button = view.findViewById(R.id.btnJugar)
        val spinner: Spinner = view.findViewById(R.id.spnPlayer1)
        val spinner2: Spinner = view.findViewById(R.id.spnPlayer2)
        val txtPrcPlayer1 : TextView = view.findViewById(R.id.txtProbPlayer1)
        val txtPrcPlayer2 : TextView = view.findViewById(R.id.txtProbPlayer2)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener { //Funcion para saber el item seleccionado
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                player1 = position
                nomPlayer1 = spinner.selectedItem.toString()
                calcularPorcentaje(txtPrcPlayer1, txtPrcPlayer2) //Funcion para calcular porcentaje ganador
            }
            override fun onNothingSelected(parent: AdapterView<*>) {} //Funcion obligatoria, si no hubiesemos elegido ningun elemento
        }
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                player2 = position
                nomPlayer2 = spinner2.selectedItem.toString()
                calcularPorcentaje(txtPrcPlayer1, txtPrcPlayer2)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        val bundle : Bundle? = cargarDatos();//Desempaquetamos el bundle recibido
        loadPlayers(spinner, spinner2, bundle) //rellenamos los Spinners
        btnJugar.setOnClickListener(){ //Funcion al pulsar el botón jugar
            frgMatch(view)
        }
        return view;
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FrgMasculinoViewModel::class.java)
        // TODO: Use the ViewModel
    }
    fun cargarDatos(): Bundle? { //Funcion para recoger los datos recibidos en un bundle
        val bundleAct = arguments
        return bundleAct
    }
    fun loadPlayers(spinner: Spinner, spinner2: Spinner, bundle: Bundle?) //Funcion para rellenear los Spinners según la categoría seleccionada
    {
        if (bundle != null) { //Si el bundle contiene datos, recoger el dato perteneciente a la categoria
            categoria = bundle.getInt("cat")
        }
        if (categoria==1) { //Si categoria es 1, cargaremos los jugadores Masculinos
            ArrayAdapter.createFromResource(
                requireActivity(),
                R.array.tenistas_masculinos,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter}

            ArrayAdapter.createFromResource(
                requireActivity(),
                R.array.tenistas_masculinos,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner2.adapter = adapter}
        }
        else //Si no, cargaremos los jugadores Femeninos
        {
            ArrayAdapter.createFromResource(
                    requireActivity(),
            R.array.tenistas_femeninas,
            android.R.layout.simple_spinner_item
            ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter}

            ArrayAdapter.createFromResource(
                requireActivity(),
                R.array.tenistas_femeninas,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner2.adapter = adapter}
        }
    }
    fun frgMatch (view: View) //Función del botón Jugar
    {
        if (nomPlayer1 != nomPlayer2) { //Si los jugadores seleccionados son distintos

            val bundle: Bundle = crearBundle() //Creamos u bundle
            parentFragmentManager.setFragmentResult("key", bundle) // el Key para el bundle
            val fragResultado = frgResultado() // Creamos instancia del nuevo Fragment
            fragResultado.arguments = bundle //Pasamos el bundle creado a los arguments del nuevo Fragment
            val fragmentManager: FragmentManager = parentFragmentManager //Fragment Manager, cogiendo el del Activity que aloja este Fragment
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction() //Iniciamos transacción al nuevo Frament
            fragmentTransaction.replace(R.id.fragment_Container, fragResultado) //Indicamos cuál sera el Fragment destino
            fragmentTransaction.disallowAddToBackStack() //Realizamos el cambio
            fragmentTransaction.commit()
        }
        else //Si jugadores seleccionados son iguales, mostraremos un mensaje
            Toast.makeText(activity,"Ambos jugadores no pueden ser el mismo",Toast.LENGTH_SHORT).show()
    }
    fun crearBundle() : Bundle //Función para crear el nuevo Bundle
    {
        var bundle : Bundle = Bundle()              //Creamos el bundle
        bundle.putInt("prcPlayer1", prcPlayer1)     // Añadismo datos al Bundle
        bundle.putInt("prcPlayer2", prcPlayer2)     //
        bundle.putString("nomPlayer1", nomPlayer1)  //
        bundle.putString("nomPlayer2", nomPlayer2)  //
        bundle.putInt("cat", categoria)             //
        return bundle;  //Regresamos el Bundle creado
    }
    fun calcularPorcentaje(txtPrcPlayer1 : TextView, txtPrcPlayer2 : TextView) //Función con el algoritmo responsable del cálculo de los porcentajes de cada jugador
    {
        var valorTotal : Int = ((20 - (player1+1))*100)+((20 - (player2+1))*100)
        var temp1 : Int = ((20 - (player1+1))*100)
        var temp2 : Int = ((20 - (player2+1))*100)
        prcPlayer1 = temp1*100/valorTotal
        prcPlayer2 = temp2*100/valorTotal
        txtPrcPlayer1.text = prcPlayer1.toString()+"%"
        txtPrcPlayer2.text = prcPlayer2.toString()+"%"
    }
}
