package com.appdavide.roundtimer.ui.custom

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.appdavide.roundtimer.R
import kotlinx.android.synthetic.main.popup_add.*

class SaveCustomPopFragment : DialogFragment() {



    companion object {
        fun newTargetInstance(): SaveCustomPopFragment {
            val fragment = SaveCustomPopFragment()
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista = inflater.inflate(R.layout.fragment_save_custom_pop, container, false)

        val btn_save_pop = vista.findViewById<Button>(R.id.btn_savepop)
        val edit_name_pop = vista.findViewById<EditText>(R.id.edit_savepop)

        btn_save_pop.setOnClickListener {

            if(edit_name_pop.length() == 0){
                edit_name_pop.error ="Please insert workout name"
            }else{
                var intent = Intent()
                var saveName:String = edit_name_pop.text.toString()
                intent.putExtra("SAVE_POP_NAME", saveName)
                targetFragment!!.onActivityResult(1, Activity.RESULT_OK, intent) //todo cambiare questo request code
                dismiss()
            }

        }




        return vista
    }


}