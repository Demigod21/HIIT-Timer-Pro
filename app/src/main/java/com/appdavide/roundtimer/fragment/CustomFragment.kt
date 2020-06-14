package com.appdavide.roundtimer.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.appdavide.roundtimer.R
import com.appdavide.roundtimer.Timer
import com.appdavide.roundtimer.models.Round
import com.appdavide.roundtimer.service.RoundRecyclerAdapter
import javax.sql.DataSource

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class CustomFragment : Fragment() {

    private lateinit var adat: RoundRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista = inflater.inflate(R.layout.fragment_custom, container, false)

        Log.d("TAG", "LOG PRIMA DEL FIND BY ID")


        val recycler = vista.findViewById(R.id.listaCustom) as RecyclerView

        Log.d("TAG", "LOG DOPO DEL FIND BY ID")


        //INIT RECYCLER VIEW
        recycler.layoutManager = LinearLayoutManager(this.activity)
        adat = RoundRecyclerAdapter()
        recycler.adapter = adat

        Log.d("TAG", "LOG DOPO INIT RECYCLER")


        //ADD INIT DATA TO RECYCLER
        val data = com.appdavide.roundtimer.repository.DataSource.createDataSet()

        adat.submitList(data)


        Log.d("TAG", "LOG DOPO ADD DATA TO RECYCLER")


        val btnStartTimer = vista.findViewById(R.id.btn_start_timer) as Button
        val btnAddRound : Button = vista.findViewById(R.id.btn_add_round) as Button

        val testo = "Passaggio Parametri"
        val array : Array<String> = Array(2) {"Test2"; "Test3"}

        btnStartTimer.setOnClickListener{
            val context = btnStartTimer.context
            val intent = Intent(context, Timer::class.java)
            intent.putExtra("test", testo)
            intent.putExtra("test2", array)
            context.startActivity(intent)
        }

        btnAddRound.setOnClickListener{

            data.add(
                Round(
                    "provaaggiunta",
                    1,
                    1,
                    1,
                    10
                )
            )
            Log.d("TAG", "LOG DOPO ADD ROUND A LISTA")
            adat.notifyDataSetChanged()


        }

        return vista



    }


}
