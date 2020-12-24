package com.appdavide.roundtimer.ui.saved


import android.content.Intent
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
import com.appdavide.roundtimer.Timer
import com.appdavide.roundtimer.data.RoundDb.RoundDb
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDbDAO
import com.appdavide.roundtimer.models.Round
import com.appdavide.roundtimer.service.SavedRecyclerAdapter
import java.io.Serializable

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

    private var nrRound : Int = 0
    private var iterRound : Int = 0


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

/*                Log.d("TAG", "LOG ON DOPO RICERCA ")
                val context = context
                val intent = Intent(context, Timer::class.java)
                intent.removeExtra("dataRounds")
                intent.putExtra("dataRounds", data as Serializable)
                context?.startActivity(intent)*/
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
        val context = context
        val intent = Intent(context, Timer::class.java)
        intent.removeExtra("dataRounds")
        intent.putExtra("dataRounds", data as Serializable)
        context?.startActivity(intent)
    }


}
