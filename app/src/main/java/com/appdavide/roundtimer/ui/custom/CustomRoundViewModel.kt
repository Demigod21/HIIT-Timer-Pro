package com.appdavide.roundtimer.ui.custom

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.appdavide.roundtimer.data.Repository
import com.appdavide.roundtimer.data.RoundDb.RoundDb
import com.appdavide.roundtimer.data.RoundtimerRoomDatabase
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDb
import com.appdavide.roundtimer.models.Round
import kotlinx.coroutines.runBlocking

class CustomRoundViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: Repository

    init {
        val workoutDAO = RoundtimerRoomDatabase.getDatabase(application, viewModelScope).WorkoutDbDAO()
        val roundDAO = RoundtimerRoomDatabase.getDatabase(application, viewModelScope).RoundDbDAO()

        repository = Repository(workoutDAO, roundDAO)

    }

    fun saveWorkoutAndRounds(workout: WorkoutDb, roundsList: ArrayList<Round>){
        runBlocking {
            var workoutId = repository.insertWorkout(workout)

            roundsList.forEach{
                val roundToSave = RoundDb(it.type, it.workDur, it.restDur, it.cycles, it.dur, workoutId)
                repository.insertRound(roundToSave)
            }

        }
    }




}