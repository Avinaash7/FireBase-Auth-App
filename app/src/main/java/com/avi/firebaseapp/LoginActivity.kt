package com.avi.firebaseapp

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var gso: GoogleSignInOptions
    val RC_SIGN_IN: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        textView5.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        btn_login.setOnClickListener {
            when {
                TextUtils.isEmpty(et_login_email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                            this@LoginActivity,
                            "Please Enter email. ",
                            Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(et_login_password.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                            this@LoginActivity,
                            "Please Enter password. ",
                            Toast.LENGTH_SHORT
                    ).show()

                }
                else -> {

                    val email: String = et_login_email.text.toString().trim { it <= ' ' }
                    val password: String = et_login_password.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->

                                if (task.isSuccessful) {

                                   // val firebaseUser: FirebaseUser = task.result!!.user!!

                                    Toast.makeText(
                                            this@LoginActivity, "You are logged in successfully!!",
                                            Toast.LENGTH_SHORT
                                    ).show()

                                    val intent =
                                            Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                                    intent.putExtra("email_id", email)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(
                                            this@LoginActivity,
                                            task.exception!!.message.toString(),
                                            Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                }
            }

        }
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)

        btn_google.setOnClickListener{

            signInGoogle()

        }

    }

    private fun signInGoogle(){
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int,data : Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RC_SIGN_IN){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult (task)
        }

    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try{
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)!!
            Toast.makeText(
                    this@LoginActivity, "You are logged in successfully!!",
                    Toast.LENGTH_SHORT
            ).show()
            updateUI(account)
        }catch (e: ApiException){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {

        val intent1 =
                Intent(this@LoginActivity, MainActivity::class.java)
        intent1.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        //intent.putExtra("user_id", )
        //intent.putExtra("email_id", email)
        startActivity(intent1)
        finish()
    }
}