package com.example.capston_lost

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class LostReportpage : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storageRef: StorageReference
    private lateinit var storage: FirebaseStorage
    private lateinit var imageUriList: MutableList<Uri>
    private lateinit var selectedImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lost_reportpage)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        imageUriList = mutableListOf()

        val closeButton: Button = findViewById(R.id.closeBtn)
        closeButton.setOnClickListener {
            finish()
        }

        val submitButton: Button = findViewById(R.id.textViewSubmit)
        submitButton.setOnClickListener {
            saveReportToFirestore()
        }

        val editTextItemType: EditText = findViewById(R.id.editTextItemType)
        editTextItemType.setOnClickListener {
            showItemTypeDialog()
        }

        val editTextGetDate: EditText = findViewById(R.id.editTextGetDate)
        editTextGetDate.setOnClickListener {
            showDatePickerDialog()
        }

        selectedImageView = findViewById(R.id.selectedImageView)
        val selectImageButton: ImageButton = findViewById(R.id.selectImageButton)
        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                imageUriList.add(uri)
                // 이미지 선택 후 이미지뷰에 표시
                Glide.with(this).load(uri).into(selectedImageView)
            }
        }
    }

    private fun showItemTypeDialog() {
        val itemTypes = arrayOf("가방", "옷", "지갑", "전자기기", "기타")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("물품 종류 선택")
        builder.setItems(itemTypes) { _, which ->
            val selectedItemType = itemTypes[which]
            val editTextItemType: EditText = findViewById(R.id.editTextItemType)
            editTextItemType.setText(selectedItemType)
        }
        builder.show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val dateString = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            val editTextGetDate: EditText = findViewById(R.id.editTextGetDate)
            editTextGetDate.setText(dateString)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun saveReportToFirestore() {
        val editTextTitle: EditText = findViewById(R.id.editTextTitle)
        val editTextItemType: EditText = findViewById(R.id.editTextItemType)
        val editTextGetDate: EditText = findViewById(R.id.editTextGetDate)
        val editTextLocation: EditText = findViewById(R.id.editTextLocation)
        val editTextRemarks: EditText = findViewById(R.id.editTextRemarks)

        val sharedPref = getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("userName", "Unknown")
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val title = editTextTitle.text.toString()
        val itemType = editTextItemType.text.toString()
        val getDate = editTextGetDate.text.toString()
        val location = editTextLocation.text.toString()
        val remarks = editTextRemarks.text.toString()

        if (userId != null) {
            // 이미지 업로드 후 Firestore에 저장
            uploadImagesToStorage(userId) { imageUrls ->
                val report = LostItem(
                    title = title,
                    itemType = itemType,
                    getDate = getDate,
                    location = location,
                    remarks = remarks,
                    userId = userId,
                    imageUrl = imageUrls.joinToString(","),
                    nickname = userName ?: "Unknown"
                )

                firestore.collection("lost_reports")
                    .add(report)
                    .addOnSuccessListener {
                        Toast.makeText(this, "분실 신고가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "저장 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun uploadImagesToStorage(userId: String, onSuccess: (List<String>) -> Unit) {
        val imageUrls = mutableListOf<String>()
        val storageRef = storage.reference.child("lost_images").child(userId)

        for (uri in imageUriList) {
            val fileRef = storageRef.child(System.currentTimeMillis().toString() + ".jpg")
            fileRef.putFile(uri).addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    imageUrls.add(downloadUri.toString())
                    if (imageUrls.size == imageUriList.size) {
                        onSuccess(imageUrls)
                    }
                }
            }.addOnFailureListener {
                // 업로드 실패 처리
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
    }
}
