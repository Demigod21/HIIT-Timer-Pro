package com.appdavide.roundtimer.ui.saved


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdavide.roundtimer.R
import com.appdavide.roundtimer.data.RoundDb.RoundDb
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDbDAO
import com.appdavide.roundtimer.models.Round
import com.appdavide.roundtimer.service.SavedRecyclerAdapter

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

    private lateinit var data: ArrayList<Round>

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

        adapter.setOnItemClickListener(object : SavedRecyclerAdapter.OnItemClickListener {
            override fun onDeleteClick(position: Int) {
                Log.d("TAG", "LOG ON DELETE SAVE "+position)
                var longlong = adapter.getItemId(position)
                var xx = savedFragmentViewModel.getbyid(longlong)
                xx.observe(viewLifecycleOwner, androidx.lifecycle.Observer { items ->
                    items?.let{
                        organizeRound(it)
                    } })

                Log.d("TAG", "LOG ON DOPO RICERCA ")
                //todo chiamare la getbyid del dao
            }
        })


        return view
    }

    fun organizeRound(list: List<RoundDb>) {
        data = ArrayList<Round>()
        list.forEach{
            var roundToAdd = Round(it.type, it.workDur, it.restDur, it.cycles, it.dur)
            data.add(roundToAdd)
        }
    }


}
