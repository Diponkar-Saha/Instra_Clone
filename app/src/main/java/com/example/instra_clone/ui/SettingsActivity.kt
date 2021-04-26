package com.example.instra_clone.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.instra_clone.R
import com.example.instra_clone.ui.auth.LogActivity
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class SettingsActivity : AppCompatActivity() {
    private lateinit var logOut_settings: Button
    private lateinit var profile_image: CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        logOut_settings=findViewById(R.id.logOut_settings)
        profile_image=findViewById(R.id.profile_image)

        logOut_settings.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,LogActivity::class.java))

        }

    }




}
