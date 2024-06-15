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

class FindReportpage : AppCompatActivity() { // FindReportpage 액티비티 클래스 정의

    private lateinit var firestore: FirebaseFirestore // Firestore 인스턴스를 나중에 초기화하기 위해 선언
    private lateinit var storageRef: StorageReference // StorageReference를 나중에 초기화하기 위해 선언
    private lateinit var storage: FirebaseStorage // FirebaseStorage 인스턴스를 나중에 초기화하기 위해 선언
    private lateinit var imageUriList: MutableList<Uri> // 이미지 URI 리스트를 나중에 초기화하기 위해 선언
    private lateinit var selectedImageView: ImageView // 선택된 이미지를 표시할 ImageView를 나중에 초기화하기 위해 선언
    private lateinit var uploadProgressBar: ProgressBar // 업로드 진행을 표시할 ProgressBar를 나중에 초기화하기 위해 선언
    private lateinit var uploadStatusTextView: TextView // 업로드 상태를 표시할 TextView를 나중에 초기화하기 위해 선언

    override fun onCreate(savedInstanceState: Bundle?) { // 액티비티가 생성될 때 호출되는 onCreate 메서드
        super.onCreate(savedInstanceState) // 슈퍼클래스의 onCreate 메서드를 호출
        setContentView(R.layout.activity_find_reportpage) // 레이아웃을 설정

        firestore = FirebaseFirestore.getInstance() // Firestore 인스턴스 초기화
        storage = FirebaseStorage.getInstance() // Firebase Storage 인스턴스 초기화
        imageUriList = mutableListOf() // 이미지 URI 리스트 초기화

        uploadProgressBar = findViewById(R.id.uploadProgressBar) // ProgressBar를 레이아웃에서 가져오기
        uploadStatusTextView = findViewById(R.id.uploadStatusTextView) // TextView를 레이아웃에서 가져오기

        val closeButton: Button = findViewById(R.id.closeBtn) // 닫기 버튼을 레이아웃에서 가져오기
        closeButton.setOnClickListener { // 닫기 버튼 클릭 리스너 설정
            finish() // 액티비티 종료
        }

        val submitButton: Button = findViewById(R.id.textViewSubmit) // 제출 버튼을 레이아웃에서 가져오기
        submitButton.setOnClickListener { // 제출 버튼 클릭 리스너 설정
            if (isFormValid()) { // 폼이 유효한지 확인
                uploadProgressBar.visibility = View.VISIBLE // ProgressBar를 보이게 설정
                uploadStatusTextView.visibility = View.VISIBLE // TextView를 보이게 설정
                saveReportToFirestore() // Firestore에 보고서 저장
            } else {
                Snackbar.make(submitButton, "모든 필드를 입력해주세요.", Snackbar.LENGTH_SHORT).show() // 폼이 유효하지 않으면 Snackbar 표시
            }
        }

        val editTextItemType: EditText = findViewById(R.id.editTextItemType) // 아이템 종류 입력 필드를 레이아웃에서 가져오기
        editTextItemType.setOnClickListener { // 아이템 종류 입력 필드 클릭 리스너 설정
            showItemTypeDialog() // 아이템 종류 다이얼로그 표시
        }

        val editTextGetDate: EditText = findViewById(R.id.editTextGetDate) // 날짜 입력 필드를 레이아웃에서 가져오기
        editTextGetDate.setOnClickListener { // 날짜 입력 필드 클릭 리스너 설정
            showDatePickerDialog() // 날짜 선택 다이얼로그 표시
        }

        selectedImageView = findViewById(R.id.selectedImageView) // 선택된 이미지를 표시할 ImageView를 레이아웃에서 가져오기
        val selectImageButton: ImageButton = findViewById(R.id.selectImageButton) // 이미지 선택 버튼을 레이아웃에서 가져오기
        selectImageButton.setOnClickListener { // 이미지 선택 버튼 클릭 리스너 설정
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI) // 이미지 선택을 위한 인텐트 생성
            intent.type = "image/*" // 이미지 타입 설정
            startActivityForResult(intent, REQUEST_IMAGE_PICK) // 이미지 선택 액티비티 시작
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // 이미지 선택 결과를 처리하는 메서드
        super.onActivityResult(requestCode, resultCode, data) // 슈퍼클래스의 onActivityResult 메서드 호출
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) { // 이미지 선택이 성공적으로 완료된 경우
            data?.data?.let { uri -> // 선택된 이미지의 URI를 가져옴
                imageUriList.add(uri) // 이미지 URI 리스트에 추가
                Glide.with(this).load(uri).into(selectedImageView) // 선택된 이미지를 ImageView에 표시
            }
        }
    }

    private fun showItemTypeDialog() { // 아이템 종류 선택 다이얼로그를 표시하는 메서드
        val itemTypes = arrayOf("가방", "옷", "지갑", "전자기기", "기타") // 아이템 종류 배열
        val builder = AlertDialog.Builder(this) // AlertDialog.Builder 생성
        builder.setTitle("물품 종류 선택") // 다이얼로그 제목 설정
        builder.setItems(itemTypes) { _, which -> // 다이얼로그 아이템 클릭 리스너 설정
            val selectedItemType = itemTypes[which] // 선택된 아이템 종류
            val editTextItemType: EditText = findViewById(R.id.editTextItemType) // 아이템 종류 입력 필드를 레이아웃에서 가져오기
            editTextItemType.setText(selectedItemType) // 선택된 아이템 종류를 입력 필드에 설정
        }
        builder.show() // 다이얼로그 표시
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val calendar = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateString = dateFormat.format(calendar.time)
            val editTextGetDate: EditText = findViewById(R.id.editTextGetDate)
            editTextGetDate.setText(dateString)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun saveReportToFirestore() { // Firestore에 보고서를 저장하는 메서드
        val editTextTitle: EditText = findViewById(R.id.editTextTitle) // 제목 입력 필드를 레이아웃에서 가져오기
        val editTextItemType: EditText = findViewById(R.id.editTextItemType) // 아이템 종류 입력 필드를 레이아웃에서 가져오기
        val editTextGetDate: EditText = findViewById(R.id.editTextGetDate) // 날짜 입력 필드를 레이아웃에서 가져오기
        val editTextLocation: EditText = findViewById(R.id.editTextLocation) // 위치 입력 필드를 레이아웃에서 가져오기
        val editTextRemarks: EditText = findViewById(R.id.editTextRemarks) // 비고 입력 필드를 레이아웃에서 가져오기

        val userId = FirebaseAuth.getInstance().currentUser?.uid // 현재 사용자의 ID 가져오기
        val title = editTextTitle.text.toString() // 제목 가져오기
        val itemType = editTextItemType.text.toString() // 아이템 종류 가져오기
        val getDate = editTextGetDate.text.toString() // 날짜 가져오기
        val location = editTextLocation.text.toString() // 위치 가져오기
        val remarks = editTextRemarks.text.toString() // 비고 가져오기

        if (userId != null) { // 사용자가 로그인되어 있는 경우
            firestore.collection("users").document(userId).get() // Firestore에서 사용자 정보 가져오기
                .addOnSuccessListener { document -> // 성공적으로 가져온 경우
                    if (document != null && document.exists()) { // 문서가 존재하는 경우
                        val userName = document.getString("name") ?: "Unknown" // 사용자 이름 가져오기
                        uploadImagesToStorage(userId) { imageUrls -> // 이미지들을 저장소에 업로드
                            val report = FoundItem( // FoundItem 객체 생성
                                title = title,
                                itemType = itemType,
                                getDate = getDate,
                                location = location,
                                remarks = remarks,
                                userId = userId,
                                imageUrl = imageUrls.joinToString(","), // 이미지 URL들을 콤마로 구분하여 문자열로 저장
                                nickname = userName
                            )

                            firestore.collection("found_reports") // Firestore의 "found_reports" 컬렉션에
                                .add(report) // 보고서 추가
                                .addOnSuccessListener {
                                    Toast.makeText(this, "습득 신고가 저장되었습니다.", Toast.LENGTH_SHORT).show() // 성공 메시지 표시
                                    uploadProgressBar.visibility = View.GONE // ProgressBar 숨기기
                                    uploadStatusTextView.visibility = View.GONE // TextView 숨기기
                                    finish() // 액티비티 종료
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "저장 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show() // 오류 메시지 표시
                                    uploadProgressBar.visibility = View.GONE // ProgressBar 숨기기
                                    uploadStatusTextView.visibility = View.GONE // TextView 숨기기
                                }
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "사용자 정보를 가져오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show() // 사용자 정보 가져오는 중 오류 메시지 표시
                    uploadProgressBar.visibility = View.GONE // ProgressBar 숨기기
                    uploadStatusTextView.visibility = View.GONE // TextView 숨기기
                }
        }
    }

    private fun isFormValid(): Boolean { // 폼이 유효한지 검사하는 메서드
        val editTextTitle: EditText = findViewById(R.id.editTextTitle) // 제목 입력 필드를 레이아웃에서 가져오기
        val editTextItemType: EditText = findViewById(R.id.editTextItemType) // 아이템 종류 입력 필드를 레이아웃에서 가져오기
        val editTextGetDate: EditText = findViewById(R.id.editTextGetDate) // 날짜 입력 필드를 레이아웃에서 가져오기
        val editTextLocation: EditText = findViewById(R.id.editTextLocation) // 위치 입력 필드를 레이아웃에서 가져오기
        val editTextRemarks: EditText = findViewById(R.id.editTextRemarks) // 비고 입력 필드를 레이아웃에서 가져오기

        return editTextTitle.text.isNotEmpty() && // 제목이 비어있지 않은지 확인
                editTextItemType.text.isNotEmpty() && // 아이템 종류가 비어있지 않은지 확인
                editTextGetDate.text.isNotEmpty() && // 날짜가 비어있지 않은지 확인
                editTextLocation.text.isNotEmpty() && // 위치가 비어있지 않은지 확인
                editTextRemarks.text.isNotEmpty() // 비고가 비어있지 않은지 확인
    }

    private fun uploadImagesToStorage(userId: String, onSuccess: (List<String>) -> Unit) { // 이미지를 저장소에 업로드하는 메서드
        val imageUrls = mutableListOf<String>() // 이미지 URL 리스트 초기화
        val storageRef = storage.reference.child("found_images").child(userId) // 저장소 레퍼런스 설정

        for (uri in imageUriList) { // 이미지 URI 리스트를 순회
            val fileRef = storageRef.child(System.currentTimeMillis().toString() + ".jpg") // 파일 레퍼런스 생성
            fileRef.putFile(uri).addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    imageUrls.add(downloadUri.toString()) // 업로드된 이미지 URL 리스트에 추가
                    if (imageUrls.size == imageUriList.size) {
                        onSuccess(imageUrls) // 모든 이미지 업로드가 완료되면 콜백 호출
                    }
                }
            }.addOnFailureListener {
                uploadProgressBar.visibility = View.GONE // 업로드 실패 시 ProgressBar 숨기기
                uploadStatusTextView.visibility = View.GONE // 업로드 실패 시 TextView 숨기기
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1 // 이미지 선택 요청 코드 상수 정의
    }
}
