package com.example.capston_lost

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capston_lost.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    var firestore : FirebaseFirestore? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        //setContentView(binding.root)
        setContentView(R.layout.activity_main)

        // 파이어스토어 인스턴스 초기화
        firestore = FirebaseFirestore.getInstance()



        //lost_recyclerview.adapter = RecyclerViewAdapter()
        //lost_recyclerview.layoutManager = LinearLayoutManager(this)


        binding.memberButton.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java )
            startActivity(intent)  // intent 실행
        }
        binding.loginButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java )
            startActivity(intent)  // intent 실행
        }
        //앱을 실행하면 앨범에 접근할 수 있는 접근권한을 물어보고 한번 허용하게 되면 앱이 설치 되어 있는 동안에는 더 이상 권한을 요청하지 않습니다.
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        // Person 클래스 ArrayList 생성성
        var activity_lost : ArrayList<lost_Item> = arrayListOf()

        init {  // telephoneBook의 문서를 불러온 뒤 Person으로 변환해 ArrayList에 담음
            firestore?.collection("activity_lost")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                activity_lost.clear()

                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(lost_Item::class.java)
                    activity_lost.add(item!!)
                }
                notifyDataSetChanged()
            }
        }

        // xml파일을 inflate하여 ViewHolder를 생성
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.lost_cardview, parent, false)
            return ViewHolder(view)
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        }

        // onCreateViewHolder에서 만든 view와 실제 데이터를 연결
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as ViewHolder).itemView

            //viewHolder.card_cat_content.text = activity_lost[position].card_cat_content
            //viewHolder.card_date_content.text = activity_lost[position].card_date_content
            //viewHolder.card_loc_content.text = activity_lost[position].card_loc_content

        }

        // 리사이클러뷰의 아이템 총 개수 반환
        override fun getItemCount(): Int {
            return activity_lost.size
        }
    }

}