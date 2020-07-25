package com.appdavide.roundtimer.data.RoundDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDb

@Entity(tableName="round",
        foreignKeys = [
        androidx.room.ForeignKey(entity = WorkoutDb::class, parentColumns = ["id"], childColumns = ["workoutId"])
])
class RoundDb(

    @ColumnInfo(name = "type") var type : String,
    @ColumnInfo(name = "workDur") var workDur: Int,
    @ColumnInfo(name = "restDur")  var restDur: Int,
    @ColumnInfo(name = "cycles") var cycles: Int,
    @ColumnInfo(name = "dur") var dur: Int,

    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    var workoutId: Int

)