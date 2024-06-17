package com.example.capston_lost

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*

// LostReportpage 액티비티 클래스 정의
class LostReportpage : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore // Firestore 인스턴스 변수 선언
    private lateinit var storageRef: StorageReference // StorageReference 인스턴스 변수 선언
    private lateinit var storage: FirebaseStorage // Firebase Storage 인스턴스 변수 선언
    private lateinit var imageUriList: MutableList<Uri> // 이미지 URI 리스트 변수 선언
    private lateinit var selectedImageView: ImageView // 선택된 이미지뷰 변수 선언
    private lateinit var uploadProgressBar: ProgressBar // 업로드 진행바 변수 선언
    private lateinit var uploadStatusTextView: TextView // 업로드 상태 텍스트뷰 변수 선언

    // 액티비티 생성 시 호출되는 메소드
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lost_reportpage) // 레이아웃 설정

        firestore = FirebaseFirestore.getInstance() // Firestore 인스턴스 초기화
        storage = FirebaseStorage.getInstance() // Firebase Storage 인스턴스 초기화
        imageUriList = mutableListOf() // 이미지 URI 리스트 초기화

        uploadProgressBar = findViewById(R.id.uploadProgressBar) // 업로드 진행바 초기화
        uploadStatusTextView = findViewById(R.id.uploadStatusTextView) // 업로드 상태 텍스트뷰 초기화

        val closeButton: Button = findViewById(R.id.closeBtn) // 닫기 버튼 초기화
        closeButton.setOnClickListener {
            finish() // 액티비티 종료
        }

        val submitButton: Button = findViewById(R.id.textViewSubmit) // 제출 버튼 초기화
        submitButton.setOnClickListener {
            if (isFormValid()) { // 폼이 유효한지 검사
                uploadProgressBar.visibility = View.VISIBLE // 업로드 진행바 보이기
                uploadStatusTextView.visibility = View.VISIBLE // 업로드 상태 텍스트뷰 보이기
                saveReportToFirestore() // Firestore에 신고 저장
            } else {
                Snackbar.make(submitButton, "모든 필드를 입력해주세요.", Snackbar.LENGTH_SHORT).show() // 오류 메시지 표시
            }
        }

        val editTextItemType: EditText = findViewById(R.id.editTextItemType) // 물품 종류 입력 필드 초기화
        editTextItemType.setOnClickListener {
            showItemTypeDialog() // 아이템 타입 선택 다이얼로그 표시
        }

        val editTextGetDate: EditText = findViewById(R.id.editTextGetDate) // 날짜 입력 필드 초기화
        editTextGetDate.setOnClickListener {
            showDatePickerDialog() // 날짜 선택 다이얼로그 표시
        }

        selectedImageView = findViewById(R.id.selectedImageView) // 선택된 이미지뷰 초기화
        val selectImageButton: ImageButton = findViewById(R.id.selectImageButton) // 이미지 선택 버튼 초기화
        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI) // 이미지 선택 인텐트 생성
            intent.type = "image/*" // 이미지 타입 설정
            startActivityForResult(intent, REQUEST_IMAGE_PICK) // 인텐트 시작
        }
    }

    // 액티비티 결과를 처리하는 메소드
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) { // 이미지 선택 결과 확인
            data?.data?.let { uri ->
                imageUriList.add(uri) // 이미지 URI 리스트에 추가
                // 이미지 선택 후 이미지뷰에 표시
                Glide.with(this).load(uri).into(selectedImageView) // Glide를 사용하여 이미지뷰에 로드
            }
        }
    }

    // 아이템 타입 선택 다이얼로그를 표시하는 메소드
    private fun showItemTypeDialog() {
        val itemTypes = arrayOf("가방", "옷", "지갑", "전자기기", "기타") // 아이템 타입 배열
        val builder = AlertDialog.Builder(this) // 다이얼로그 빌더 생성
        builder.setTitle("물품 종류 선택") // 다이얼로그 제목 설정
        builder.setItems(itemTypes) { _, which ->
            val selectedItemType = itemTypes[which] // 선택된 아이템 타입
            val editTextItemType: EditText = findViewById(R.id.editTextItemType) // 아이템 타입 입력 필드
            editTextItemType.setText(selectedItemType) // 입력 필드에 설정
        }
        builder.show() // 다이얼로그 표시
    }

    // 날짜 선택 다이얼로그를 표시하는 메소드
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance() // 현재 날짜와 시간을 가져오기 위한 Calendar 인스턴스
        val year = calendar.get(Calendar.YEAR) // 현재 년도
        val month = calendar.get(Calendar.MONTH) // 현재 월
        val day = calendar.get(Calendar.DAY_OF_MONTH) // 현재 일

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val calendar = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay) // 선택된 날짜로 Calendar 설정
            }
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // 날짜 형식 지정
            val dateString = dateFormat.format(calendar.time) // 날짜를 문자열로 변환
            val editTextGetDate: EditText = findViewById(R.id.editTextGetDate) // 날짜 입력 필드
            editTextGetDate.setText(dateString) // 입력 필드에 설정
        }, year, month, day) // 현재 날짜로 초기화

        datePickerDialog.show() // 날짜 선택 다이얼로그 표시
    }

    // Firestore에 신고를 저장하는 메소드
    private fun saveReportToFirestore() {
        val editTextTitle: EditText = findViewById(R.id.editTextTitle) // 제목 입력 필드
        val editTextItemType: EditText = findViewById(R.id.editTextItemType) // 아이템 타입 입력 필드
        val editTextGetDate: EditText = findViewById(R.id.editTextGetDate) // 날짜 입력 필드
        val editTextLocation: EditText = findViewById(R.id.editTextLocation) // 위치 입력 필드
        val editTextRemarks: EditText = findViewById(R.id.editTextRemarks) // 비고 입력 필드

        val userId = FirebaseAuth.getInstance().currentUser?.uid // 현재 사용자 ID 가져오기
        val title = editTextTitle.text.toString() // 제목 텍스트 가져오기
        val itemType = editTextItemType.text.toString() // 아이템 타입 텍스트 가져오기
        val getDate = editTextGetDate.text.toString() // 날짜 텍스트 가져오기
        val location = editTextLocation.text.toString() // 위치 텍스트 가져오기
        val remarks = editTextRemarks.text.toString() // 비고 텍스트 가져오기

        if (userId != null) { // 사용자 ID가 null이 아닌 경우
            // Firestore에서 사용자 이름을 가져오기
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val userName = document.getString("name") ?: "Unknown" // 사용자 이름 가져오기
                        uploadImagesToStorage(userId) { imageUrls ->
                            val report = LostItem(
                                title = title, // 제목 설정
                                itemType = itemType, // 아이템 타입 설정
                                getDate = getDate, // 날짜 설정
                                location = location, // 위치 설정
                                remarks = remarks, // 비고 설정
                                userId = userId, // 사용자 ID 설정
                                imageUrl = imageUrls.joinToString(","), // 이미지 URL 설정
                                nickname = userName // 사용자 이름 설정
                            )

                            firestore.collection("lost_reports") // Firestore 컬렉션 참조
                                .add(report) // 데이터 추가
                                .addOnSuccessListener {
                                    Toast.makeText(this, "분실 신고가 저장되었습니다.", Toast.LENGTH_SHORT).show() // 성공 메시지 표시
                                    uploadProgressBar.visibility = View.GONE // 업로드 진행바 숨기기
                                    uploadStatusTextView.visibility = View.GONE // 업로드 상태 텍스트뷰 숨기기
                                    finish() // 액티비티 종료
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "저장 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show() // 오류 메시지 표시
                                    uploadProgressBar.visibility = View.GONE // 업로드 진행바 숨기기
                                    uploadStatusTextView.visibility = View.GONE // 업로드 상태 텍스트뷰 숨기기
                                }
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "사용자 정보를 가져오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show() // 오류 메시지 표시
                    uploadProgressBar.visibility = View.GONE // 업로드 진행바 숨기기
                    uploadStatusTextView.visibility = View.GONE // 업로드 상태 텍스트뷰 숨기기
                }
        }
    }

    // 폼이 유효한지 검사하는 메소드
    private fun isFormValid(): Boolean {
        val editTextTitle: EditText = findViewById(R.id.editTextTitle) // 제목 입력 필드
        val editTextItemType: EditText = findViewById(R.id.editTextItemType) // 아이템 타입 입력 필드
        val editTextGetDate: EditText = findViewById(R.id.editTextGetDate) // 날짜 입력 필드
        val editTextLocation: EditText = findViewById(R.id.editTextLocation) // 위치 입력 필드
        val editTextRemarks: EditText = findViewById(R.id.editTextRemarks) // 비고 입력 필드

        return editTextTitle.text.isNotEmpty() && // 제목이 비어있지 않은지 확인
                editTextItemType.text.isNotEmpty() && // 아이템 타입이 비어있지 않은지 확인
                editTextGetDate.text.isNotEmpty() && // 날짜가 비어있지 않은지 확인
                editTextLocation.text.isNotEmpty() && // 위치가 비어있지 않은지 확인
                editTextRemarks.text.isNotEmpty() // 비고가 비어있지 않은지 확인
    }

    // 이미지를 Firebase Storage에 업로드하는 메소드
    private fun uploadImagesToStorage(userId: String, onSuccess: (List<String>) -> Unit) {
        val imageUrls = mutableListOf<String>() // 이미지 URL 리스트 초기화
        val storageRef = storage.reference.child("lost_images").child(userId) // Firebase Storage 참조 경로 설정

        for (uri in imageUriList) { // 이미지 URI 리스트를 순회
            val fileRef = storageRef.child(System.currentTimeMillis().toString() + ".jpg") // 파일 참조 경로 설정
            fileRef.putFile(uri).addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    imageUrls.add(downloadUri.toString()) // 이미지 URL 리스트에 추가
                    if (imageUrls.size == imageUriList.size) { // 모든 이미지가 업로드 되었는지 확인
                        onSuccess(imageUrls) // 성공 콜백 호출
                    }
                }
            }.addOnFailureListener {
                // 업로드 실패 처리
                uploadProgressBar.visibility = View.GONE // 업로드 진행바 숨기기
                uploadStatusTextView.visibility = View.GONE // 업로드 상태 텍스트뷰 숨기기
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1 // 이미지 선택 요청 코드 상수
    }
}
