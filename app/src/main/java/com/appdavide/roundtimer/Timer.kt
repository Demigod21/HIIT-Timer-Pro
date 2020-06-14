package com.appdavide.roundtimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_timer.*

class Timer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        val testo = intent.getStringExtra("test")
        val test1 = intent.getStringArrayExtra("test2")

        txt_prova.text = testo
        txt_prova_2.setText(test1.get(1))


    }
}
