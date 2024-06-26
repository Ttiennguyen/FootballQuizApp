package com.example.quizonline

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizonline.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var  quizModelList : MutableList<QuizModel>
    lateinit var adapter: QuizlistAdapter
    lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        quizModelList = mutableListOf()
        getDataFromFirebase()

        auth = Firebase.auth

        displayUsername()

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    // Xử lý khi chọn mục "Home"
                    // Ví dụ: Chuyển sang màn hình MainActivity
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
                    true
                }
                R.id.profile -> {
                    // Xử lý khi chọn mục "Profile"
                    // Ví dụ: Chuyển sang màn hình ProfileActivity
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                // Thêm các trường hợp khác tương ứng với các mục trên BottomNavigationView nếu cần
                else -> false
            }
        }


        binding.signout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    private fun displayUsername(){
        val currentUser = auth.currentUser
        val userId = currentUser?.uid
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(userId)
            userRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val userName = document.getString("name")
                        if (userName != null) {
                            // Hiển thị tên của người dùng trong giao diện
                            binding.username.text = "Hello, $userName!"
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // Xử lý nếu có lỗi xảy ra khi đọc dữ liệu từ Firestore
                    // Ví dụ: Hiển thị thông báo lỗi
                    Toast.makeText(this, "Failed to retrieve user data: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setupRecyclerView(){
        binding.progressBar.visibility = View.GONE
        adapter = QuizlistAdapter(quizModelList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    // kết nối thẳng vs firebase thông qua nút không cần code lại check dữ liệu phải trùng vs đặt trên firebase
    private fun getDataFromFirebase(){
        binding.progressBar.visibility = View.VISIBLE
        //dumy data
        FirebaseDatabase.getInstance().reference
            .get()
            .addOnSuccessListener { dataSnapshot->
                if (dataSnapshot.exists()){
                    for (snapshot in dataSnapshot.children){
                        val quizModel = snapshot.getValue(QuizModel::class.java)
                        if (quizModel != null) {
                            quizModelList.add(quizModel)
                        }
                    }
                }
                setupRecyclerView()
            }
    }
}