package ie.wit.roadincident.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ie.wit.roadincident.R
import ie.wit.roadincident.databinding.ActivityIncidentBinding
import ie.wit.roadincident.databinding.ActivityLoginBinding
import ie.wit.roadincident.models.UserModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    var user = UserModel()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(R.layout.activity_login)

            findViewById<Button>(R.id.buttonLogin).setOnClickListener {
                user.userName = findViewById<TextView>(R.id.userName).text.toString()
                user.userPassword = findViewById<TextView>(R.id.userPassword).text.toString()
                when {
                    TextUtils.isEmpty(user.userName.trim { it <= ' '}) -> {
                        Toast.makeText(
                            this@LoginActivity,
                            "Please Enter Email.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    TextUtils.isEmpty(user.userPassword.trim { it <= ' '}) -> {
                        Toast.makeText(
                            this@LoginActivity,
                            "Please Enter Password.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        val email: String = user.userName.trim { it <= ' '}
                        val password: String = user.userPassword.trim { it <= ' '}

                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(
                                OnCompleteListener<AuthResult> { task ->
                                    if (task.isSuccessful) {
                                        val firebaseUser: FirebaseUser = task.result!!.user!!
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "You are Logged In!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        val intent = Intent(this@LoginActivity, IncidentListActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                                        intent.putExtra("email_id", email)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        //If user fails to register
                                        Toast.makeText(
                                            this@LoginActivity,
                                            task.exception!!.message.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            )

                    }
                }
            }
        }
    }