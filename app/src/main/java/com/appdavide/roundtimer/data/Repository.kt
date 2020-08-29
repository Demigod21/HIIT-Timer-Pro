package com.appdavide.roundtimer.data

import androidx.lifecycle.LiveData
import com.appdavide.roundtimer.data.RoundDb.RoundDb
import com.appdavide.roundtimer.data.RoundDb.RoundDbDAO
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDb
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDbAndRoundsDb
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDbDAO

class Repository(private val workoutDbDAO: WorkoutDbDAO,
                 private val roundDbDAO: RoundDbDAO)    {
    val allRounds : LiveData<List<RoundDb>> = roundDbDAO.getallRounds()
    val allWorkouts : LiveData<List<WorkoutDb>> = workoutDbDAO.getallWorkouts()
    val allWorkoutsAndRounds : List<WorkoutDbAndRoundsDb> = workoutDbDAO.getWorkoutDbAndRoundsDb()


    suspend fun insertRound(round: RoundDb){
        roundDbDAO.insert(round)
    }

    suspend fun insertWorkouts(workout: WorkoutDb){
        workoutDbDAO.insert(workout)
    }

}