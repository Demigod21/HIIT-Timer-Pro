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
import android.widget.TextView

import com.appdavide.roundtimer.R
import com.appdavide.roundtimer.Timer
import com.appdavide.roundtimer.models.Round
import com.appdavide.roundtimer.ui.custom.AddPopFragment
import com.appdavide.roundtimer.ui.custom.SaveCustomPopFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable


class SimpleFragment : Fragment() {

    private lateinit var data: ArrayList<Round>
    private var duration: Int = 0
    private var saveName: String = ""




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val vista = inflater.inflate(R.layout.fragment_simple2, container, false)

        val simpePrep = vista.findViewById<EditText>(R.id.input_simpe_prep)
        val simpeWork = vista.findViewById<EditText>(R.id.input_simpe_work)
        val simpeRest = vista.findViewById<EditText>(R.id.input_simpe_rest)
        val simpeCycles = vista.findViewById<EditText>(R.id.input_simpe_cycles)
        val simpeSet = vista.findViewById<EditText>(R.id.input_simpe_set)
        val simpeRestSet = vista.findViewById<EditText>(R.id.input_simpe_restset)
        val simpeCool = vista.findViewById<EditText>(R.id.input_simpe_cooldown)

        data = ArrayList<Round>()

//        loadData() //todo modificare save data e load data per salvare edit text

        val btnSimpleStart = vista.findViewById(R.id.btn_simple_start) as Button
        val btnSimpleSave  = vista.findViewById(R.id.btn_simple_save) as Button

        val txt_save = vista.findViewById<TextView>(R.id.txt_simple_name)


            btnSimpleSave.setOnClickListener {
            val saveCustomPopFragment = SaveCustomPopFragment.Companion.newTargetInstance()
            saveCustomPopFragment.setTargetFragment(this, 1)
            activity?.supportFragmentManager?.beginTransaction()?.let { it1 -> saveCustomPopFragment.show(it1, SaveCustomPopFragment::class.java.name) }
        }

        btnSimpleStart.setOnClickListener{
            val context = btnSimpleStart.context
            val intent = Intent(context, Timer::class.java)

            if(simpePrep.length()== 0 ||
                simpeWork.length()== 0 ||
                simpeRest.length()== 0 ||
                simpeCycles.length()== 0 ||
                simpeSet.length()== 0 ||
                simpeRestSet.length()== 0 ||
                simpeCool.length()== 0 ){

                if(simpePrep.length()== 0){
                    simpePrep.error = "Please insert preparation time"
                }
                if(simpeWork.length()== 0){
                    simpeWork.error = "Please insert work time"
                }
                if(simpeRest.length()== 0){
                    simpeRest.error = "Please insert rest time"
                }
                if(simpeCycles.length()== 0){
                    simpeCycles.error = "Please insert cycles"
                }
                if(simpeSet.length()== 0){
                    simpeSet.error = "Please insert nr of sets"
                }
                if(simpeRestSet.length()== 0){
                    simpeRestSet.error = "Please insert rest between sets"
                }
                if(simpeCool.length()== 0){
                    simpeCool.error = "Please insert cooldown time"
                }

            }else{

                val prep: Int = view?.findViewById<EditText>(R.id.input_simpe_prep)?.text.toString().toInt()
                val work: Int = view?.findViewById<EditText>(R.id.input_simpe_work)?.text.toString().toInt()
                val rest: Int = view?.findViewById<EditText>(R.id.input_simpe_rest)?.text.toString().toInt()
                val cycles: Int = view?.findViewById<EditText>(R.id.input_simpe_cycles)?.text.toString().toInt()
                val sets: Int = view?.findViewById<EditText>(R.id.input_simpe_set)?.text.toString().toInt()
                val restSet: Int = view?.findViewById<EditText>(R.id.input_simpe_restset)?.text.toString().toInt()
                val cool: Int = view?.findViewById<EditText>(R.id.input_simpe_cooldown)?.text.toString().toInt()

//                saveData() //todo modificare save data e load data per salvare edit text

                organizeData(prep, work, rest, cycles, sets, restSet, cool)
                intent.removeExtra("dataRounds")
                intent.putExtra("dataRounds", data as Serializable)
                context.startActivity(intent)
            }

        }

        return vista
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data2: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == 1) {

            val saveName3 = data2!!.getStringExtra("SAVE_POP_NAME")
            saveName = saveName3
            val txt_save2 = view?.findViewById<TextView>(R.id.txt_simple_name)
            txt_save2?.setText(saveName)
            //todo salvataggio del workout

        }
    }




    fun organizeData(prep: Int, work: Int, rest: Int, cycles: Int, sets: Int, restSet: Int, cool: Int){
        data.add(Round("PREPARATION", 0, 0, 0, prep))

        for (i in 1..sets){
            data.add(Round("WORK ROUND", work, rest, cycles, 0))
            if(i!=sets){
                data.add(Round("REST ROUND", 0, 0, 0, restSet))
            }
        }
        data.add(Round("COOLDOWN", 0, 0, 0, cool))

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
