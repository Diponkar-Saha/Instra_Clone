package com.example.instra_clone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.instra_clone.R
import com.example.instra_clone.model.Userr
import com.example.instra_clone.ui.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UserAdapter(
    private var mContext: Context,
    private var mUser: List<Userr>
):
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var firebaseUser:FirebaseUser?=FirebaseAuth.getInstance().currentUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(mContext).inflate(R.layout.user_item_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user=mUser[position]
        holder.userNameTextView.text=user.getName()
        holder.userBioTextView.text=user.getEmail()

        holder.itemView.setOnClickListener{
            val pref=mContext.getSharedPreferences("PREF",Context.MODE_PRIVATE).edit()
            pref.putString("profileID",user.getUid())
            pref.apply()

            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container,ProfileFragment()).commit()
        }


        checkFollowingStatus(user.getUid(),holder.userFollowButton)

        holder.userFollowButton.setOnClickListener {
            if(holder.userFollowButton.text.toString()=="Follow"){
                firebaseUser?.uid.let { it->
                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(it.toString())
                        .child("Following").child(user.getUid())
                        .setValue(true).addOnCompleteListener { task ->
                            if(task.isSuccessful){

                                firebaseUser?.uid.let { it->
                                    FirebaseDatabase.getInstance().reference
                                        .child("Follow").child(it.toString())
                                        .child("Followers").child(user.getUid())
                                        .setValue(true).addOnCompleteListener { task ->
                                            if(task.isSuccessful){

                                            }

                                        }
                                }

                            }

                        }
                }

            }else
            {
                firebaseUser?.uid.let { it->
                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(it.toString())
                        .child("Following").child(user.getUid())
                        .removeValue().addOnCompleteListener { task ->
                            if(task.isSuccessful){

                                firebaseUser?.uid.let { it->
                                    FirebaseDatabase.getInstance().reference
                                        .child("Follow").child(it.toString())
                                        .child("Followers").child(user.getUid())
                                        .removeValue().addOnCompleteListener { task ->
                                            if(task.isSuccessful){

                                            }

                                        }
                                }

                            }

                        }
                }

            }
        }
    }



    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView){
        var userNameTextView:TextView=itemView.findViewById(R.id.nick_name_user_l)
        var userBioTextView:TextView=itemView.findViewById(R.id.bio_name_user_l)
        var userFollowButton: Button =itemView.findViewById(R.id.follow_user_l)
    }

    private fun checkFollowingStatus(uid: String, userFollowButton: Button) {

        val followingRef= firebaseUser?.uid.let { it ->
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it.toString())
                .child("Following")
        }

        followingRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
               if(snapshot.child(uid).exists()){
                   userFollowButton.text="Following"
               }else{
                   userFollowButton.text="Follow"

               }
            }

        })
//"INSERT INTO registrations (firstName,lastName,gender,passwrd,number address) VALUES (%s, %s,%s, %s,%s, %s)"


    }


}