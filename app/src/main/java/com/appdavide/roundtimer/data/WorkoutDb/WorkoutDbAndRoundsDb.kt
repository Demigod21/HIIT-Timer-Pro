package com.appdavide.roundtimer.data.WorkoutDb

import androidx.room.Embedded
import androidx.room.Relation
import com.appdavide.roundtimer.data.RoundDb.RoundDb

data class WorkoutDbAndRoundsDb(
    @Embedded
    val workoutDb: WorkoutDb,

    @Relation(parentColumn = "id", entityColumn = "workoutId")
    val workoutRounds: List<RoundDb>
)