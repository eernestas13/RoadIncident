package ie.wit.roadincident.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import ie.wit.roadincident.R
import ie.wit.roadincident.databinding.ActivityLoginBinding
import ie.wit.roadincident.models.UserModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    var user = UserModel()
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleLogin: ImageView
    private lateinit var firebaseAuth: FirebaseAuth

    private companion object{
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(R.layout.activity_login)


            findViewById<Button>(R.id.registerButtonLogin).setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
//                startActivity(intent)
//                finish()
            }

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)
        googleLogin = findViewById(R.id.googleLogin)
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding.googleLogin.setOnClickListener {
            Log.d(TAG, "onCreate: google sign in start")
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }

        googleLogin.setOnClickListener() {
            googleSignIn();
        }



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


    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null) {
            startActivity(Intent(this@LoginActivity, IncidentListActivity::class.java))
            finish()
        }
    }
    private fun googleSignIn(){
        val signInIntent = googleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent,1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode== RC_SIGN_IN) {
            Log.d(TAG,"onActivityResult : google signin intent")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //Google Signin complete, ...signin with firebase
               val googleUser = task.getResult(ApiException::class.java)!!
                navigateToIncidentListActivity(googleUser) //.idToken!!
            } catch (e: ApiException) {
                //failed sigin
                Log.d(TAG,"onActivityResult: ${e.message}")
                Toast.makeText(applicationContext,"Something Wrong!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToIncidentListActivity(googleUser: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(googleUser!!.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                //Successful Login
                Log.d(TAG,"navigateToIncidentListActivity: Logged In")
                //Get User
                val firebaseUser = firebaseAuth.currentUser
                val uid = firebaseAuth!!.uid
                val email = firebaseUser!!.email
                startActivity(Intent(this@LoginActivity, IncidentListActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@LoginActivity, "Logged in Failed.. Error:${e.message}", Toast.LENGTH_SHORT).show()
            }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}