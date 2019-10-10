package dominando.android.basico

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.parceler.Parcels

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*val edtText = findViewById<EditText>(R.id.editTexto)
        val button = findViewById<Button>(R.id.buttonToast)

        button.setOnClickListener {
            val texto = edtText.text.toString()
            Toast.makeText(this, texto, Toast.LENGTH_SHORT).show()
        }*/

        buttonToast.setOnClickListener {
            val texto = editTexto.text.toString()
            Toast.makeText(this, texto, Toast.LENGTH_SHORT).show()
        }

        buttonTela2.setOnClickListener {
            val intent = Intent(this, Tela2Activity::class.java)
            intent.putExtra("nome", "Felipe")
            intent.putExtra("idade", 30)
            startActivity(intent)
        }

        buttonParcel.setOnClickListener {
            val cliente = Cliente(1, "Felipe")
            val intent = Intent(this, Tela2Activity::class.java)
//            intent.putExtra("cliente", cliente) obj Parcelable
            intent.putExtra("cliente", Parcels.wrap(cliente))
            startActivity(intent)
        }

        buttonSerializable.setOnClickListener {
            val intent = Intent(this, Tela2Activity::class.java)
            intent.putExtra("pessoa", Pessoa("Felipe", 35))
            startActivity(intent)
        }

        Log.i("FLMWG", "T1 - onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.i("FLMWG", "T1 - onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("FLMWG", "T1 - onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("FLMWG", "T1 - onRestart")
    }

    override fun onPause() {
        super.onPause()
        Log.i("FLMWG", "T1 - onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("FLMWG", "T1 - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("FLMWG", "T1 - onDestroy")
    }
}
