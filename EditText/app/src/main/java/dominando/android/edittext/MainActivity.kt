package dominando.android.edittext

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        maskCep()

        edtPassword.setOnEditorActionListener { v, actionId, event ->
            if (v == edtPassword && EditorInfo.IME_ACTION_DONE == actionId) {
                registerUser()
            }
            false
        }

    }

    fun maskCep() {
        edtCep.addTextChangedListener(object : TextWatcher {
            var isUpdating = false

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    //      Quando o texto é alterado o onTextChanged é chamado
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    //          Essa flag evita a chamada infinita desse método
                if (isUpdating) {
                    isUpdating = false
                    return
                }

    //          Ao apagar o texto, a máscara é removida, então o posicionamento do cursor precisa saber
    //          se o texto atual tinha ou não máscara.
                val hasMask = s.toString().indexOf('.') > -1 || s.toString().indexOf('-') > -1

                var str = s.toString().filterNot { it == '.' || it == '-' }     //Remove o '.' e '-' da String

    //          Os parâmetros before e count dizem o temanho anterior e atual da String digitada, se
    //          count > before é porque está digitando, caso contrário, está apagando
                if (count > before) {

                    if (str.length > 5) {
    //                  Se tem mais de 5 caracteres (sem máscara) coloca o '.' e o '-'
                        str = "${str.substring(0,2)}.${str.substring(2,5)}-${str.substring(5)}"

                    } else if (str.length > 2) {
    //                  Se tem mais de 2, coloca só o ponto
                        str = "${str.substring(0,2)}.${str.substring(2)}"
                    }

                    isUpdating = true       //Seta a flag para evitar chamada infinita
                    edtCep.setText(str)     //Seta o novo texto
                    edtCep.setSelection(edtCep.text?.length ?: 0)   //Seta a posição do cursor

                } else {
                    isUpdating = true
                    edtCep.setText(str)

    //              Se estiver apagando posiciona o cursor no local correto, isso trata a deleção dos
    //              caracteres da máscara
                    edtCep.setSelection(Math.max(0, Math.min(if (hasMask) start - before else start, str.length)))
                }
            }
        })
    }

    fun registerUser() {

        val name = edtName.text.toString()
        val email = edtEmail.text.toString()
        val password = edtPassword.text.toString()
        var isValid = true

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.error = getString(R.string.msg_error_email)
            isValid = false

        } else {
            tilEmail.error = null
        }

        if (password != "123") {
            tilPassword.error = getString(R.string.msg_error_password)
            isValid = false

        } else {
            tilPassword.error = null
        }

        if (isValid) {
            Toast.makeText(this, getString(R.string.msg_success, name, email), Toast.LENGTH_LONG).show()
        }
    }
}

