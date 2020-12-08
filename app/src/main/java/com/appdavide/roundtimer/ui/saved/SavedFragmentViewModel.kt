package com.appdavide.roundtimer.ui.saved

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.appdavide.roundtimer.data.Repository
import com.appdavide.roundtimer.data.RoundDb.RoundDb
import com.appdavide.roundtimer.data.RoundtimerRoomDatabase
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDb
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDbAndRoundsDb
import com.appdavide.roundtimer.models.Round
import kotlinx.coroutines.runBlocking

class SavedFragmentViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: Repository

    val allWorkouts : LiveData<List<WorkoutDb>>

    lateinit var ListDb : LiveData<List<RoundDb>>


    init {
        val workoutDAO = RoundtimerRoomDatabase.getDatabase(application, viewModelScope).WorkoutDbDAO()
        val roundDAO = RoundtimerRoomDatabase.getDatabase(application, viewModelScope).RoundDbDAO()

        repository = Repository(workoutDAO, roundDAO)

        allWorkouts = repository.allWorkouts

    }

    fun getbyid(workoutId: Long) : LiveData<List<RoundDb>>{
        var listRound : ArrayList<Round> = ArrayList<Round>()
        runBlocking {
            ListDb = repository.getRoundsByWorkout(workoutId)
        }
        return ListDb
    }



}