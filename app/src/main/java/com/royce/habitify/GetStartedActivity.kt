package com.royce.habitify

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class GetStartedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)

        // Login button → Go to LoginActivity
        findViewById<MaterialButton>(R.id.btn_login).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Sign Up button → Go to RegisterActivity
        findViewById<MaterialButton>(R.id.btn_signup).setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // (Optional) Google login
        findViewById<MaterialButton>(R.id.btn_google).setOnClickListener {
            // TODO: Implement Google login
            android.widget.Toast.makeText(this, "Google login coming soon!", android.widget.Toast.LENGTH_SHORT).show()
        }

        // (Optional) Facebook login
        findViewById<MaterialButton>(R.id.btn_facebook).setOnClickListener {
            // TODO: Implement Facebook login
            android.widget.Toast.makeText(this, "Facebook login coming soon!", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
}
