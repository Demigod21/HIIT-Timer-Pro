package com.appdavide.roundtimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.appdavide.roundtimer.models.Round
import kotlinx.android.synthetic.main.activity_timer.*

class Timer : AppCompatActivity() {

    private lateinit var typeArray: ArrayList<String>
    private lateinit var durArray: ArrayList<Int>
    private var current: Int = 0
    private var total: Int = 0


    enum class TimerState{
        Stopped, Paused, Running
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Long = 0
    private var timerState = TimerState.Stopped

    private var secondsRemaining: Long = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        typeArray = arrayListOf()
        durArray = arrayListOf()



        var data = intent.getSerializableExtra("dataRounds") as List<Round>
//        total = data.size
        current = 0
        organizeTime(data)

        btn_timer_start.setOnClickListener{
            startTimer()
            timerState =  TimerState.Running
            updateButtons()
        }

        btn_timer_pause.setOnClickListener {
            timer.cancel()
            timerState = TimerState.Paused
            updateButtons()
        }

        btn_timer_stop.setOnClickListener {
            timer.cancel()
            onTimerFinished()
        }

    }
/*
    override fun onResume() {
        super.onResume()

        initTimer()

        //TODO: remove background timer, hide notification
    }

    override fun onPause() {
        super.onPause()

        if (timerState == TimerState.Running){
            timer.cancel()
            //TODO: start background timer and show notification
        }
        else if (timerState == TimerState.Paused){
            //TODO: show notification
        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, this)
        PrefUtil.setSecondsRemaining(secondsRemaining, this)
        PrefUtil.setTimerState(timerState, this)
    }*/

/*    private fun initTimer(){
//        timerState = PrefUtil.getTimerState(this)

        //we don't want to change the length of the timer which is already running
        //if the length was changed in settings while it was backgrounded
        if (timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

*//*
        secondsRemaining = if (timerState == TimerState.Running || timerState == TimerState.Paused)
            PrefUtil.getSecondsRemaining(this)
        else
            timerLengthSeconds
*//*

        //TODO: change secondsRemaining according to where the background timer stopped

        //resume where we left off
        if (timerState == TimerState.Running)
            startTimer()

        updateButtons()
        updateCountdownUI()
    }*/

    private fun onTimerFinished(){

        if(current == (total-1)){
            timerState = TimerState.Stopped
            //todo qua modifico e si ritorna a capo, rimettendo tutto da zero

            secondsRemaining = 33
            updateCountdownUI()



            //set the length of the timer to be the one set in SettingsActivity
            //if the length was changed when the timer was running
/*            setNewTimerLength()

            progress_bar.progress = 0

            PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
            secondsRemaining = timerLengthSeconds

            updateButtons()
            updateCountdownUI()*/
        }else{
            current++
            updateCountdownUI()
            startTimer()
            //todo qua devo metter cosa succede nello svolgimento normale
        }

    }

    private fun startTimer(){
        timerState = TimerState.Running
        secondsRemaining = durArray[current].toLong()
        timerLengthSeconds = durArray[current].toLong()
        txt_timer_type.text = typeArray[current]
        progress_bar.max = timerLengthSeconds.toInt()

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

/*    private fun setNewTimerLength(){
        val lengthInMinutes = PrefUtil.getTimerLength(this)
        timerLengthSeconds = (lengthInMinutes * 60L)
        progress_bar.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength(){
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this)
        progress_bar.max = timerLengthSeconds.toInt()
    }*/

    private fun updateCountdownUI(){
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        txt_countdown.text = "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
        progress_bar.progress = (timerLengthSeconds - secondsRemaining).toInt()
    }




    private fun updateButtons(){
        when (timerState) {
            TimerState.Running ->{
                btn_timer_start.isEnabled = false
                btn_timer_pause.isEnabled = true
                btn_timer_stop.isEnabled = true
            }
            TimerState.Stopped -> {
                btn_timer_start.isEnabled = true
                btn_timer_pause.isEnabled = false
                btn_timer_stop.isEnabled = false
            }
            TimerState.Paused -> {
                btn_timer_start.isEnabled = true
                btn_timer_pause.isEnabled = false
                btn_timer_stop.isEnabled = true
            }
        }
    }


    fun organizeTime(data: List<Round>){


        for (round: Round in data){
            var type = round.type
            when(type){
                "Preparation" -> organizePreparation(round)
                "Work Round" -> organizeWorkRound(round)
                "Rest Round" -> organizeRestRound(round)
                "Cooldown" -> organizeCooldown(round)
            }
        }
        total = durArray.size

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
