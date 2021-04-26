package com.example.instra_clone.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instra_clone.MainActivity
import com.example.instra_clone.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LogActivity : AppCompatActivity() {
    var isStarted = false
    var progressStatus = 0
    var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)




        binding.textViewRegister.setOnClickListener {
            startActivity(Intent(this, SigninActivity::class.java))
        }

        binding.buttonSignIn.setOnClickListener {

            val email=binding.textEmail.text.toString()
            val password=binding.editTextPassword.text.toString()


            when{
                 TextUtils.isEmpty(email)-> Toast.makeText(this,"email fill", Toast.LENGTH_LONG)
                 TextUtils.isEmpty(password)-> Toast.makeText(this,"password fill", Toast.LENGTH_LONG)

                else->{

                    handler = Handler(Handler.Callback {
                        if (isStarted) {
                            progressStatus++
                        }
                        binding.progressbar.setVisibility(View.VISIBLE);
                        binding.progressbar.progress = progressStatus
                        //  textViewHorizontalProgress.text = "${progressStatus}/${progressBarHorizontal.max}"
                        handler?.sendEmptyMessageDelayed(0, 100)

                        true
                    })

                    handler?.sendEmptyMessage(0)



                    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                    mAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information

                                startActivity(Intent(this,
                                    MainActivity::class.java))


                            } else {
                                val message=task.exception!!.toString()
                                // If sign in fails, display a message to the user.
                                // Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(baseContext, "Authentication vvv  failed."+message,
                                    Toast.LENGTH_SHORT).show()
                                mAuth.signOut()

                            }
                        }

                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser!=null){
            startActivity(Intent(this,
                MainActivity::class.java))

        }
    }


}