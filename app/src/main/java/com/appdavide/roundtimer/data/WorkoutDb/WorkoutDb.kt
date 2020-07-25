package com.appdavide.roundtimer.data.WorkoutDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="workout")
class WorkoutDb(

    @ColumnInfo(name = "name") var name : String,

    @PrimaryKey(autoGenerate = true) val id: Int = 0

    )