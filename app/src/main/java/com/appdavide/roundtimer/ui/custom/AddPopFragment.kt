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
import kotlinx.android.synthetic.main.popup_add.*

class AddPopFragment : DialogFragment() {

    lateinit var work_dur : EditText
    lateinit var rest_dur : EditText
    lateinit var dur : EditText
    lateinit var cycles_edit : EditText


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
        val btnCancelPopup = vista.findViewById(R.id.btn_cancel_popup) as Button

        work_dur = vista.findViewById(R.id.edit_work_dur)
        rest_dur = vista.findViewById(R.id.edit_rest_dur)
        cycles_edit = vista.findViewById(R.id.edit_cycles)
        dur = vista.findViewById(R.id.edit_duration)


        val spinner: Spinner = vista.findViewById(R.id.spinner2)
        ArrayAdapter.createFromResource(
            this.context!!,
            R.array.numbers,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

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
                    0->{ //preparation
                        work_dur?.visibility = View.GONE
                        edit_cycles?.visibility = View.GONE
                        rest_dur?.visibility = View.GONE
                    }
                    1-> { //work round
                        work_dur?.visibility = View.VISIBLE
                        edit_cycles?.visibility = View.VISIBLE
                        rest_dur?.visibility = View.VISIBLE
                        dur.isEnabled = false
                    }
                    2 -> { //rest round
                        work_dur?.visibility = View.GONE
                        edit_cycles?.visibility = View.GONE
                        rest_dur?.visibility = View.GONE
                    }
                    3-> { //cool
                        work_dur?.visibility = View.GONE
                        edit_cycles?.visibility = View.GONE
                        rest_dur?.visibility = View.GONE

                    }
                }
            }


        }

        btnAddPopup.setOnClickListener{

            val type = view?.findViewById<Spinner>(R.id.spinner2)?.selectedItem.toString()
            var intent = Intent()

            when (type){
                "PREPARATION"->{
                    dur.error = null //todo non setta error a null
                    if (dur.length() == 0){
                        dur.error = "Please insert duration"
                    }else{
                        val duration: Int = view?.findViewById<EditText>(R.id.edit_duration)?.text.toString().toInt()

                        intent.putExtra("CUSTOM_TYPE", type)
                        intent.putExtra("CUSTOM_DURATION", duration)

                        targetFragment!!.onActivityResult(1, Activity.RESULT_OK, intent)
                        dismiss()
                    }
                }
                "WORK ROUND" ->{
                    dur.error = null
                    if (work_dur.length() == 0 || rest_dur.length() == 0 || cycles_edit.length() == 0){
                        if(work_dur.length() == 0 ){
                            work_dur.error = "Please insert work duration"
                        }
                        if(rest_dur.length() == 0 ){
                            rest_dur.error = "Please insert rest duration"
                        }
                        if(cycles_edit.length() == 0 ){
                            cycles_edit.error = "Please insert cycles"
                        }
                    }else{
                        val workdur: Int = view?.findViewById<EditText>(R.id.edit_work_dur)?.text.toString().toInt()
                        val restdur: Int = view?.findViewById<EditText>(R.id.edit_rest_dur)?.text.toString().toInt()
                        val cycles: Int = view?.findViewById<EditText>(R.id.edit_cycles)?.text.toString().toInt()

                        intent.putExtra("CUSTOM_TYPE", type)
                        intent.putExtra("CUSTOM_WORK_DUR", workdur)
                        intent.putExtra("CUSTOM_REST_DUR", restdur)
                        intent.putExtra("CUSTOM_CYCLES", cycles)

                        targetFragment!!.onActivityResult(1, Activity.RESULT_OK, intent)
                        dismiss()
                    }


                }
                "REST ROUND" -> {
                    dur.error = null
                    if (dur.length() == 0){
                        dur.error = "Please insert duration"
                    }else{
                        val duration: Int = view?.findViewById<EditText>(R.id.edit_duration)?.text.toString().toInt()
                        intent.putExtra("CUSTOM_TYPE", type)
                        intent.putExtra("CUSTOM_DURATION", duration)
                        targetFragment!!.onActivityResult(1, Activity.RESULT_OK, intent)
                        dismiss()
                    }
                }
                "COOLDOWN" -> {
                    dur.error = null
                    if (dur.length() == 0){
                        dur.error = "Please insert duration"
                    }else{
                        val duration: Int = view?.findViewById<EditText>(R.id.edit_duration)?.text.toString().toInt()
                        intent.putExtra("CUSTOM_TYPE", type)
                        intent.putExtra("CUSTOM_DURATION", duration)
                        targetFragment!!.onActivityResult(1, Activity.RESULT_OK, intent)
                        dismiss()
                    }
                }
            }

/*            val workdur: Int = view?.findViewById<EditText>(R.id.edit_work_dur)?.text.toString().toInt()
            val restdur: Int = view?.findViewById<EditText>(R.id.edit_rest_dur)?.text.toString().toInt()
            val cycles: Int = view?.findViewById<EditText>(R.id.edit_cycles)?.text.toString().toInt()
            val duration: Int = view?.findViewById<EditText>(R.id.edit_duration)?.text.toString().toInt()


            //todo agiungere i vari controlli

            intent.putExtra("CUSTOM_TYPE", type)
            intent.putExtra("CUSTOM_WORK_DUR", workdur)
            intent.putExtra("CUSTOM_REST_DUR", restdur)
            intent.putExtra("CUSTOM_CYCLES", cycles)
            intent.putExtra("CUSTOM_DURATION", duration)

            targetFragment!!.onActivityResult(1, Activity.RESULT_OK, intent)
            dismiss()*/
        }

        btnCancelPopup.setOnClickListener{
            dismiss()
        }




        return vista
    }


}


