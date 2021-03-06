package it.simoa.covid_19italia

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.analytics.FirebaseAnalytics
import it.simoa.covid_19italia.data.DatiPerRegione
import it.simoa.covid_19italia.utils.DownloadDataTask
import it.simoa.covid_19italia.utils.DownloadUrls
import it.simoa.covid_19italia.utils.OpenSection
import kotlinx.android.synthetic.main.activity_datiperregione.*
import kotlinx.android.synthetic.main.content_datiperregione.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ceil


class DatiPerRegioneActivity : AppCompatActivity() {

    // Firebase
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datiperregione)
        setSupportActionBar(toolbar)

        mFirebaseAnalytics = OpenSection.OpenActivityFirebase(this, "DatiAndamentoNazionaleActivity")

        provinciaBtn.setOnClickListener(OpenSection.ClickOnProvincia(this))
        regioneBtn.setOnClickListener(OpenSection.ClickOnRegione(this))
        nazioneBtn.setOnClickListener(OpenSection.ClickOnNazione(this))
        showChartButton.setOnClickListener(OpenSection.ClickShowChart(this, chart, dataGroup, buttonGroup, showChartButton))

        OpenSection.CheckNetwork(this, this::GetAndSetDatiRegionali)
    }

    fun GetAndSetDatiRegionali(){
        // Get intent data
        val regione = intent.getStringExtra("regione")

        // Start loading data
        val fullData: ArrayList<DatiPerRegione> = DownloadDataTask<DatiPerRegione>()
            .execute(DownloadUrls.DatiPerRegione).get().toCollection(ArrayList())

        this.title = this.title.toString() + " " + regione

        val data: ArrayList<DatiPerRegione> = ArrayList()

        fullData.forEach{
            if(it.denominazione_regione == regione){
                data.add(it);
            }
        }

        val last: DatiPerRegione = data[data.size - 1]
        ShowData(last)

        val xValues: Array<String> = Array(data.size) { "" }
        val lineData: ArrayList<ILineDataSet> = GetLineDataSet(data, xValues);

        SetGraph(DownloadUrls.AndamentoNazionale, lineData, last.totale_casi, data.size, xValues)

        dataGroup.visibility = View.VISIBLE
        chart.visibility = View.INVISIBLE

    }

    fun GetLineDataSet(data: ArrayList<DatiPerRegione>, xValues: Array<String>): ArrayList<ILineDataSet>{
        val dataSets:ArrayList<ILineDataSet> = ArrayList()
        val ricoverati_con_sintomi: ArrayList<Entry> = ArrayList()
        val terapia_intensiva: ArrayList<Entry> = ArrayList()
        val totale_ospedalizzati: ArrayList<Entry> = ArrayList()
        val isolamento_domiciliare: ArrayList<Entry> = ArrayList()
        val totale_positivi: ArrayList<Entry> = ArrayList()
        val variazione_totale_positivi: ArrayList<Entry> = ArrayList()
        val nuovi_positivi: ArrayList<Entry> = ArrayList()
        val dimessi_guariti: ArrayList<Entry> = ArrayList()
        val deceduti: ArrayList<Entry> = ArrayList()
        val totale_casi: ArrayList<Entry> = ArrayList()
        val tamponi: ArrayList<Entry> = ArrayList()

        var i = 0
        data.forEach {
            ricoverati_con_sintomi.add(Entry(i.toFloat(), it.ricoverati_con_sintomi.toFloat()))
            terapia_intensiva.add(Entry(i.toFloat(), it.terapia_intensiva.toFloat()))
            totale_ospedalizzati.add(Entry(i.toFloat(), it.totale_ospedalizzati.toFloat()))
            isolamento_domiciliare.add(Entry(i.toFloat(), it.isolamento_domiciliare.toFloat()))
            totale_positivi.add(Entry(i.toFloat(), it.totale_positivi.toFloat()))
            variazione_totale_positivi.add(Entry(i.toFloat(), it.variazione_totale_positivi.toFloat()))
            nuovi_positivi.add(Entry(i.toFloat(), it.nuovi_positivi.toFloat()))
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

        set = LineDataSet(isolamento_domiciliare, "Totale positivi")
        set.color = Color.rgb(255,117,20);
        dataSets.add(set)

        set = LineDataSet(variazione_totale_positivi, "Variazione totale positivi")
        set.color = Color.GRAY
        dataSets.add(set)

        set = LineDataSet(nuovi_positivi, "Nuovi positivi")
        set.color = Color.rgb(204, 0, 255)
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
        return dataSets;
    }

    fun SetGraph(type: DownloadUrls, data: ArrayList<ILineDataSet>, maxValue: Int, numberOfValues: Int, xValues: Array<String>){
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

        xAxis.axisMaximum = (numberOfValues - 1).toFloat()

        val formatter: ValueFormatter =
            object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase): String {

                    return if(value.toInt() > xValues.size - 1){
                        ""
                    }else{
                        xValues[value.toInt()]
                    }


                }
            }

        xAxis.valueFormatter = formatter

        chart.data = LineData(data)

        chart.setDrawMarkers(true)

        // get the legend (only possible after setting data)
        val l = chart.legend

        // draw legend entries as lines
        l.form = Legend.LegendForm.DEFAULT
        l.isWordWrapEnabled = true
    }

    fun ShowData(andamento: DatiPerRegione){
        //data.text = andamento.data

        data.text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ITALIAN).format(andamento.data)
        ricoveratiConSintomi.text = andamento.ricoverati_con_sintomi.toString()
        terapiaIntensiva.text = andamento.terapia_intensiva.toString()
        totaleOspedalizzati.text = andamento.totale_ospedalizzati.toString()
        isolamentoDomiciliare.text = andamento.isolamento_domiciliare.toString()
        totalePositivi.text = andamento.totale_positivi.toString()
        variazioneTotalePositivi.text = andamento.variazione_totale_positivi.toString()
        nuoviPositivi.text = andamento.nuovi_positivi.toString()
        dimessiGuariti.text = andamento.dimessi_guariti.toString()
        deceduti.text = andamento.deceduti.toString()
        totaleCasi.text = andamento.totale_casi.toString()
        tamponi.text = andamento.tamponi.toString()

    }
}

