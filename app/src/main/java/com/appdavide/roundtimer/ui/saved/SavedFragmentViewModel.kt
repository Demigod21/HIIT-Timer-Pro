package com.appdavide.roundtimer.ui.saved

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.appdavide.roundtimer.data.Repository
import com.appdavide.roundtimer.data.RoundtimerRoomDatabase
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDbAndRoundsDb

class SavedFragmentViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: Repository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allWorkoutDbAndRoundsDb : List<WorkoutDbAndRoundsDb>

    init {

        val workoutDbDAO = RoundtimerRoomDatabase.getDatabase(application, viewModelScope).WorkoutDbDAO()
        val roundDbDAO = RoundtimerRoomDatabase.getDatabase(application, viewModelScope).RoundDbDAO()

        repository = Repository(workoutDbDAO, roundDbDAO)
        allWorkoutDbAndRoundsDb = repository.allWorkoutsAndRounds

    }



}