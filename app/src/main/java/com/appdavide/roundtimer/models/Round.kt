package com.appdavide.roundtimer.models

import java.io.Serializable

data class Round(
    var type : String,
    var workDur: Int,
    var restDur: Int,
    var cycles: Int,
    var dur: Int) : Serializable
