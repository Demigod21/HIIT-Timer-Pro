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
    version = 2,
    exportSchema = false
)
abstract class RoundtimerRoomDatabase : RoomDatabase(){

    val dbName = "roundtimer.db"

    abstract fun WorkoutDbDAO() : WorkoutDbDAO
    abstract fun RoundDbDAO() : RoundDbDAO


    private class RoundtimerRoomDatabaseCallback(private val scope: CoroutineScope):
            RoomDatabase.Callback(){

        override fun onCreate(db : SupportSQLiteDatabase){
            super.onCreate(db)
            INSTANCE?.let{ database ->
                scope.launch {
                    populateTestingDatabase(database.WorkoutDbDAO(),
                        database.RoundDbDAO())
                }
            }
        }


        override fun onOpen(db: SupportSQLiteDatabase) {
            db.disableWriteAheadLogging()
            super.onOpen(db)
        }

        suspend fun populateTestingDatabase(workoutdbDAO: WorkoutDbDAO,
                                            rounddbDAO: RoundDbDAO){

            workoutdbDAO.deleteAll()
            rounddbDAO.deleteAll()

            Log.e("DEBUG", "Populating Database")
            //WORKOUT



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
                    "roundtimer.db"
                ).addCallback(RoundtimerRoomDatabaseCallback(scope)).build()
                Log.e("DEBUG", "Database BUILT")
                INSTANCE = instance
                return instance

            }

        }

    }

}