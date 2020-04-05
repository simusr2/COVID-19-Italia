package it.simoa.covid_19italia.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import com.github.mikephil.charting.charts.LineChart
import com.google.firebase.analytics.FirebaseAnalytics
import it.simoa.covid_19italia.DatiPerProvinciaActivity
import it.simoa.covid_19italia.DatiPerRegioneActivity
import it.simoa.covid_19italia.MainActivity
import it.simoa.covid_19italia.R
import it.simoa.covid_19italia.data.RegioniProvince

class OpenSection{
    companion object{

        fun CheckNetwork(activity: Activity, action: (() -> Unit)){

            if(Util.isNetworkAvailable(activity)){
                action()
            }else{
                val dlgAlert: androidx.appcompat.app.AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(activity)
                dlgAlert.setMessage("Connessione ad internet non disponibile")
                dlgAlert.setTitle("Errore di rete")
                dlgAlert.setPositiveButton("Riprova") { _, _ ->
                    if(Util.isNetworkAvailable(activity)){
                        //GetAndSetDatiNazionali()
                        action()
                    }else{
                        CheckNetwork(activity, action)
                    }
                }
                dlgAlert.setNegativeButton("Chiudi") { _, _ -> activity.finish() }
                dlgAlert.setCancelable(false)
                dlgAlert.create().show()
            }
        }

        fun OpenActivityFirebase(context: Context, event: String) : FirebaseAnalytics{
            // Obtain the FirebaseAnalytics instance.
            val mFirebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)
            mFirebaseAnalytics?.logEvent(event, null)

            return mFirebaseAnalytics
        }

        fun ClickShowChart(context: Context, chart: LineChart, dataGroup: TableLayout, buttonGroup: TableLayout, showChartButton: Button) : View.OnClickListener{
            return View.OnClickListener {
                showChartButton.setOnClickListener {
                    if (chart.visibility == View.INVISIBLE) {
                        dataGroup.visibility = View.INVISIBLE
                        buttonGroup.visibility = View.INVISIBLE
                        chart.visibility = View.VISIBLE
                        showChartButton.text = context.getString(R.string.nascondiGrafico)
                    } else {
                        dataGroup.visibility = View.VISIBLE
                        buttonGroup.visibility = View.VISIBLE
                        chart.visibility = View.INVISIBLE
                        showChartButton.text = context.getString(R.string.MostraGrafico)
                    }
                }
            }
        }

        fun ClickOnNazione(context: Context) : View.OnClickListener {
            return View.OnClickListener {
                StartAndamentoNazionaleActivity(context)
            }
        }

        fun ClickOnRegione(context: Context) : View.OnClickListener {
            return View.OnClickListener {
                val window: AlertDialog.Builder = AlertDialog.Builder(context)
                window.setTitle("Seleziona una regione")
                val options: Array<String> = RegioniProvince.getRegioni();
                window.setItems(options) { _, which ->
                    StartDatiPerRegioneActivity(context, options[which])
                }

                window.show()
            }
        }

        fun ClickOnProvincia(context: Context) : View.OnClickListener {
            return View.OnClickListener{
                val window: AlertDialog.Builder = AlertDialog.Builder(context)
                window.setTitle("Seleziona una regione")
                val options = RegioniProvince.getRegioni()
                window.setItems(options) { _, which ->
                    val windowProvince: AlertDialog.Builder = AlertDialog.Builder(context)
                    windowProvince.setTitle("Seleziona una provincia")
                    val optionsProvince = RegioniProvince.getProvince(options[which])

                    windowProvince.setItems(optionsProvince) { _, whichProvincia ->
                        StartDatiPerProvinciaActivity(context, options[which], optionsProvince[whichProvincia])
                    }
                    windowProvince.show()
                }
                window.show()
            }
        }

        fun StartAndamentoNazionaleActivity(context: Context){
            // Open MainActivity
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

        fun StartDatiPerProvinciaActivity(context: Context, regione: String, provincia: String){
            // Open DatiPerProvinciaActivity
            val intent = Intent(context, DatiPerProvinciaActivity::class.java)
            intent.putExtra("regione", regione)
            intent.putExtra("provincia", provincia)
            context.startActivity(intent)
        }

        fun StartDatiPerRegioneActivity(context: Context, regione: String){
            // Open DatiPerRegioneActivity
            val intent = Intent(context, DatiPerRegioneActivity::class.java)
            intent.putExtra("regione", regione) // Temp putExtra
            context.startActivity(intent)
        }

    }
}