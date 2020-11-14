package com.appdavide.roundtimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.appdavide.roundtimer.models.Round
import kotlinx.android.synthetic.main.activity_timer.*

class Timer : AppCompatActivity() {

    private lateinit var typeArray: ArrayList<String>
    private lateinit var durArray: ArrayList<Int>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        var data = intent.getSerializableExtra("dataRounds") as List<Round>
        organizeTime(data)


    }

    fun organizeTime(data: List<Round>){

        for (round: Round in data){
            var type = round.type;
            when(type){
                "Preparation" -> organizePreparation(round)
                "Work round" -> organizeWorkRound(round)
                "Rest round" -> organizeRestRound(round)
                "Cooldown" -> organizeCooldown(round)
            }
        }

    }

    fun organizePreparation(prep: Round){
        typeArray.add(prep.type)
        durArray.add(prep.dur)
    }

    fun organizeWorkRound(work: Round){
        val workDur = work.workDur
        val restDur = work.restDur
        val nrc = work.cycles * 2 - 1
        for (i in 1..nrc){
            if (i%2 == 1){
                typeArray.add("WORK")
                durArray.add(workDur)
            }else{
                typeArray.add("REST")
                durArray.add(restDur)
            }
        }

    }

    fun organizeRestRound(rest: Round){
        typeArray.add(rest.type)
        durArray.add(rest.dur)
    }

    fun organizeCooldown(cool: Round){
        typeArray.add(cool.type)
        durArray.add(cool.dur)
    }

}
