package com.example.capston_lost

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class FoundDetail : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    private lateinit var titleTextView: TextView
    private lateinit var itemTypeTextView: TextView
    private lateinit var getDateTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var keepTextView: TextView
    private lateinit var remarksTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.found_detail)

        // Firebase Firestore 초기화
        firestore = FirebaseFirestore.getInstance()

        // UI 요소 초기화
        titleTextView = findViewById(R.id.found_detail_cat)
        itemTypeTextView = findViewById(R.id.found_detail_cat)
        getDateTextView = findViewById(R.id.found_detail_date)
        locationTextView = findViewById(R.id.found_detail_lostloc)
        keepTextView = findViewById(R.id.found_detail_storageloc)
        remarksTextView = findViewById(R.id.found_detail_comment)

        // Intent로 전달된 document ID 가져오기
        val documentId = intent.getStringExtra("documentId")

        // Firestore에서 데이터 가져오기
        if (documentId != null) {
            firestore.collection("find_reports").document(documentId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val foundItem = document.toObject(FoundItem::class.java)
                        if (foundItem != null) {
                            // UI에 데이터 설정
                            titleTextView.text = foundItem.itemType
                            itemTypeTextView.text = foundItem.itemType
                            getDateTextView.text = foundItem.getDate
                            locationTextView.text = foundItem.location
                            keepTextView.text = foundItem.keep
                            remarksTextView.text = foundItem.remarks
                        } else {
                            Log.d(TAG, "DocumentSnapshot data: null")
                        }
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
        }
    }

    companion object {
        private const val TAG = "FoundDetail"
    }
}
