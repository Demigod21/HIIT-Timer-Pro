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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.appdavide.roundtimer.R
import com.appdavide.roundtimer.Timer
import com.appdavide.roundtimer.models.Round
import com.appdavide.roundtimer.service.RoundRecyclerAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

class CustomFragment : Fragment() {

    private lateinit var adat: RoundRecyclerAdapter

    private lateinit var data: ArrayList<Round>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista = inflater.inflate(R.layout.fragment_custom, container, false)

        Log.d("TAG", "LOG PRIMA LOAD DATA")
        loadData()
        Log.d("TAG", "LOG DOPO LOAD DATA")

        Log.d("TAG", "LOG PRIMA DEL FIND BY ID")
        val recycler = vista.findViewById(R.id.listaCustom) as RecyclerView
        Log.d("TAG", "LOG DOPO DEL FIND BY ID")


        //INIT RECYCLER VIEW
        recycler.layoutManager = LinearLayoutManager(this.activity)
        adat = RoundRecyclerAdapter()
        recycler.adapter = adat

        Log.d("TAG", "LOG DOPO INIT RECYCLER")



        //ADD INIT DATA TO RECYCLER
//        val data = com.appdavide.roundtimer.repository.DataSource.createDataSet()
//        data = DataSource.list2 //todo guardare questo

        adat.submitList(data)


        Log.d("TAG", "LOG DOPO ADD DATA TO RECYCLER")


        val btnStartTimer = vista.findViewById(R.id.btn_start_timer) as Button

        val btnAddRound : Button = vista.findViewById(R.id.btn_add_round) as Button

        val testo = "Passaggio Parametri"
        val array : Array<String> = Array(2) {"Test2"; "Test3"}



        btnStartTimer.setOnClickListener{
            val context = btnStartTimer.context
            val intent = Intent(context, Timer::class.java)
//            intent.putExtra("test", testo)
//            intent.putExtra("test2", array)
            intent.putExtra("dataRounds", data as Serializable)
            context.startActivity(intent)
        }

        btnAddRound.setOnClickListener{


            Log.d("TAG", "LOG PRIMA DI ADDPOPFRAGMENT")
            val addPopFragment = AddPopFragment.Companion.newTargetInstance()
            addPopFragment.setTargetFragment(this, 1)
            activity?.supportFragmentManager?.beginTransaction()?.let { it1 -> addPopFragment.show(it1, AddPopFragment::class.java.name) }

            Log.d("TAG", "LOG PRIMA SAVE DATA")
            saveData()
            Log.d("TAG", "LOG DOPO SAVE DATA")
            adat.notifyDataSetChanged()
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
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })


        touchHelper.attachToRecyclerView(recycler)


        return vista

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

        }
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
