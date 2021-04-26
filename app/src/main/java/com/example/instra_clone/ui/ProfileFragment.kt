package com.example.instra_clone.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.instra_clone.R
import com.example.instra_clone.model.Userr
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class ProfileFragment : Fragment() {
    private lateinit var imageView_pro: CircleImageView
    private lateinit var nameTextView_Pro: TextView
    private lateinit var bioTextView_Pro: TextView
    private lateinit var totalFollowTextView_Pro: TextView
    private lateinit var totalFollowerTextView_Pro: TextView
    private lateinit var edit_profile: Button
    private lateinit var profileId: String
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        imageView_pro = view.findViewById(R.id.profile_image)
        nameTextView_Pro = view.findViewById(R.id.nick_name)
        bioTextView_Pro = view.findViewById(R.id.bio_name)
        edit_profile = view.findViewById(R.id.edit_profile)
        totalFollowTextView_Pro = view.findViewById(R.id.total_following)
        totalFollowerTextView_Pro = view.findViewById(R.id.total_follower)


        firebaseUser = FirebaseAuth.getInstance().currentUser!!


        val pref = context?.getSharedPreferences("PREF", Context.MODE_PRIVATE)
        if (pref != null) {
            this.profileId = pref.getString("profileID", "none").toString()
        }

        if (profileId == firebaseUser.uid) {
            edit_profile.text = "Edit Profile"

        } else if (profileId != firebaseUser.uid) {
            checkFollowAndFollowing()
        }


        edit_profile.setOnClickListener {
            startActivity(Intent(this.activity, SettingsActivity::class.java))
        }

        getFollowers()
        getFollowing()
        userInfo()



        return view;
    }

    private fun checkFollowAndFollowing() {
        val followingRef = firebaseUser?.uid.let { it ->
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it.toString())
                .child("Following")

        }
        followingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(profileId).exists()) {
                    edit_profile.text = "Following"
                } else {
                    edit_profile.text = "Follow"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getFollowers(){
        val followerRef =  FirebaseDatabase.getInstance().reference
                .child("Follow").child(profileId)
                .child("Followers")

        followerRef.addValueEventListener(object:ValueEventListener{

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    totalFollowerTextView_Pro.text=snapshot.childrenCount.toString()
                }
            }
        })

    }

    private fun getFollowing(){
        val followerRef = FirebaseDatabase.getInstance().reference
                .child("Follow").child(profileId)
                .child("Following")

        followerRef.addValueEventListener(object:ValueEventListener{

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    totalFollowTextView_Pro.text=snapshot.childrenCount.toString()
                }
            }
        })

    }

    private fun userInfo(){
        val userRef=FirebaseDatabase.getInstance().reference.child("Users")

        userRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
//               if(context!=null){
//                   return
//               }
                if(snapshot.exists()){
                    var user=snapshot.getValue<Userr>(Userr::class.java)
                   // Picasso.get().load(user!!.getImage())
                    nameTextView_Pro.text=user!!.getName()
                    bioTextView_Pro.text=user!!.getEmail()
                   // nameTextView_Pro.text=user!!.getName()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }


    override fun onStop() {
        super.onStop()

        val pref=context?.getSharedPreferences("PREF",Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileID",firebaseUser.uid)
        pref?.apply()

    }

    override fun onPause() {
        super.onPause()

        val pref=context?.getSharedPreferences("PREF",Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileID",firebaseUser.uid)
        pref?.apply()
    }

    override fun onDestroy() {
        super.onDestroy()

        val pref=context?.getSharedPreferences("PREF",Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileID",firebaseUser.uid)
        pref?.apply()
    }
}