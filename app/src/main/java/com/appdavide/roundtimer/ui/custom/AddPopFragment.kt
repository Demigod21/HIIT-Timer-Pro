package com.appdavide.roundtimer.ui.custom


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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

        btnAddPopup.setOnClickListener{

            val workdur: Int = view?.findViewById<EditText>(R.id.edit_work_dur)?.text.toString().toInt()
            val restdur: Int = view?.findViewById<EditText>(R.id.edit_rest_dur)?.text.toString().toInt()
            val cycles: Int = view?.findViewById<EditText>(R.id.edit_cycles)?.text.toString().toInt()
            val duration: Int = view?.findViewById<EditText>(R.id.edit_duration)?.text.toString().toInt()

            //todo agiungere i vari controlli

            var intent = Intent()
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


