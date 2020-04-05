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
import it.simoa.covid_19italia.data.DatiPerProvincia
import it.simoa.covid_19italia.utils.DownloadDataTask
import it.simoa.covid_19italia.utils.DownloadUrls
import it.simoa.covid_19italia.utils.OpenSection
import kotlinx.android.synthetic.main.activity_datiperregione.*
import kotlinx.android.synthetic.main.content_datiperrprovincia.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ceil

class DatiPerProvinciaActivity : AppCompatActivity() {

    // Firebase
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datiperprovincia)
        setSupportActionBar(toolbar)

        mFirebaseAnalytics = OpenSection.OpenActivityFirebase(this, "DatiAndamentoNazionaleActivity")

        provinciaBtn.setOnClickListener(OpenSection.ClickOnProvincia(this))
        regioneBtn.setOnClickListener(OpenSection.ClickOnRegione(this))
        nazioneBtn.setOnClickListener(OpenSection.ClickOnNazione(this))
        showChartButton.setOnClickListener(OpenSection.ClickShowChart(this, chart, dataGroup, buttonGroup, showChartButton))

        OpenSection.CheckNetwork(this, this::GetAndSetDatiProvinciali)
    }

    fun GetAndSetDatiProvinciali(){
        // Get intent data
        val regione = intent.getStringExtra("regione")
        val provincia = intent.getStringExtra("provincia")

        // Start loading data
        val fullData: ArrayList<DatiPerProvincia> = DownloadDataTask<DatiPerProvincia>()
            .execute(DownloadUrls.DatiPerProvincia).get().toCollection(ArrayList())

        this.title = this.title.toString() + " " + provincia + " " + regione

        val data: ArrayList<DatiPerProvincia> = ArrayList()

        fullData.forEach{
            if(it.denominazione_regione == regione && it.denominazione_provincia == provincia){
                data.add(it);
            }
        }

        val last: DatiPerProvincia = data[data.size - 1]
        ShowData(last)

        val xValues: Array<String> = Array(data.size) { "" }
        val lineData: ArrayList<ILineDataSet> = GetLineDataSet(data, xValues);

        SetGraph(DownloadUrls.AndamentoNazionale, lineData, last.totale_casi, data.size, xValues)

        dataGroup.visibility = View.VISIBLE
        chart.visibility = View.INVISIBLE
    }

    fun GetLineDataSet(data: ArrayList<DatiPerProvincia>, xValues: Array<String>): ArrayList<ILineDataSet>{
        val dataSets:ArrayList<ILineDataSet> = ArrayList()
        val totale_casi: ArrayList<Entry> = ArrayList()

        var i = 0
        data.forEach {
            totale_casi.add(Entry(i.toFloat(), it.totale_casi.toFloat()))

            xValues[i] = SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN).format(it.data)
            i++
        }

        val set = LineDataSet(totale_casi, "Totale casi")
        set.color = Color.MAGENTA
        dataSets.add(set)

        return dataSets
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

    fun ShowData(andamento: DatiPerProvincia){
        //data.text = andamento.data

        data.text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ITALIAN).format(andamento.data)
        totaleCasi.text = andamento.totale_casi.toString()

    }
}

