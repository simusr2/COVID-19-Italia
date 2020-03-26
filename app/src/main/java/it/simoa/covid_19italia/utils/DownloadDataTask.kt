package it.simoa.covid_19italia.utils

import android.os.AsyncTask
import android.os.Parcelable
import com.google.gson.GsonBuilder
import it.simoa.covid_19italia.data.CovidData
import java.lang.reflect.Type
import java.net.URL

enum class DownloadUrls(val url: String, val tipo: Type) {
    AndamentoNazionale(
        "https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-andamento-nazionale.json",
        Array<it.simoa.covid_19italia.data.AndamentoNazionale>::class.java
    ),
    AndamentoRegionale("", CovidData::class.java),
    AndamentoProvinciale("", CovidData::class.java)
}

class DownloadDataTask<T> : AsyncTask<DownloadUrls, Void, Array<T>>() where T : Parcelable {
    override fun doInBackground(vararg params: DownloadUrls?): Array<T> {
        val param: DownloadUrls = params[0] as DownloadUrls
        val result = URL(param.url).readText()


        val gsonBuilder = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val gson = gsonBuilder.create()
        val Model = gson.fromJson<Array<T>>(result,param.tipo)

/*
        val gsonBuilder = GsonBuilder().serializeNulls()
        gsonBuilder.registerTypeAdapter(WeatherObject::class.java, WeatherDeserializer())
        val gson = gsonBuilder.create()

        val weatherList: List<WeatherObject> = gson.fromJson(stringReader , Array<WeatherObject>::class.java).toList()

        return weatherList
*/

        //val weatherList: List<T> = Gson().fromJson(stringReader , ).toList()
        //return Gson().fromJson<Array<T>>(result, param.tipo)
        return Model

    }
}