package it.simoa.covid_19italia.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class AndamentoNazionale(
    val data: Date,
    val stato: String,
    val ricoverati_con_sintomi: Int,
    val terapia_intensiva: Int,
    val totale_ospedalizzati: Int,
    val isolamento_domiciliare: Int,
    val totale_positivi: Int,
    val variazione_totale_positivi: Int,
    val nuovi_positivi: Int,
    val dimessi_guariti: Int,
    val deceduti: Int,
    val totale_casi: Int,
    val tamponi: Int): Parcelable