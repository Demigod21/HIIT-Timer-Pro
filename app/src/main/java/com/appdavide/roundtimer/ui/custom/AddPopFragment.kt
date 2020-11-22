package com.appdavide.roundtimer.ui.custom


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment


import com.appdavide.roundtimer.R

class AddPopFragment : DialogFragment() {


    companion object {
        fun newTargetInstance(): AddPopFragment {
            val fragment = AddPopFragment()
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista = inflater.inflate(R.layout.popup_add, container, false)


        val btnAddPopup = vista.findViewById(R.id.btn_add_popup) as Button


        val spinner: Spinner = vista.findViewById(R.id.spinner2)
        ArrayAdapter.createFromResource(
            this.context!!,
            R.array.numbers,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        val xxx = view?.findViewById<EditText>(R.id.edit_work_dur)

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){
                    0-> Log.d("TAG", "LOG POSIION 0")
                    1-> Log.d("TAG", "LOG ID 1")
                    3-> {
                        var xs = view?.findViewById<EditText>(R.id.edit_work_dur)
                        xs?.visibility = View.GONE
                        xs?.setText("22")
                    }
                }
            }


        }

        btnAddPopup.setOnClickListener{

            val type = view?.findViewById<Spinner>(R.id.spinner2)?.selectedItem.toString()

            val workdur: Int = view?.findViewById<EditText>(R.id.edit_work_dur)?.text.toString().toInt()
            val restdur: Int = view?.findViewById<EditText>(R.id.edit_rest_dur)?.text.toString().toInt()
            val cycles: Int = view?.findViewById<EditText>(R.id.edit_cycles)?.text.toString().toInt()
            val duration: Int = view?.findViewById<EditText>(R.id.edit_duration)?.text.toString().toInt()

            //todo agiungere i vari controlli

            var intent = Intent()
            intent.putExtra("CUSTOM_TYPE", type)
            intent.putExtra("CUSTOM_WORK_DUR", workdur)
            intent.putExtra("CUSTOM_REST_DUR", restdur)
            intent.putExtra("CUSTOM_CYCLES", cycles)
            intent.putExtra("CUSTOM_DURATION", duration)

            targetFragment!!.onActivityResult(1, Activity.RESULT_OK, intent)
            dismiss()
        }


        return vista
    }


}


