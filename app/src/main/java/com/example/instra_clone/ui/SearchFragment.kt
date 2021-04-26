package com.example.instra_clone.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instra_clone.R
import com.example.instra_clone.adapter.UserAdapter
import com.example.instra_clone.model.Userr
import com.google.firebase.database.*

class SearchFragment : Fragment() {

    private var recyclerView: RecyclerView?=null
    private var userAdapter: UserAdapter?=null
    private var mUser: MutableList<Userr>?=null
    private lateinit var search_editText: EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_search, container, false)


        recyclerView=view.findViewById(R.id.recycleView_searche)
        search_editText=view.findViewById(R.id.search_edit)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager=LinearLayoutManager(context)

        mUser=ArrayList()
        userAdapter=context?.let { UserAdapter(it, mUser as ArrayList<Userr>) }
        recyclerView?.adapter=userAdapter

        search_editText.addTextChangedListener(object :TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }



            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(search_editText.text.toString()==""){
                    Toast.makeText(requireActivity(),"No data enter", Toast.LENGTH_LONG)


                }else{
                    recyclerView?.visibility=View.VISIBLE
                    retriveUsers()
                    searchUser(s.toString().toLowerCase())
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        return view
    }

    private fun searchUser(input: String) {
        val query=FirebaseDatabase.getInstance().reference
            .child("Users")
            .orderByChild("name")
            .startAt(input)
            .endAt(input + "\uf8ff")

        query.addValueEventListener( object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                mUser?.clear()
                    for(dataSnapshot in snapshot.children){
                        //searching all users
                        val user=dataSnapshot.getValue(Userr::class.java)
                        if(user!=null){
                            mUser?.add(user)
                        }
                    }
                     userAdapter?.notifyDataSetChanged()
                }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun retriveUsers() {
       val usersRef=FirebaseDatabase.getInstance().reference.child("Users")

       // val userRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
        usersRef.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                    mUser?.clear()
                    for(snapshot in dataSnapshot.children){
                        val user=snapshot.getValue(Userr::class.java)
                        val name = snapshot.child("name").value.toString()

                        val email = snapshot.child("email").value.toString()

                        val uid = snapshot.child("uid").value.toString()
                        Userr(email,name,  uid)
                        if(user!=null){
                            mUser?.add( Userr(email,name,uid))
                        }
                        userAdapter?.notifyDataSetChanged()
                    }


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Could not read from Database",Toast.LENGTH_LONG).show()
            }
        })
    }


}