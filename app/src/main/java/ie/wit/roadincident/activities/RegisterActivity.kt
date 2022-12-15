package ie.wit.roadincident.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import ie.wit.roadincident.R
import ie.wit.roadincident.databinding.ActivityRegisterBinding
import ie.wit.roadincident.models.IncidentModel
import ie.wit.roadincident.models.UserModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ie.wit.roadincident.databinding.ActivityIncidentBinding


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    var user = UserModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_register)


        findViewById<Button>(R.id.loginButtonRegister).setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        findViewById<Button>(R.id.buttonRegister).setOnClickListener {
            user.userName = findViewById<TextView>(R.id.userName).text.toString()
            user.userPassword = findViewById<TextView>(R.id.userPassword).text.toString()

            when {
                TextUtils.isEmpty(user.userName.trim { it <= ' '}) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please Enter Email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(user.userPassword.trim { it <= ' '}) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please Enter Password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val email: String = user.userName.trim { it <= ' '}
                    val password: String = user.userPassword.trim { it <= ' '}

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->
                                if (task.isSuccessful) {
                                    val firebaseUser: FirebaseUser = task.result!!.user!!
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "You are Registered!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent = Intent(this@RegisterActivity, IncidentListActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id", firebaseUser.uid)
                                    intent.putExtra("email_id", email)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    //If user fails to register
                                    Toast.makeText(
                                        this@RegisterActivity,
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