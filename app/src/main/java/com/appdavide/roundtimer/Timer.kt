package com.appdavide.roundtimer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.appdavide.roundtimer.broadcasts.TimerExpiredReceiver
import com.appdavide.roundtimer.models.Round
import com.appdavide.roundtimer.ui.simple.SimpleFragment
import com.appdavide.roundtimer.util.NotificationUtil
import com.appdavide.roundtimer.util.PrefUtil
import kotlinx.android.synthetic.main.activity_timer.*
import java.util.*
import kotlin.properties.Delegates


class Timer : AppCompatActivity() {

    private lateinit var typeArray: ArrayList<String>
    private lateinit var durArray: ArrayList<Int>
    private var current: Int = 0
    private var total: Int = 0

    private var suono = SoundPool(6, AudioManager.STREAM_MUSIC, 0)
    private var soundBeep : Int = 0
    private var soundWhistle : Int = 0
    private var finishedTimer : Boolean = false

    companion object {
        fun setAlarm(context: Context, nowSeconds: Long, secondsRemaining: Long): Long{
            val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
            PrefUtil.setAlarmSetTime(nowSeconds, context)
            return wakeUpTime
        }

        fun removeAlarm(context: Context){
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtil.setAlarmSetTime(0, context)
        }

        val nowSeconds: Long
            get() = Calendar.getInstance().timeInMillis / 1000
    }

    enum class TimerState{
        Stopped, Paused, Running
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Long = 0
    private var timerState = TimerState.Stopped
    private var secondsRemaining: Long = 0
    private var firstTime by Delegates.notNull<Boolean>()
    private var comeBack by Delegates.notNull<Boolean>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        typeArray = arrayListOf()
        durArray = arrayListOf()

        timer_layout.setOnClickListener {
            if(finishedTimer != false){
                this.onBackPressed()
            }
        }



        var data = intent.getSerializableExtra("dataRounds") as List<Round>
//        total = data.size
        current = 0
        firstTime=true
        comeBack=false
        organizeTime(data)

        PrefUtil.setDurArray(durArray, this)
        PrefUtil.setTypeArray(typeArray, this)
        PrefUtil.setCurrentPosition(current, this)
        PrefUtil.setTotalPosition(total, this)

        soundBeep = suono.load(this, R.raw.tic, 1)
        soundWhistle = suono.load(this, R.raw.whistle, 1)


        fab_timer_play.setOnClickListener {
            startTimer()
            updateButtons()
        }

        fab_timer_pause.setOnClickListener {
            timer.cancel()
            timerState = TimerState.Paused
            PrefUtil.setSecondsRemaining(secondsRemaining, this)
            Log.d("TAG", "LOG CLICK PAUSE")
            updateButtons()
        }


        fab_timer_stop.setOnClickListener {
            timer.cancel()
            finishedTimer = true
            timerState = TimerState.Stopped
            finished()
        }


    }
    override fun onResume() {
        super.onResume()
        Log.d("TAG", "LOG ON RESUME")
        comeBack=true
        initTimer()

        removeAlarm(this)
        NotificationUtil.hideTimerNotification(this)
    }

    override fun onPause() {
        super.onPause()
        Log.d("TAG", "LOG ON PAUSE")

        if (timerState == TimerState.Running){
            timer.cancel()
            val wakeUpTime = setAlarm(this, nowSeconds, secondsRemaining)
            val curType = typeArray[current]
            NotificationUtil.showTimerRunning(this, wakeUpTime, curType)
        }
        else if (timerState == TimerState.Paused){
            NotificationUtil.showTimerPaused(this)
        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, this)
        PrefUtil.setSecondsRemaining(secondsRemaining, this)
        PrefUtil.setTimerState(timerState, this)
    }

    private fun initTimer(){

        if(firstTime)
        {
            firstTime=false
            timerLengthSeconds = durArray[0].toLong()
            secondsRemaining = durArray[0].toLong()
            updateCountdownUI()
            txt_timer_type.text = typeArray[0]

        }else{
            timerState = PrefUtil.getTimerState(this)
            current = PrefUtil.getCurrentPosition(this)
            setPreviousTimerLength()

            secondsRemaining = if (timerState == TimerState.Running || timerState == TimerState.Paused)
                PrefUtil.getSecondsRemaining(this)
            else
                timerLengthSeconds

            val alarmSetTime = PrefUtil.getAlarmSetTime(this) //qua setto la properti che mi serve per calcolare i secondi mancanti
            if (alarmSetTime > 0)
                secondsRemaining -= nowSeconds - alarmSetTime //quando riprende, calcola quanti secondi son mancanti

            if (secondsRemaining <= 0)
                onTimerFinished()
            else if (timerState == TimerState.Running){
                startTimer()
            }
        }

        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished(){
        suoniWhistle()

        if(current == (total-1)){
            finishedTimer = true
            timerState = TimerState.Stopped
            finished()
        }else{
            current++
            PrefUtil.setCurrentPosition(current, this)
            updateCountdownUI()
            startTimer()
        }

    }

    private fun startTimer(){

        fab_timer_pause.visibility = View.VISIBLE
        fab_timer_play.visibility = View.VISIBLE
        fab_timer_stop.visibility = View.VISIBLE

        if(comeBack == true){
            comeBack=false
        }else{
            if(timerState == TimerState.Paused){
                secondsRemaining = PrefUtil.getSecondsRemaining(this)
            }else{
                secondsRemaining = durArray[current].toLong()
            }
        }
        timerLengthSeconds = durArray[current].toLong()
        timerState = TimerState.Running
        txt_timer_type.text = typeArray[current]
        progress_bar.max = timerLengthSeconds.toInt()
        updateCountdownUI()

        when(typeArray[current]){
            "PREPARATION" -> {
                progress_bar.progressDrawable.setColorFilter(resources.getColor(R.color.bar_progress_prep), PorterDuff.Mode.SRC_IN)
                txt_countdown.setTextColor(resources.getColor(R.color.bar_progress_prep))
                txt_timer_type.setTextColor(resources.getColor(R.color.bar_progress_prep))
                fab_timer_play.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.bar_progress_prep)))
                fab_timer_pause.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.bar_progress_prep)))
                fab_timer_stop.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.bar_progress_prep)))

            }
            "WORK" -> {
                progress_bar.progressDrawable.setColorFilter(resources.getColor(R.color.bar_progress_work), PorterDuff.Mode.SRC_IN)
                txt_countdown.setTextColor(resources.getColor(R.color.bar_progress_work))
                txt_timer_type.setTextColor(resources.getColor(R.color.bar_progress_work))
                fab_timer_play.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.bar_progress_work)))
                fab_timer_pause.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.bar_progress_work)))
                fab_timer_stop.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.bar_progress_work)))

            }
            "REST" -> {
                progress_bar.progressDrawable.setColorFilter(resources.getColor(R.color.bar_progress_rest), PorterDuff.Mode.SRC_IN)
                txt_countdown.setTextColor(resources.getColor(R.color.bar_progress_rest))
                txt_timer_type.setTextColor(resources.getColor(R.color.bar_progress_rest))
                fab_timer_play.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.bar_progress_rest)))
                fab_timer_pause.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.bar_progress_rest)))
                fab_timer_stop.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.bar_progress_rest)))

            }
            "COOLDOWN" -> {
                progress_bar.progressDrawable.setColorFilter(resources.getColor(R.color.bar_progress_cooldown), PorterDuff.Mode.SRC_IN)
                txt_countdown.setTextColor(resources.getColor(R.color.bar_progress_cooldown))
                txt_timer_type.setTextColor(resources.getColor(R.color.bar_progress_cooldown))
                fab_timer_play.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.bar_progress_cooldown)))
                fab_timer_pause.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.bar_progress_cooldown)))
                fab_timer_stop.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.bar_progress_cooldown)))
            }
        }

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                if(secondsRemaining in 0..3){
                        suoniBeep()
                }
                updateCountdownUI()
            }
        }.start()
    }


    private fun suoniWhistle(){
        suono.play(soundWhistle, 1F, 1F, 1, 0, 1F)

    }
    private fun suoniBeep(){
        suono.play(soundBeep, 1F, 1F, 1, 0, 1F)
    }

    private fun setNewTimerLength(){
        val lengthInMinutes = PrefUtil.getTimerLength(this)
        timerLengthSeconds = (lengthInMinutes * 60L)
        progress_bar.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength(){
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this)
        progress_bar.max = timerLengthSeconds.toInt()
    }

    private fun finished(){

        intent.removeExtra("dataRounds")
        typeArray.clear()
        durArray.clear()

        fab_timer_pause.visibility = View.INVISIBLE
        fab_timer_play.visibility = View.INVISIBLE
        fab_timer_stop.visibility = View.INVISIBLE



        txt_timer_type.text = "FINISHED"
        txt_countdown.text = "0:00"
        progress_bar.max = 10
        progress_bar.progress = 10



        progress_bar.progressDrawable.setColorFilter(resources.getColor(R.color.bar_progress_finished), PorterDuff.Mode.SRC_IN)
        txt_countdown.setTextColor(resources.getColor(R.color.bar_progress_finished))
        txt_timer_type.setTextColor(resources.getColor(R.color.bar_progress_finished))
        fab_timer_play.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.bar_progress_finished)))
        fab_timer_pause.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.bar_progress_finished)))
        fab_timer_stop.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.bar_progress_finished)))
    }

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
                fab_timer_play.isEnabled = false
                fab_timer_pause.isEnabled = true
                fab_timer_stop.isEnabled = true
            }
            TimerState.Stopped -> {
                fab_timer_play.isEnabled = true
                fab_timer_pause.isEnabled = false
                fab_timer_stop.isEnabled = false
            }
            TimerState.Paused -> {
                fab_timer_play.isEnabled = true
                fab_timer_pause.isEnabled = false
                fab_timer_stop.isEnabled = true
            }
        }
    }


    fun organizeTime(data: List<Round>){
        for (round: Round in data){
            var type = round.type
            when(type){
                "PREPARATION" -> organizePreparation(round)
                "WORK ROUND" -> organizeWorkRound(round)
                "REST ROUND" -> organizeRestRound(round)
                "COOLDOWN" -> organizeCooldown(round)
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
