package com.example.oving1

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(meny: Menu): Boolean {
        super.onCreateOptionsMenu(meny)
        meny.add("Adrian")
        meny.add("Hakvåg")
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.title) {
            "Adrian" -> {
                Log.w("warning", item.title.toString())
            }
            "Hakvåg" -> {
                Log.e("error", item.title.toString())
            }
        }
        return true
    }
}