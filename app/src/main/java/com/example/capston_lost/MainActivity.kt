package com.example.capston_lost

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.found_detail)
/*
        findViewById<TextView>(R.id.textViewClose).setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.textViewSubmit).setOnClickListener {
            val title = findViewById<TextView>(R.id.editTextTitle).text.toString()
            val itemType = findViewById<TextView>(R.id.editTextItemType).text.toString()
            val lossDate = findViewById<TextView>(R.id.editTextLossDate).text.toString()
            val location = findViewById<TextView>(R.id.editTextLocation).text.toString()
            val remarks = findViewById<TextView>(R.id.editTextRemarks).text.toString()

        }*/
    }
}