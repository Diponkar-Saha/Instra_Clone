package com.example.instra_clone.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instra_clone.MainActivity
import com.example.instra_clone.R
import com.example.instra_clone.databinding.ActivitySigninBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SigninActivity : AppCompatActivity() {

    private lateinit var userProfilePhoto: ImageView
    private lateinit var binding: ActivitySigninBinding

    companion object{
        val IMAGE_REQUEST_CODE=100
    }

    var isStarted = false
    var progressStatus = 0
    var handler: Handler? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)



        userProfilePhoto=findViewById(R.id.user_profile_photo)
        val mAuth:FirebaseAuth= FirebaseAuth.getInstance()


        userProfilePhoto.setOnClickListener {
            val intent=Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,
                IMAGE_REQUEST_CODE
            )
        }

//        Glide.with(this)
//            .load(userProfilePhoto)
//            .fitCenter()
//            .circleCrop()
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .into(userProfilePhoto)







        binding.textViewRegister.setOnClickListener {
            startActivity(Intent(this, LogActivity::class.java))
        }

        binding.buttonSignUp.setOnClickListener {
            val name=binding.textName.text.toString()
            val email=binding.textEmail.text.toString()
            val password=binding.editTextPassword.text.toString()


            when{
                TextUtils.isEmpty(name)->{
                    Toast.makeText(this,"name fill",Toast.LENGTH_LONG).show()
                }
                TextUtils.isEmpty(email)->{
                    Toast.makeText(this,"email fill",Toast.LENGTH_LONG).show()
                }
                TextUtils.isEmpty(password)->{
                    Toast.makeText(this,"password fill",Toast.LENGTH_LONG).show()
                }

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



                  //  val mAuth:FirebaseAuth= FirebaseAuth.getInstance()
                    mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                saveUserinfo(name,email,password)

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



    private fun saveUserinfo(name: String, email: String, password: String) {
        val currentUserID=FirebaseAuth.getInstance().currentUser!!.uid
        val userRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
        val userMap=HashMap<String,Any>()
        userMap["uid"]=currentUserID
        userMap["name"]=name.toLowerCase()
        userMap["email"]=email
        userMap["pass"]=password
        userRef.child(currentUserID).setValue(userMap)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){

                    Toast.makeText(this,"Account Created",Toast.LENGTH_LONG)
                    startActivity(Intent(this,
                        MainActivity::class.java))
                }else{
                    val message=task.exception!!.toString()
                    // If sign in fails, display a message to the user.
                    // Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed. "+message,
                        Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut()
                }

            }



    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== IMAGE_REQUEST_CODE &&resultCode== Activity.RESULT_OK){
            userProfilePhoto.setImageURI(data?.data)
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



