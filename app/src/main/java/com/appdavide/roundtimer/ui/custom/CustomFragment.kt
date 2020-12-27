package com.appdavide.roundtimer.ui.custom


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.appdavide.roundtimer.R
import com.appdavide.roundtimer.Timer
import com.appdavide.roundtimer.data.WorkoutDb.WorkoutDb
import com.appdavide.roundtimer.models.Round
import com.appdavide.roundtimer.service.RoundRecyclerAdapter
import com.appdavide.roundtimer.ui.saved.SavedFragmentViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

class CustomFragment : Fragment() {

    private lateinit var customRoundViewModel: CustomRoundViewModel
    private lateinit var adat: RoundRecyclerAdapter

    private lateinit var data: ArrayList<Round>

    private var saveNameC: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista = inflater.inflate(R.layout.fragment_custom, container, false)

        customRoundViewModel = ViewModelProvider(this).get(CustomRoundViewModel::class.java)

        loadData()
        val recycler = vista.findViewById(R.id.listaCustom) as RecyclerView


        //INIT RECYCLER VIEW
        recycler.layoutManager = LinearLayoutManager(this.activity)
        adat = RoundRecyclerAdapter()
        recycler.adapter = adat

        adat.submitList(data)

        val btnStartTimer = vista.findViewById(R.id.fab_start_timer) as FloatingActionButton
        val btnAddRound  = vista.findViewById(R.id.fab_add_round) as FloatingActionButton
        val btnSaveWorkout = vista.findViewById(R.id.fab_custom_save) as FloatingActionButton

        btnStartTimer.setOnClickListener{
            if (data != null && data.size != 0){
                val context = btnStartTimer.context
                val intent = Intent(context, Timer::class.java)
                intent.removeExtra("dataRounds")
                intent.putExtra("dataRounds", data as Serializable)
                context.startActivity(intent)
            }

        }

        btnAddRound.setOnClickListener{
            val addPopFragment = AddPopFragment.Companion.newTargetInstance()
            addPopFragment.setTargetFragment(this, 1)
            activity?.supportFragmentManager?.beginTransaction()?.let { it1 -> addPopFragment.show(it1, AddPopFragment::class.java.name) }
            saveData()
            adat.notifyDataSetChanged()
        }

        btnSaveWorkout.setOnClickListener{
            val savePopCustomFragment = SavePopCustomFragment.Companion.newTargetInstance()
            savePopCustomFragment.setTargetFragment(this, 2)
            activity?.supportFragmentManager?.beginTransaction()?.let { it1 -> savePopCustomFragment.show(it1, SavePopCustomFragment::class.java.name) }
        }




        val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0){

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val source = viewHolder.adapterPosition
                val dest = target.adapterPosition
                Collections.swap(data, source, dest)
                adat.notifyItemMoved(source, dest)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }
        })
        touchHelper.attachToRecyclerView(recycler)

        return vista
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    override fun onDestroy() {
        super.onDestroy()
        saveData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data2: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == 1) {

            val type = data2!!.getStringExtra("CUSTOM_TYPE")
            val workdur = data2!!.getIntExtra("CUSTOM_WORK_DUR", 0)
            val restdur = data2!!.getIntExtra("CUSTOM_REST_DUR", 0)
            val cycles = data2!!.getIntExtra("CUSTOM_CYCLES", 0)
            val duration = data2!!.getIntExtra("CUSTOM_DURATION", 0)


            data.add(
                Round(
                    type,
                    workdur,
                    restdur,
                    cycles,
                    duration
                )
            )
            saveData()
            adat.notifyDataSetChanged()
        } else if (requestCode == 2 ){
            val saveName2 = data2!!.getStringExtra("SAVE_POP_CUSTOM_NAME")
            saveNameC = saveName2
            storeWorkoutToSave()
        }

    }

    //  fugly spaghetti code
    fun storeWorkoutToSave(){

        //  grab all elements in data arraylist, should process for safety, remove garbage and and move with filterNotNull
        var workoutRounds = ArrayList<Round>()
        workoutRounds.addAll(data)

        //  create temporary workout object to fill with data and send away to saving function
        val workoutToSave = WorkoutDb(saveNameC)

        //  send workout object and rounds list to viewmodel to save
        customRoundViewModel.saveWorkoutAndRounds(workoutToSave, workoutRounds)


    }

    fun saveData(){
        val sharedPref = activity?.getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        val gson = Gson()
        val json = gson.toJson(data)
        editor?.putString("task list", json)
        editor?.apply()
    }

    fun loadData(){
        val sharedPref = activity?.getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val emptyList = Gson().toJson(ArrayList<Round>())
        val gson = Gson()
        val json = sharedPref?.getString("task list", emptyList)
        val type = object : TypeToken<ArrayList<Round>>() {}.type
        data = gson?.fromJson(json, type)

        if (data == null) {
            data = ArrayList()
        }

    }

}
