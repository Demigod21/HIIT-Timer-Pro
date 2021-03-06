package com.appdavide.roundtimer.repository

import com.appdavide.roundtimer.models.Round

class DataSource {

    companion object{

        var list2 = ArrayList<Round>()

        fun createDataSet(): ArrayList<Round>{
            val list = ArrayList<Round>()
            list.add(
                Round(
                    "prep",
                    0,
                    0,
                    0,
                    120
                )
            )
            list.add(
                Round(
                    "workround",
                    10,
                    10,
                    1,
                    20
                )
            )

            list.add(
                Round(
                    "COOLDOWN",
                    0,
                    0,
                    0,
                    120
                )
            )

            return list
        }
    }
}