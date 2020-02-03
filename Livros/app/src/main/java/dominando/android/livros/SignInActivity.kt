package dominando.android.livros

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dominando.android.livros.booklist.BookListActivity
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    private var googleSignInClient: GoogleSignInClient? = null
//    private var googleApiClient: GoogleApiClient? = null
    private var fbAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        initGoogleSignIn()

        btnSignIn.setOnClickListener {
            signIn()
        }
    }

    private fun initGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        /*googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this) { showErrorSignIn() }
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()*/
    }

    private fun signIn() {
//        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN) {
//            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            val result = GoogleSignIn.getSignedInAccountFromIntent(data)

            if (result.isSuccessful) {
//                val account = result.signInAccount
                try {
                    val account = result.getResult(ApiException::class.java)

                    if (account != null) {
                        firebaseAuthWithGoogle(account)
                    }

                } catch (apiException: ApiException) {
                    showErrorSignIn()
                }

            } else {
                showErrorSignIn()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

        fbAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    finish()
                    startActivity(Intent(this, BookListActivity::class.java))

                } else {
                    showErrorSignIn()
                }
            }
    }

    private fun showErrorSignIn() {
        Toast.makeText(this, R.string.error_google_sign_in, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val RC_GOOGLE_SIGN_IN = 1
    }
}
