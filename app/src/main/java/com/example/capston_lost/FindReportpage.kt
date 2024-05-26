package com.example.capston_lost

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capston_lost.databinding.ActivityFindReportpageBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class FindReportpage : AppCompatActivity() {

    private lateinit var binding: ActivityFindReportpageBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindReportpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        // Finding views using View Binding
        val editTextTitle: EditText = binding.editTextTitle
        val editTextItemType: EditText = binding.editTextItemType
        val editTextGetDate: EditText = binding.editTextGetDate
        val editTextLocation: EditText = binding.editTextLocation
        val editTextKeep: EditText = binding.editTextKeep
        val editTextRemarks: EditText = binding.editTextRemarks
        val submitButton: Button = binding.textViewSubmit
        val closeButton: Button = binding.closeBtn

        submitButton.setOnClickListener {
            val title = editTextTitle.text.toString()
            val itemType = editTextItemType.text.toString()
            val getDate = editTextGetDate.text.toString()
            val location = editTextLocation.text.toString()
            val keepLocation = editTextKeep.text.toString()
            val remarks = editTextRemarks.text.toString()

            val foundItem = hashMapOf(
                "title" to title,
                "itemType" to itemType,
                "getDate" to getDate,
                "location" to location,
                "keepLocation" to keepLocation,
                "remarks" to remarks
            )

            db.collection("foundItems")
                .add(foundItem)
                .addOnSuccessListener {
                    showToast("데이터가 성공적으로 추가되었습니다.")
                }
                .addOnFailureListener {
                    showToast("데이터 추가에 실패했습니다.")
                }
        }

        closeButton.setOnClickListener {
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
