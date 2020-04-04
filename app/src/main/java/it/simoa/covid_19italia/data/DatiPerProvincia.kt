package it.simoa.covid_19italia.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
class DatiPerProvincia(
    val data: Date,
    val stato: String,
    val codice_regione: String,
    val denominazione_regione: String,
    val codice_provincia: String,
    val denominazione_provincia: String,
    val sigla_provincia: String,
    val lat: String,
    val lon: String,
    val totale_casi: Int,
    val note_it: String,
    val note_en: String): Parcelable