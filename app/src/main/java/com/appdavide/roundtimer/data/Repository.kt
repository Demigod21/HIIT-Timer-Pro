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
    val allWorkoutsAndRounds : LiveData<List<WorkoutDbAndRoundsDb>> = workoutDbDAO.getWorkoutDbAndRoundsDb()


    suspend fun insertRound(round: RoundDb){
        roundDbDAO.insert(round)
    }

    suspend fun insertWorkout(workout: WorkoutDb) : Long {
        return workoutDbDAO.insert(workout)
    }

    suspend fun getRoundsByWorkout(workoutId : Long) : LiveData<List<RoundDb>>{
        return roundDbDAO.getId(workoutId)
    }


}