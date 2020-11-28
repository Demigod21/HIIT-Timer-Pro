package com.appdavide.roundtimer.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.appdavide.roundtimer.Timer
import com.appdavide.roundtimer.util.NotificationUtil
import com.appdavide.roundtimer.util.PrefUtil
import java.util.*

class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("TAG", "LOG ON RECEIVE TIMER EXPIRED")
        var current = PrefUtil.getCurrentPosition(context)
        val total = PrefUtil.getTotalPosition(context)
        var durArray = PrefUtil.getDurArray(context)
        var typeArray = PrefUtil.getTypeArray(context)

        if(current == total - 1){
            NotificationUtil.showTimerExpired(context)
            PrefUtil.setTimerState(Timer.TimerState.Stopped, context)
            PrefUtil.setAlarmSetTime(0, context)
        }else{
            current++
            PrefUtil.setCurrentPosition(current, context)
            var type = typeArray[current].toString()
            val secondsRemaining = durArray[current].toLong()
            PrefUtil.setSecondsRemaining(secondsRemaining, context)
            val nowSeconds: Long = Calendar.getInstance().timeInMillis / 1000
            val wakeUpTime = Timer.setAlarm(context, nowSeconds, secondsRemaining)
            NotificationUtil.showTimerRunning(context, wakeUpTime, type)


        }


    }
}