package dominando.android.livros

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

abstract class BaseActivity : AppCompatActivity() {

    protected val fbAuth : FirebaseAuth = FirebaseAuth.getInstance()

    private var hasInitCaled = false

    private var authListener: FirebaseAuth.AuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser

        if (user != null) {
            if (!hasInitCaled) { //PARA O INIT SER CHAMADO APENAS UMA VEZ
                hasInitCaled = true
                init()
            }

        } else {
            finish()

            val it = Intent(this, SignInActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            startActivity(it)
        }
    }

    public override fun onStart() {
        super.onStart()
        fbAuth.addAuthStateListener(authListener)
    }

    public override fun onStop() {
        super.onStop()
        fbAuth.removeAuthStateListener(authListener)
    }

    protected abstract fun init()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.book_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_sign_out) {
            FirebaseAuth.getInstance().signOut()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}