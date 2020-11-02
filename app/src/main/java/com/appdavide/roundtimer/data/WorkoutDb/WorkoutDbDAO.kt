package com.appdavide.roundtimer.data.WorkoutDb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.appdavide.roundtimer.data.RoundDb.RoundDb

@Dao
interface WorkoutDbDAO{

    @Query("SELECT workout.* from workout")
    fun getallWorkouts(): LiveData<List<WorkoutDb>>

    //  We return a Long (PK type) so we have the inserted record id available
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(workout: WorkoutDb) : Long

    @Query("DELETE FROM workout")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT workout.name, round.* FROM workout left outer JOIN round ON workout.id = round.workoutId")
    fun getWorkoutDbAndRoundsDb(): LiveData<List<WorkoutDbAndRoundsDb>>

}