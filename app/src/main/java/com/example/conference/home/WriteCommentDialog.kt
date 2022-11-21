package com.example.conference.home

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.example.conference.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.dialog_comment.*

class WriteCommentDialog(val context: Context, val programKey : String) {
    private val dialog = Dialog(context)
    private val user = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    fun showDialog() {
        dialog.setContentView(R.layout.dialog_comment)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

        dialog.comment_write_ok_btn.setOnClickListener {
            if (dialog.comment_contents_tv.text.isNotEmpty()) {
                val commentList = ProgramCommentDTO()
                commentList.uid = user?.currentUser?.uid!!
                commentList.comment = dialog.comment_contents_tv.text.toString()
                commentList.timestamp = System.currentTimeMillis()
                commentList.email = user?.currentUser?.email!!

                updateComment(programKey,commentList)
            } else {
                Log.d("태그","1")
                Toast.makeText(context,"댓글을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.comment_close_btn.setOnClickListener {
            dialog.dismiss()
        }
    }
    private fun updateComment(programKey: String, commentList: ProgramCommentDTO) {
        db?.collection("program")?.document(programKey)
            .collection("comment").document()
            ?.set(commentList)
            ?.addOnSuccessListener {
                Toast.makeText(context,"댓글을 작성했습니다.",Toast.LENGTH_SHORT).show()
            }?.addOnFailureListener {
                Log.d("태그", "댓글 작성 에러")
            }
    }
}