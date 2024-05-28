package com.example.capston_lost

import android.os.Bundle
import android.text.TextUtils
import android.text.TextUtils.replace
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.capston_lost.databinding.ActivityChatMainBinding
import com.example.capston_lost.userlist.UserFragment

class ChatMainActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityChatMainBinding
    private  val userFragment = UserFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.back -> {
                    return@setOnItemSelectedListener  true
                }
                R.id.chatroomList ->{
                    return@setOnItemSelectedListener  true
                }

                R.id.userList -> {
                    replaceFragment(userFragment)
                    return@setOnItemSelectedListener  true
                }


                else ->{
                    return@setOnItemSelectedListener false
                }

            }
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.frameLayout,fragment)
                commit()
            }
    }

}


