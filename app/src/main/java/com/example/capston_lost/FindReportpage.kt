package com.example.capston_lost

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class FindReportpage : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_reportpage)

        // Firestore 초기화
        firestore = FirebaseFirestore.getInstance()

        // 닫기 버튼 설정
        val closeButton: Button = findViewById(R.id.closeBtn)
        closeButton.setOnClickListener {
            finish() // 화면을 닫음
        }

        // 완료 버튼 설정
        val submitButton: Button = findViewById(R.id.textViewSubmit)
        submitButton.setOnClickListener {
            saveReportToFirestore()
        }
    }

    private fun saveReportToFirestore() {
        val title = findViewById<EditText>(R.id.editTextTitle).text.toString()
        val itemType = findViewById<EditText>(R.id.editTextItemType).text.toString()
        val getDate = findViewById<EditText>(R.id.editTextGetDate).text.toString()
        val location = findViewById<EditText>(R.id.editTextLocation).text.toString()
        val keep = findViewById<EditText>(R.id.editTextKeep).text.toString()
        val remarks = findViewById<EditText>(R.id.editTextRemarks).text.toString()

        // 데이터 클래스 인스턴스 생성
        val foundItem = FoundItem(title, itemType, getDate, location, keep, remarks)

        // Firestore 'find_reports' 컬렉션에 데이터 추가
        firestore.collection("find_reports")
            .add(foundItem)
            .addOnSuccessListener { documentReference ->
                // 추가 성공
                val toastMessage = "글이 작성되었습니다."
                Toast.makeText(this@FindReportpage, toastMessage, Toast.LENGTH_SHORT).show()
                finish() // 화면을 닫음
            }
            .addOnFailureListener { e ->
                // 추가 실패
                val errorMessage = "글 작성 중 오류가 발생했습니다: $e"
                Toast.makeText(this@FindReportpage, errorMessage, Toast.LENGTH_SHORT).show()
            }
    }
}