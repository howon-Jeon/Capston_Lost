package com.example.capston_lost

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class LostReportpage : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storageRef: StorageReference
    private lateinit var storage: FirebaseStorage
    private lateinit var imageUriList: MutableList<Uri>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_reportpage)

        // Firestore 초기화
        firestore = FirebaseFirestore.getInstance()
        // Firebase Storage 초기화
        storage = FirebaseStorage.getInstance()
        // 이미지 URI 리스트 초기화
        imageUriList = mutableListOf()

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

        // 물품 종류 선택
        val editTextItemType: EditText = findViewById(R.id.editTextItemType)
        editTextItemType.setOnClickListener {
            showItemTypeDialog()
        }

        // 캘린더 호출
        val editTextGetDate: EditText = findViewById(R.id.editTextGetDate)
        editTextGetDate.setOnClickListener {
            showDatePickerDialog()
        }

        // 갤러리에서 사진 선택
        val cameraIcon: ImageButton = findViewById(R.id.cameraIcon)
        cameraIcon.setOnClickListener {
            openGallery()
        }
    }

    private fun showItemTypeDialog() {
        val items = arrayOf("카드", "지갑", "가방", "기타")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("물품 종류 선택")
            .setItems(items) { _, which ->
                findViewById<EditText>(R.id.editTextItemType).setText(items[which])
            }
        builder.create().show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val date = "$year-${month + 1}-$dayOfMonth"
                findViewById<EditText>(R.id.editTextGetDate).setText(date)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            val selectedImage: Uri? = data.data
            selectedImage?.let {
                if (imageUriList.size < 2) {
                    imageUriList.add(it)
                    uploadImageToFirebase(it)
                } else {
                    Toast.makeText(this, "최대 2장의 이미지만 업로드할 수 있습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadImageToFirebase(fileUri: Uri) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val ref = storage.reference.child("images/$fileName")
        ref.putFile(fileUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    // 이미지 URL을 Firestore에 저장
                    // Firestore 문서에 추가할 필드나 구조에 따라 구현하세요.
                    // 여기서는 예시로 remarks 필드에 이미지 URL을 저장합니다.
                    // 이 부분은 실제 요구사항에 맞게 변경하세요.
                    firestore.collection("find_reports")
                        .add(mapOf("imageUrl" to uri.toString()))
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "이미지 업로드 실패", Toast.LENGTH_SHORT).show()
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
        val lostItem = LostItem(title, itemType, getDate, location, remarks)

        // Firestore 'find_reports' 컬렉션에 데이터 추가
        firestore.collection("lost_reports")
            .add(lostItem)
            .addOnSuccessListener { documentReference ->
                // 추가 성공
                val toastMessage = "글이 작성되었습니다."
                Toast.makeText(this@LostReportpage, toastMessage, Toast.LENGTH_SHORT).show()
                finish() // 화면을 닫음
            }
            .addOnFailureListener { e ->
                // 추가 실패
                val errorMessage = "글 작성 중 오류가 발생했습니다: $e"
                Toast.makeText(this@LostReportpage, errorMessage, Toast.LENGTH_SHORT).show()
            }
    }
}
