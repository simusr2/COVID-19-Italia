package it.simoa.covid_19italia.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AlertDialog
import java.lang.Exception
import java.lang.NullPointerException
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity


class Util {
    companion object{

        fun CheckNetwork(context: Context?, application: AppCompatActivity){
            if(context == null) throw NullPointerException("Context required")
            if(isNetworkAvailable(context)) return

            val dlgAlert: AlertDialog.Builder = AlertDialog.Builder(context)
            dlgAlert.setMessage("Connessione ad internet non disponibile")
            dlgAlert.setTitle("Errore di rete")
            dlgAlert.setPositiveButton("Riprova") { _, _ -> CheckNetwork(context, application) }
            dlgAlert.setNegativeButton("Chiudi") { _, _ -> application.finish() }
            dlgAlert.setCancelable(false)
            dlgAlert.create().show()
        }

        fun isNetworkAvailable(context: Context?): Boolean {
            if (context == null) return false
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            return true
                        }
                    }
                }
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    return true
                }
            }
            return false
        }
    }
}