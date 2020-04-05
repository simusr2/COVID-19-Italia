package it.simoa.covid_19italia.data

enum class RegioniProvince(val regione: String, val province: Array<String>) {
    Abruzzo(
        "Abruzzo",
        arrayOf<String>(
            "Chieti",
            "L'Aquila",
            "Pescara",
            "Teramo",
            "In fase di definizione/aggiornamento"
        )
    ),
    Basilicata("Basilicata",
        arrayOf<String>(
            "Matera",
            "Potenza",
            "In fase di definizione/aggiornamento"
        )
    ),
    Calabria("Calabria",
        arrayOf<String>(
            "Catanzaro",
            "Cosenza",
            "Crotone",
            "Reggio di Calabria",
            "Vibo Valentia",
            "In fase di definizione/aggiornamento"
        )
    ),
    Campania("Campania",
        arrayOf<String>(
            "Avellino",
            "Benevento",
            "Caserta",
            "Napoli",
            "Salerno",
            "In fase di definizione/aggiornamento"
        )
    ),
    EmiliaRomagna("Emilia-Romagna",
        arrayOf<String>(
            "Bologna",
            "Ferrara",
            "Forlì-Cesena",
            "Modena",
            "Parma",
            "Piacenza",
            "Ravenna",
            "Reggio nell'Emilia",
            "Rimini",
            "In fase di definizione/aggiornamento"
        )
    ),
    FriuliVeneziaGiulia("Friuli Venezia Giulia",
        arrayOf<String>(
            "Gorizia",
            "Pordenone",
            "Trieste",
            "Udine",
            "In fase di definizione/aggiornamento"
        )
    ),
    Lazio("Lazio",
        arrayOf<String>(
            "Frosinone",
            "Latina",
            "Rieti",
            "Roma",
            "Viterbo",
            "In fase di definizione/aggiornamento"
        )
    ),
    Liguria("Liguria",
        arrayOf<String>(
            "Genova",
            "Imperia",
            "La Spezia",
            "Savona",
            "In fase di definizione/aggiornamento"
        )
    ),
    Lombardia("Lombardia",
        arrayOf<String>(
            "Bergamo",
            "Brescia",
            "Como",
            "Cremona",
            "Lecco",
            "Lodi",
            "Mantova",
            "Milano",
            "Monza e della Brianza",
            "Pavia",
            "Sondrio",
            "Varese",
            "In fase di definizione/aggiornamento"
        )
    ),
    Marche("Marche",
        arrayOf<String>(
            "Ancona",
            "Ascoli Piceno",
            "Fermo",
            "Macerata",
            "Pesaro e Urbino",
            "In fase di definizione/aggiornamento"
        )
    ),
    Molise("Molise",
        arrayOf<String>(
            "Campobasso",
            "Isernia",
            "In fase di definizione/aggiornamento"
        )
    ),
    Piemonte("Piemonte",
        arrayOf<String>(
            "Alessandria",
            "Asti",
            "Biella",
            "Cuneo",
            "Novara",
            "Torino",
            "Verbano-Cusio-Ossola",
            "Vercelli",
            "In fase di definizione/aggiornamento"
        )
    ),
    Puglia("Puglia",
        arrayOf<String>(
            "Bari",
            "Barletta-Andria-Trani",
            "Brindisi",
            "Foggia",
            "Lecce",
            "Taranto",
            "In fase di definizione/aggiornamento"
        )
    ),
    Sardegna("Sardegna",
        arrayOf<String>(
            "Agrigento",
            "Caltanissetta",
            "Catania",
            "Enna",
            "Messina",
            "Palermo",
            "Ragusa",
            "Siracusa",
            "Trapani",
            "In fase di definizione/aggiornamento"
        )
    ),
    Toscana("Toscana",
        arrayOf<String>(
            "Arezzo",
            "Firenze",
            "Grosseto",
            "Livorno",
            "Lucca",
            "Massa Carrara",
            "Pisa",
            "Pistoia",
            "Prato",
            "Siena",
            "In fase di definizione/aggiornamento"
        )
    ),
    PATrento("P.A. Trento",
        arrayOf<String>(
            "Trento",
            "In fase di definizione/aggiornamento"
        )
    ),
    PABolzano("P.A. Bolzano",
        arrayOf<String>(
            "Bolzano",
            "In fase di definizione/aggiornamento"
        )
    ),
    Umbria("Umbria",
        arrayOf<String>(
            "Perugia",
            "Terni",
            "In fase di definizione/aggiornamento"
        )
    ),
    ValledAosta("Valle d'Aosta",
        arrayOf<String>(
            "Aosta",
            "In fase di definizione/aggiornamento"
        )
    ),
    Veneto("Veneto",
        arrayOf<String>(
            "Belluno",
            "Padova",
            "Rovigo",
            "Treviso",
            "Venezia",
            "Verona",
            "Vicenza",
            "In fase di definizione/aggiornamento"
        )
    );
    companion object {
        fun getRegioni(): Array<String>{
            val list: ArrayList<String> = ArrayList()

            values().forEach {
                list.add(it.regione)
            }

            val array: Array<String> = Array(list.size) { _ -> "" }
            list.toArray(array)
            return array
        }

        fun getProvince(regione: String): Array<String>{
            values().forEach {
                if(it.regione == regione){
                    return it.province
                }
            }

            return emptyArray()
        }
    }
}