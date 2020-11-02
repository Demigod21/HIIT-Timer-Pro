package com.appdavide.roundtimer.ui.saved


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.appdavide.roundtimer.R
import com.appdavide.roundtimer.data.Repository
import com.appdavide.roundtimer.data.RoundtimerRoomDatabase
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDbAndRoundsDb
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDbDAO
import com.appdavide.roundtimer.service.SavedRecyclerAdapter
import java.util.Observer

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SavedFragment : Fragment() {

    private lateinit var savedFragmentViewModel: SavedFragmentViewModel

    private lateinit var dao: WorkoutDbDAO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_saved, container, false)

        //  This fragment viewmodel
        savedFragmentViewModel = ViewModelProvider(this).get(SavedFragmentViewModel::class.java)

        //  Recycler and adapter
        val savedRecycler = view.findViewById<RecyclerView>(R.id.saved_recycler)
        savedRecycler.layoutManager = LinearLayoutManager(this.activity)
        val adapter = SavedRecyclerAdapter()
        savedRecycler.adapter = adapter

        savedFragmentViewModel.allWorkouts.observe(viewLifecycleOwner, androidx.lifecycle.Observer { items ->
            items?.let{adapter.setItems(it)
        } })


        return view
    }


}
