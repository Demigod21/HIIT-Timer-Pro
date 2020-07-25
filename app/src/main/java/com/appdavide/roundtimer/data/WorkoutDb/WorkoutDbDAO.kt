package com.appdavide.roundtimer.data.WorkoutDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appdavide.roundtimer.data.RoundDb.RoundDb

@Dao
interface WorkoutDbDAO{

    @Query("SELECT * from workout")
    fun getallRounds(): LiveData<List<WorkoutDb>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(workout: WorkoutDb)

    @Query("DELETE FROM workout")
    suspend fun deleteAll()
}