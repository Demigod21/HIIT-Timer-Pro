package com.appdavide.roundtimer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.appdavide.roundtimer.broadcasts.TimerExpiredReceiver
import com.appdavide.roundtimer.models.Round
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

        var data = intent.getSerializableExtra("dataRounds") as List<Round>
//        total = data.size
        current = 0
        Log.d("TAG", "LOG ON CREATE")
        firstTime=true
        comeBack=false
        organizeTime(data)

        PrefUtil.setDurArray(durArray, this)
        PrefUtil.setTypeArray(typeArray, this)
        PrefUtil.setCurrentPosition(current, this)
        PrefUtil.setTotalPosition(total, this)


        btn_timer_start.setOnClickListener{
            startTimer()
//            timerState =  TimerState.Running
            updateButtons()
        }

        btn_timer_pause.setOnClickListener {
            timer.cancel()
            timerState = TimerState.Paused
            PrefUtil.setSecondsRemaining(secondsRemaining, this)
            Log.d("TAG", "LOG CLICK PAUSE")
            updateButtons()
        }

        btn_timer_stop.setOnClickListener {
            timer.cancel()
            onTimerFinished()
        }

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
            onTimerFinished()
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
        Log.d("TAG", "LOG ON INIT TIMER")

        if(firstTime)
        {
            firstTime=false
            timerLengthSeconds = durArray[0].toLong()
            secondsRemaining = durArray[0].toLong()
            updateCountdownUI()
            txt_timer_type.text = typeArray[0]
            Log.d("TAG", "LOG ON INIT TIMER PRIMA VOLTA")

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
        Log.d("TAG", "LOG ON TIMER FINISHED")


        if(current == (total-1)){
            timerState = TimerState.Stopped
            //todo qua modifico e si ritorna a capo, rimettendo tutto da zero

            secondsRemaining = 33
            timerLengthSeconds = 33
            updateCountdownUI()

        }else{
            current++
            PrefUtil.setCurrentPosition(current, this)
            updateCountdownUI()
            startTimer()
        }

    }

    private fun startTimer(){

        if(comeBack == true){
            comeBack=false
        }else{
            if(timerState == TimerState.Paused){
                secondsRemaining = PrefUtil.getSecondsRemaining(this)
            }else{
                secondsRemaining = durArray[current].toLong()
            }
        }
        Log.d("TAG", "LOG ON START TIMER")
        timerLengthSeconds = durArray[current].toLong()
        timerState = TimerState.Running
        txt_timer_type.text = typeArray[current]
        progress_bar.max = timerLengthSeconds.toInt()

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
                if(secondsRemaining < 4){
//                    val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
//                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 50)
//                    todo add music
                }
                updateCountdownUI()
            }
        }.start()
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

                fab_timer_play.isEnabled = false
                fab_timer_pause.isEnabled = true
                fab_timer_stop.isEnabled = true
            }
            TimerState.Stopped -> {
                btn_timer_start.isEnabled = true
                btn_timer_pause.isEnabled = false
                btn_timer_stop.isEnabled = false

                fab_timer_play.isEnabled = true
                fab_timer_pause.isEnabled = false
                fab_timer_stop.isEnabled = false
            }
            TimerState.Paused -> {
                btn_timer_start.isEnabled = true
                btn_timer_pause.isEnabled = false
                btn_timer_stop.isEnabled = true

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
