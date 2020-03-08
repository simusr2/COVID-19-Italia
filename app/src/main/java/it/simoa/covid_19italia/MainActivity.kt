package it.simoa.covid_19italia

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.snackbar.Snackbar
import it.simoa.covid_19italia.data.AndamentoNazionale
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil


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

            val lastAndamentoNazionale: AndamentoNazionale = data[data.size - 1]
            ShowAndamentoNazionale(lastAndamentoNazionale)

            val maxValue: Int = lastAndamentoNazionale.totale_casi

            // background color
            chart.setBackgroundColor(Color.TRANSPARENT)

            // disable description text
            chart.description.isEnabled = false;

            // enable touch gestures
            chart.setTouchEnabled(true)

            // enable scaling and dragging
            chart.isDragEnabled = true
            chart.setScaleEnabled(true)
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true);

            // set listeners
            //chart.setOnChartValueSelectedListener(this);
            chart.setDrawGridBackground(false);
            chart.axisRight.isEnabled = false

// // X-Axis Style // //
            val xAxis: XAxis = chart.xAxis

            xAxis.position = XAxis.XAxisPosition.BOTTOM
            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f)


            xAxis.axisMinimum = 0f

            xAxis.granularity = 1f; // minimum axis-step (interval) is 1

            xAxis.labelRotationAngle = 45f



// // Y-Axis Style // //
            val yAxis: YAxis = chart.axisLeft
                // disable dual axis (only use LEFT axis)

                // horizontal grid lines
                yAxis.enableGridDashedLine(10f, 10f, 0f)
                // axis range


                yAxis.axisMaximum = (ceil(maxValue / 1000.0) * 1000).toFloat()
                yAxis.axisMinimum = 0f



            val dataSets:ArrayList<ILineDataSet> = ArrayList()
            val ricoverati_con_sintomi: ArrayList<Entry> = ArrayList()
            val terapia_intensiva: ArrayList<Entry> = ArrayList()
            val totale_ospedalizzati: ArrayList<Entry> = ArrayList()
            val isolamento_domiciliare: ArrayList<Entry> = ArrayList()
            val totale_attualmente_positivi: ArrayList<Entry> = ArrayList()
            val nuovi_attualmente_positivi: ArrayList<Entry> = ArrayList()
            val dimessi_guariti: ArrayList<Entry> = ArrayList()
            val deceduti: ArrayList<Entry> = ArrayList()
            val totale_casi: ArrayList<Entry> = ArrayList()
            val tamponi: ArrayList<Entry> = ArrayList()

            val xValues: Array<String> = Array(data.size) {
                _ -> ""
            }


            var i = 0
            data.forEach {
                ricoverati_con_sintomi.add(Entry(i.toFloat(), it.ricoverati_con_sintomi.toFloat()))
                terapia_intensiva.add(Entry(i.toFloat(), it.terapia_intensiva.toFloat()))
                totale_ospedalizzati.add(Entry(i.toFloat(), it.totale_ospedalizzati.toFloat()))
                isolamento_domiciliare.add(Entry(i.toFloat(), it.isolamento_domiciliare.toFloat()))
                totale_attualmente_positivi.add(Entry(i.toFloat(), it.totale_attualmente_positivi.toFloat()))
                nuovi_attualmente_positivi.add(Entry(i.toFloat(), it.nuovi_attualmente_positivi.toFloat()))
                dimessi_guariti.add(Entry(i.toFloat(), it.dimessi_guariti.toFloat()))
                deceduti.add(Entry(i.toFloat(), it.deceduti.toFloat()))
                totale_casi.add(Entry(i.toFloat(), it.totale_casi.toFloat()))
                tamponi.add(Entry(i.toFloat(), it.tamponi.toFloat()))

                xValues[i] = SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN).format(it.data)
                i++
            }

            var set = LineDataSet(ricoverati_con_sintomi, "Ricoverati con sintomi")
            set.color = Color.BLACK
            dataSets.add(set)

            set = LineDataSet(terapia_intensiva, "Terapia intensiva")
            set.color = Color.BLUE
            dataSets.add(set)

            set = LineDataSet(totale_ospedalizzati, "Totale ospedalizzati")
            set.color = Color.CYAN
            dataSets.add(set)

            set = LineDataSet(isolamento_domiciliare, "Isolamento domiciliare")
            set.color = Color.RED
            dataSets.add(set)

            set = LineDataSet(nuovi_attualmente_positivi, "Nuovi attualmente positivi")
            set.color = Color.GRAY
            dataSets.add(set)

            set = LineDataSet(dimessi_guariti, "Dimessi guariti")
            set.color = Color.GREEN
            dataSets.add(set)

            set = LineDataSet(deceduti, "Deceduti")
            set.color = Color.YELLOW
            dataSets.add(set)

            set = LineDataSet(totale_casi, "Totale casi")
            set.color = Color.MAGENTA
            dataSets.add(set)

            /*set = LineDataSet(tamponi, "Tamponi")
            set.color = Color.DKGRAY
            dataSets.add(set)*/

            xAxis.axisMaximum = i.toFloat()

            val formatter: ValueFormatter =
                object : ValueFormatter() {
                    override fun getAxisLabel(value: Float, axis: AxisBase): String {

                        return if(value.toInt() > xValues.size - 1){
                            xValues[xValues.size - 1]
                        }else{
                            xValues[value.toInt()]
                        }


                    }
                }
            xAxis.setValueFormatter(formatter)

            chart.data = LineData(dataSets)

            chart.setDrawMarkers(true)

            // get the legend (only possible after setting data)
            // get the legend (only possible after setting data)
            val l = chart.legend

            // draw legend entries as lines
            // draw legend entries as lines
            l.form = LegendForm.DEFAULT
            l.isWordWrapEnabled = true

            dataGroup.visibility = View.VISIBLE
            chart.visibility = View.INVISIBLE

            showChartButton.setOnClickListener {
                if(chart.visibility == View.INVISIBLE){
                    dataGroup.visibility = View.INVISIBLE
                    chart.visibility = View.VISIBLE
                    showChartButton.text = "Nascondi grafico"
                }else{
                    dataGroup.visibility = View.VISIBLE
                    chart.visibility = View.INVISIBLE
                    showChartButton.text = "Mostra grafico"
                }
            }

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

