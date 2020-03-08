package it.simoa.covid_19italia

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import it.simoa.covid_19italia.data.AndamentoNazionale

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show() // Todo
        }

        // Get intent data
        val intent: Intent = intent // TODO
        val data = intent.getParcelableArrayListExtra<AndamentoNazionale>("data")


        if(data != null) {

            val lastAndamentoNazionale: AndamentoNazionale = data[12]
            ShowAndamentoNazionale(lastAndamentoNazionale)
        }




        //Toast.makeText(applicationContext, "${data.size} results", Toast.LENGTH_LONG).show()
    }

    public fun ShowAndamentoNazionale(andamento: AndamentoNazionale){
        //data.text = andamento.data
        data.text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ITALIAN).format(andamento.data)
        ricoveratiConSintomi.text = andamento.ricoverati_con_sintomi.toString()
        terapiaIntensiva.text = andamento.terapia_intensiva.toString()
        totaleOspedalizzati.text = andamento.totale_ospedalizzati.toString()
        isolamentoDomiciliare.text = andamento.isolamento_domiciliare.toString()
        totaleAttualmentePositivi.text = andamento.totale_attualmente_positivi.toString()
        nuoviAttualmentePositivi.text = andamento.nuovi_attualmente_positivi.toString()
        dimessiGuariti.text = andamento.dimessi_guariti.toString()
        deceduti.text = andamento.deceduti.toString()
        totaleCasi.text = andamento.totale_casi.toString()
        tamponi.text = andamento.tamponi.toString()

    }
}

