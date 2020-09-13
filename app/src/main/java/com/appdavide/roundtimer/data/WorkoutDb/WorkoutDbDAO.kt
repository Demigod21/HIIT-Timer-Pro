package com.appdavide.roundtimer.data.WorkoutDb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.appdavide.roundtimer.data.RoundDb.RoundDb

@Dao
interface WorkoutDbDAO{

    @Query("SELECT * from workout")
    suspend fun getallWorkouts(): LiveData<List<WorkoutDb>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(workout: WorkoutDb)

    @Query("DELETE FROM workout")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM workout")
    suspend fun getWorkoutDbAndRoundsDb(): List<WorkoutDbAndRoundsDb>

}