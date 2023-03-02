package com.example.receipeapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

//Airplane Mode Broadcast Receiver class
class AirplaneModeReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        //stores whether airplane mode is enabled or not
        val isAirplaneModeEnabled = intent?.getBooleanExtra("state",false) ?: return

        if(isAirplaneModeEnabled){
            //Displays a Toast Message if Airplane Mode is Enabled.
            //parameters of toast is the application context, the message, and duration of toast
            //.show() method called to display the toast.
            Toast.makeText(context, "Airplane Mode Enabled", Toast.LENGTH_LONG).show()
        }
        else{
            //Displays a Toast Message if Airplane Mode is Disabled
            Toast.makeText(context, "Airplane Mode Disabled", Toast.LENGTH_LONG).show()
        }
    }
}