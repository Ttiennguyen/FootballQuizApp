package com.example.quizonline

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        // Ánh xạ BottomNavigationView từ layout XML
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigation)

        // Lắng nghe sự kiện chọn mục trên BottomNavigationView
        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    // Xử lý khi chọn mục "Home"
                    // Ví dụ: Chuyển sang màn hình MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.profile -> {
                    // Đã ở trong trang ProfileActivity, không cần chuyển đến chính nó nữa
                    true
                }
                // Thêm các trường hợp khác tương ứng với các mục trên BottomNavigationView nếu cần
                else -> false
            }
        }
    }
}