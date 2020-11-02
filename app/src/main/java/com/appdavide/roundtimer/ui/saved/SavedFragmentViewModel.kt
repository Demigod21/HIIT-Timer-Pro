package com.appdavide.roundtimer.ui.saved

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.appdavide.roundtimer.data.Repository
import com.appdavide.roundtimer.data.RoundtimerRoomDatabase
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDb
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDbAndRoundsDb

class SavedFragmentViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: Repository

    val allWorkouts : LiveData<List<WorkoutDb>>

    init {
        val workoutDAO = RoundtimerRoomDatabase.getDatabase(application, viewModelScope).WorkoutDbDAO()
        val roundDAO = RoundtimerRoomDatabase.getDatabase(application, viewModelScope).RoundDbDAO()

        repository = Repository(workoutDAO, roundDAO)

        allWorkouts = repository.allWorkouts

    }



}