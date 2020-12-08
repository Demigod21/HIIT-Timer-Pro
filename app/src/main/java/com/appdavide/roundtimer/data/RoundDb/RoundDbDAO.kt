package com.appdavide.roundtimer.data.RoundDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoundDbDAO{

    @Query("SELECT * from round")
    fun getallRounds(): LiveData<List<RoundDb>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(round: RoundDb)

    @Query("DELETE FROM round")
    suspend fun deleteAll()

    @Query("SELECT * FROM round WHERE round.workoutId = :argOne ORDER BY round.id")
    fun getId(argOne: Long) : LiveData<List<RoundDb>>

}