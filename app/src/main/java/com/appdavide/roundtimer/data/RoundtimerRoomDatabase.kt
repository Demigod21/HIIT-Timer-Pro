package com.appdavide.roundtimer.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.appdavide.roundtimer.data.RoundDb.RoundDb
import com.appdavide.roundtimer.data.RoundDb.RoundDbDAO
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDb
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDbDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [WorkoutDb::class, RoundDb::class],
    version = 1,
    exportSchema = false
)
abstract class RoundtimerRoomDatabase : RoomDatabase(){

    val dbName = "shdrll.db"

    abstract fun WorkoutDbDAO() : WorkoutDbDAO
    abstract fun RoundDbDAO() : RoundDbDAO


    private class RoundtimerRoomDatabaseCallback(private val scope: CoroutineScope):
            RoomDatabase.Callback(){

        override fun onCreate(db : SupportSQLiteDatabase){
            super.onCreate(db)
            INSTANCE
            INSTANCE?.let{ database ->
                scope.launch {
                    //  TODO    populateInitialDatabase
                    populateTestingDatabase(database.WorkoutDbDAO(),
                        database.RoundDbDAO())
                }
            }
        }


        suspend fun populateTestingDatabase(workoutdbDAO: WorkoutDbDAO,
                                            rounddbDAO: RoundDbDAO){
            //WORKOUT
            workoutdbDAO.deleteAll()
            var workout1 = WorkoutDb("NomeTest1Workout")
            workoutdbDAO.insert(workout1)
            var workout2 = WorkoutDb("NomeTest2Workout")
            workoutdbDAO.insert(workout2)


            //ROUND
            rounddbDAO.deleteAll()
            var round1 = RoundDb("prep", 0, 80, 0, 80, workoutId = 0)
            rounddbDAO.insert(round1)
            var round2 = RoundDb("cooldown", 0, 70, 0, 70, workoutId = 0)
            rounddbDAO.insert(round2)
            var round3 = RoundDb("workvero1010", 10, 10, 15, 250, workoutId = 1)
            rounddbDAO.insert(round3)
        }


    }

    companion object{
        @Volatile
        private var INSTANCE: RoundtimerRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): RoundtimerRoomDatabase {
            val tempInstance = INSTANCE
            if( tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoundtimerRoomDatabase::class.java,
                    "shdrll.db"
                ).addCallback(RoundtimerRoomDatabaseCallback(scope)).build()
                Log.e("DEBUG", "Database BUILT")
                INSTANCE = instance
                return instance

            }

        }

    }

}