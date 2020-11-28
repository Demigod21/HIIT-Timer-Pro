package com.appdavide.roundtimer.util

import android.content.Context
import android.preference.PreferenceManager
import com.appdavide.roundtimer.Timer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class PrefUtil {
    companion object {

        fun getTimerLength(context: Context): Int{
            //placeholder
            return 1
        }

        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "com.resocoder.timer.previous_timer_length_seconds"

        fun getPreviousTimerLengthSeconds(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }

        fun setPreviousTimerLengthSeconds(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }


        private const val TIMER_STATE_ID = "com.resocoder.timer.timer_state"

        fun getTimerState(context: Context): Timer.TimerState{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
            return Timer.TimerState.values()[ordinal]
        }

        fun setTimerState(state: Timer.TimerState, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }


        private const val SECONDS_REMAINING_ID = "com.resocoder.timer.seconds_remaining"

        fun getSecondsRemaining(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }

        fun setSecondsRemaining(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }


        private const val ALARM_SET_TIME_ID = "com.resocoder.timer.backgrounded_time"

        fun getAlarmSetTime(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return  preferences.getLong(ALARM_SET_TIME_ID, 0)
        }

        fun setAlarmSetTime(time: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME_ID, time)
            editor.apply()
        }

        //la variabile corrente del timer
        private const val CURRENT_POSITION = "com.resocoder.timer.current"

        fun getCurrentPosition(context: Context): Int{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return  preferences.getInt(CURRENT_POSITION, 0)
        }

        fun setCurrentPosition(current: Int, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putInt(CURRENT_POSITION, current)
            editor.apply()
        }

        //la variabile totale del timer
        private const val TOTAL_POS = "com.resocoder.timer.total"

        fun getTotalPosition(context: Context): Int{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return  preferences.getInt(TOTAL_POS, 0)
        }

        fun setTotalPosition(current: Int, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putInt(TOTAL_POS, current)
            editor.apply()
        }

        //array durate timer
        private const val DUR_ARRAY = "com.resocoder.timer.durarray"

        fun getDurArray(context: Context): ArrayList<Int>{
            val shared = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val gson = Gson()
            val json = shared.getString(DUR_ARRAY, "")
            val type: Type =
                object : TypeToken<List<Int?>?>() {}.type
            val arrayList: List<Int> = gson.fromJson(json, type)
            val retArray = ArrayList<Int>()
            retArray.addAll(arrayList)
            return retArray
        }

        fun setDurArray(current: ArrayList<Int>, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val gson = Gson()
            val json = gson.toJson(current)
            editor.putString(DUR_ARRAY, json)
            editor.apply()
        }


        //array TIPI timer
        private const val TYPE_ARRAY = "com.resocoder.timer.typearray"
        fun getTypeArray(context: Context): ArrayList<String>{
            val shared = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val gson = Gson()
            val json = shared.getString(TYPE_ARRAY, "")
            val type: Type =
                object : TypeToken<List<String?>?>() {}.type
            val arrayList: List<String> = gson.fromJson(json, type)
            val retArray = ArrayList<String>()
            retArray.addAll(arrayList)
            return retArray
        }

        fun setTypeArray(current: ArrayList<String>, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val gson = Gson()
            val json = gson.toJson(current)
            editor.putString(TYPE_ARRAY, json)
            editor.apply()
        }


    }
}