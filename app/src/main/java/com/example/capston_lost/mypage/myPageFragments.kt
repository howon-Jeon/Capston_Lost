package com.example.capston_lost.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.capston_lost.HomeFragment
import com.example.capston_lost.Key
import com.example.capston_lost.MainActivity2
import com.example.capston_lost.MyPageFragment
import com.example.capston_lost.R
import com.example.capston_lost.databinding.FragmentMypageBinding
import com.example.capston_lost.userlist.UserItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class myPageFragments : Fragment(R.layout.fragment_mypage) { // fragment_mypage 레이아웃을 사용하는 myPageFragments 클래스 선언

    private lateinit var binding: FragmentMypageBinding // FragmentMypageBinding 변수를 선언

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // onViewCreated 메소드 재정의
        super.onViewCreated(view, savedInstanceState) // 부모 클래스의 onViewCreated 호출
        binding = FragmentMypageBinding.bind(view) // binding 변수 초기화

        val currentUserId = Firebase.auth.currentUser?.uid ?: "" // 현재 사용자 ID를 가져옴, 없으면 빈 문자열
        val currentUserDB = Firebase.database.reference.child(Key.DB_USERS).child(currentUserId) // 현재 사용자 DB 참조를 가져옴

        currentUserDB.get().addOnSuccessListener { // 현재 사용자 DB에서 데이터를 가져옴
            val currentUserItem = it.getValue(UserItem::class.java) ?: return@addOnSuccessListener // UserItem 객체로 변환

            binding.usernameEditText.setText(currentUserItem.username) // usernameEditText에 사용자 이름 설정
            binding.descriptionEditText.setText(currentUserItem.description) // descriptionEditText에 설명 설정
        }

        binding.applyButton.setOnClickListener { // applyButton 클릭 리스너 설정
            val username = binding.usernameEditText.text.toString() // usernameEditText의 텍스트를 문자열로 가져옴
            val description = binding.descriptionEditText.text.toString() // descriptionEditText의 텍스트를 문자열로 가져옴

            if (username.isEmpty()) { // 사용자 이름이 비어 있으면
                Toast.makeText(context, "유저이름은 빈 값으로 두실 수 없습니다.", Toast.LENGTH_SHORT).show() // 토스트 메시지를 표시
                return@setOnClickListener // 클릭 리스너 종료
            }
            val user = mutableMapOf<String, Any>() // 변경할 사용자 정보를 저장할 맵 객체 생성
            user["username"] = username // 맵에 사용자 이름 추가
            user["description"] = description // 맵에 설명 추가
            currentUserDB.updateChildren(user) // 사용자 DB를 업데이트
        }

        binding.backButton.setOnClickListener { // backButton 클릭 리스너 설정
            Firebase.auth.signOut() // 사용자 로그아웃
            startActivity(Intent(context, MainActivity2::class.java)) // MainActivity2로 이동
            activity?.finish() // 현재 액티비티 종료
        }
    }
}