package com.avi.firebaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")

        textView7.text = "User ID : $userId"
        textView8.text = "Email ID : $emailId"
        
        btn_logout.setOnClickListener {
            
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@MainActivity,LoginActivity::class.java))
            
        }
    }
}