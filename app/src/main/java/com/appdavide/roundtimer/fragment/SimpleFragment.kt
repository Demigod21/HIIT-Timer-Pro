package com.appdavide.roundtimer.fragment


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

import com.appdavide.roundtimer.R
import com.appdavide.roundtimer.Timer
import com.appdavide.roundtimer.models.Round
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable


class SimpleFragment : Fragment() {

    private lateinit var data: ArrayList<Round>
    private var duration: Int = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val vista = inflater.inflate(R.layout.fragment_simple2, container, false)

        loadData()

        val btnSimpleStart = vista.findViewById(R.id.btn_simple_start) as Button

        btnSimpleStart.setOnClickListener{
            val context = btnSimpleStart.context
            val intent = Intent(context, Timer::class.java)

            val prep: Int = view?.findViewById<EditText>(R.id.input_simpe_prep)?.text.toString().toInt()
            val work: Int = view?.findViewById<EditText>(R.id.input_simpe_work)?.text.toString().toInt()
            val rest: Int = view?.findViewById<EditText>(R.id.input_simpe_rest)?.text.toString().toInt()
            val cycles: Int = view?.findViewById<EditText>(R.id.input_simpe_cycles)?.text.toString().toInt()
            val sets: Int = view?.findViewById<EditText>(R.id.input_simpe_set)?.text.toString().toInt()
            val restSet: Int = view?.findViewById<EditText>(R.id.input_simpe_restset)?.text.toString().toInt()
            val cool: Int = view?.findViewById<EditText>(R.id.input_simpe_cooldown)?.text.toString().toInt()

            saveData()

            //todo controlli su sta cazzo di robaaaaaaaa

            organizeData(prep, work, rest, cycles, sets, restSet, cool)
            intent.putExtra("dataRounds", data as Serializable)
            context.startActivity(intent)
        }

        return vista
    }




    fun organizeData(prep: Int, work: Int, rest: Int, cycles: Int, sets: Int, restSet: Int, cool: Int){
        data.add(Round("Preparation", 0, 0, 0, prep))

        for (i in 1..sets){
            data.add(Round("Work Round", work, rest, cycles, 0))
            if(i!=sets){
                data.add(Round("Rest Round", 0, 0, 0, restSet))
            }
        }
        data.add(Round("Cooldown", 0, 0, 0, cool))

    }

    //todo catturare evento cambiamento menu per saveData

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
