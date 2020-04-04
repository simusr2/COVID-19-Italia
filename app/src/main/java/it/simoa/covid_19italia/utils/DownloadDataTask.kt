package it.simoa.covid_19italia.utils

import android.os.AsyncTask
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Type
import java.net.URL

enum class DownloadUrls(val url: String, val tipo: Type) {
    AndamentoNazionale(
        "https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-andamento-nazionale.json",
        Array<it.simoa.covid_19italia.data.AndamentoNazionale>::class.java
    ),
    DatiPerRegione("https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-regioni.json",
        Array<it.simoa.covid_19italia.data.DatiPerRegione>::class.java
    ),
    DatiPerProvincia("https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-province.json",
        Array<it.simoa.covid_19italia.data.DatiPerProvincia>::class.java
    )
}

class DownloadDataTask<T> : AsyncTask<DownloadUrls, Void, Array<T>>() where T : Parcelable {
    override fun doInBackground(vararg params: DownloadUrls?): Array<T> {
        val param: DownloadUrls = params[0] as DownloadUrls
        val result: String = URL(param.url).readText()

        val gsonBuilder: GsonBuilder = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val gson: Gson = gsonBuilder.create()

        return gson.fromJson(result, param.tipo)
    }
}