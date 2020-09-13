package com.appdavide.roundtimer.ui.saved


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.appdavide.roundtimer.R
import com.appdavide.roundtimer.data.RoundtimerRoomDatabase
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDbAndRoundsDb
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDbDAO
import com.appdavide.roundtimer.service.SavedRecyclerAdapter
import kotlinx.coroutines.runBlocking


class SavedFragment : Fragment() {

    private lateinit var adapter: SavedRecyclerAdapter

    private lateinit var dao: WorkoutDbDAO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista = inflater.inflate(R.layout.fragment_saved, container, false)

        val recycler = vista.findViewById<RecyclerView>(R.id.saved_recycler)
        recycler.layoutManager = LinearLayoutManager(this.activity)
        adapter = SavedRecyclerAdapter()

        var data: List<WorkoutDbAndRoundsDb>? = null

        runBlocking {
            dao = context?.let { RoundtimerRoomDatabase.getDatabase(it).WorkoutDbDAO() }!!;
            data = dao.getWorkoutDbAndRoundsDb();
        }


        data?.let { adapter.submitList(it) }



        return vista
    }


}
