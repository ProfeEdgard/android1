package com.example.ciclobikedospuntocero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val setupCiclovias=SetupCiclovias()

        var listaComunas=setupCiclovias.init().distinctBy { it.comuna }.map { it.comuna }.toMutableList()
        listaComunas.add(0, "Mostrar todo")

        spinnerComunas.adapter=ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaComunas)

        spinnerComunas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>) {

            }

            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                when(id){
                    0L->listaCiclovias.adapter= ArrayAdapter<Ciclovia>(view.context, android.R.layout.simple_list_item_1, setupCiclovias.init())
                    else->{
                        val listaCicloviasTodasComunas=setupCiclovias.init()
                        listaCiclovias.adapter= ArrayAdapter<Ciclovia>(view.context,
                            android.R.layout.simple_list_item_1,
                            listaCicloviasTodasComunas.filter { it.comuna.equals(listaCicloviasTodasComunas.get(id.toInt()-1).comuna) })
                    }
                }
            }

        }

        botonInvertir.setOnClickListener{
                view ->
            val listaDesdeListView=ArrayList<Ciclovia>()
            for(i in 0..listaCiclovias.adapter.count-1){
                listaDesdeListView.add(listaCiclovias.adapter.getItem(i) as Ciclovia)
            }
            listaDesdeListView.reverse()
            listaCiclovias.adapter=ArrayAdapter<Ciclovia>(view.context, android.R.layout.simple_list_item_1, listaDesdeListView)

        }

        botonFiltrar.setOnClickListener{
            view ->
                val listaCicl=setupCiclovias.init()
                val textoIngresado=input.text.toString().toLowerCase()
                var listaFiltrada=ArrayList<Ciclovia>()

                val listaEmpiezaCon=listaCicl.filter { it.nombre.toLowerCase().startsWith(textoIngresado)}
                listaFiltrada.addAll(listaEmpiezaCon)
                listaCicl.removeAll(listaEmpiezaCon)

                val listaIgual=listaCicl.filter { it.nombre.toLowerCase().contains(textoIngresado)}
                listaFiltrada.addAll(listaIgual)
                listaCicl.removeAll(listaIgual)

                if(textoIngresado.toIntOrNull()!=null){
                    val(listaMayorQueComuna, listaMenorQueComuna)=listaCicl.partition{it.comuna.count()>textoIngresado.toInt()}
                    listaFiltrada.addAll(listaMayorQueComuna)
                    listaCicl.removeAll(listaMayorQueComuna)

                    val(listaMayorQueNombre, listaMenorQueNombre)=listaCicl.partition{it.nombre.count()>textoIngresado.toInt()}
                    listaFiltrada.addAll(listaMayorQueNombre)
                }

                listaCiclovias.adapter= ArrayAdapter<Ciclovia>(view.context, android.R.layout.simple_list_item_1, listaFiltrada)

        }
    }
}
