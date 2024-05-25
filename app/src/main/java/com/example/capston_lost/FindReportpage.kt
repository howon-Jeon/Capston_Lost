package com.example.capston_lost

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FindReportpage : AppCompatActivity() {

    private lateinit var editTextTitle: EditText
    private lateinit var editTextItemType: EditText
    private lateinit var editTextGetDate: EditText
    private lateinit var editTextLocation: EditText
    private lateinit var editTextKeep: EditText
    private lateinit var editTextRemarks: EditText
    private lateinit var submitButton: Button
    private lateinit var db: FirebaseFirestore
    val fbdb = Firebase.firestore
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_reportpage)

        val btn1=findViewById<Button>(R.id.textViewSubmit)
        val title=findViewById<EditText>(R.id.editTextTitle)
        val itemtype=findViewById<EditText>(R.id.editTextItemType)
        btn1.setOnClickListener {
            val atitle=title.text
            val aitem= itemtype.text

            val adata= hashMapOf(
                "title" to aitem.toString()
            )

            fbdb.collection("findreport").document(atitle.toString()).set(adata)
                .addOnSuccessListener {
                    showToast("데이터가 성공적으로 추가되었습니다.")
                }
                .addOnFailureListener { e ->
                    // 추가 실패 시 처리
                    showToast("데이터 추가에 실패했습니다.")
                }

        }




    }
}
/*
        // Firebase Firestore 초기화
        db = FirebaseFirestore.getInstance()

        // XML에서 뷰 요소들 찾기
        editTextTitle = findViewById(R.id.editTextTitle)
        editTextItemType = findViewById(R.id.editTextItemType)
        editTextGetDate = findViewById(R.id.editTextGetDate)
        editTextLocation = findViewById(R.id.editTextLocation)
        editTextKeep = findViewById(R.id.editTextKeep)
        editTextRemarks = findViewById(R.id.editTextRemarks)
        submitButton = findViewById(R.id.textViewSubmit)

        // 완료 버튼 클릭 시 데이터 저장
        submitButton.setOnClickListener {
            val title = editTextTitle.text.toString()
            val itemType = editTextItemType.text.toString()
            val getDate = editTextGetDate.text.toString()
            val location = editTextLocation.text.toString()
            val keepLocation = editTextKeep.text.toString()
            val remarks = editTextRemarks.text.toString()

            // Firestore에 저장할 데이터 객체 생성
            val foundItem = FoundItem(
                title = title,
                itemType = itemType,
                getDate = getDate,
                location = location,
                keepLocation = keepLocation,
                remarks = remarks
            )

            // Firestore에 데이터 추가
            db.collection("foundItems")
                .add(foundItem)
                .addOnSuccessListener { documentReference ->
                    // 성공적으로 추가됐을 때 처리
                    showToast("데이터가 성공적으로 추가되었습니다.")
                }
                .addOnFailureListener { e ->
                    // 추가 실패 시 처리
                    showToast("데이터 추가에 실패했습니다.")
                }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}*/