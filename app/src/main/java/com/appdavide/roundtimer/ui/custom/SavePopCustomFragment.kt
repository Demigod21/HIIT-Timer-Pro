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
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SavePopCustomFragment : DialogFragment() {



    companion object {
        fun newTargetInstance(): SavePopCustomFragment {
            val fragment = SavePopCustomFragment()
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista = inflater.inflate(R.layout.fragment_save_custom_pop, container, false)

        val btn_save_pop = vista.findViewById<FloatingActionButton>(R.id.fab_savepop)
        val edit_name_pop = vista.findViewById<EditText>(R.id.edit_savepop)

        btn_save_pop.setOnClickListener {

            if(edit_name_pop.length() == 0){
                edit_name_pop.error ="Please insert workout name"
            }else{
                var intent = Intent()
                var saveName:String = edit_name_pop.text.toString()
                intent.putExtra("SAVE_POP_CUSTOM_NAME", saveName)
                targetFragment!!.onActivityResult(2, Activity.RESULT_OK, intent)
                dismiss()
            }

        }




        return vista
    }


}