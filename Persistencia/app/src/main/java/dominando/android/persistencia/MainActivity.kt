package dominando.android.persistencia

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder

@RuntimePermissions
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnRead.setOnClickListener {
            btnReadClick()
        }

        btnSave.setOnClickListener {
            btnSaveClick()
        }

        btnOpenPref.setOnClickListener {
            startActivity(Intent(this, ConfigActivity::class.java))
        }

        btnReadPref.setOnClickListener {
            readPref()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @OnPermissionDenied(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun showDeniedForExternal() {
        Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show()
    }

    private fun btnReadClick() {
        val type = rgType.checkedRadioButtonId

        when (type) {
            R.id.rbInternal -> loadFromInternal()
            R.id.rbExternalPriv -> loadFromExternalWithPermissionCheck(true)
            R.id.rbExternalPublic -> loadFromExternalWithPermissionCheck(false)
        }
    }

    private fun btnSaveClick() {
        val type = rgType.checkedRadioButtonId

        when (type) {
            R.id.rbInternal -> saveToInternal()
            R.id.rbExternalPriv -> saveToExternalWithPermissionCheck(true)
            R.id.rbExternalPublic -> saveToExternalWithPermissionCheck(false)
        }
    }

    private fun saveToInternal() {
        try {
            val fos = openFileOutput("arquivo.txt", Context.MODE_PRIVATE)
            save(fos)

        } catch (e: Exception) {
            Log.e("FLMWG", "Erro ao salvar o arquivo", e)
        }
    }

    private fun loadFromInternal() {
        try {
            val fis = openFileInput("arquivo.txt")
            load(fis)

        } catch (e: Exception) {
            Log.e("FLMWG", "Erro ao carregar o arquivo", e)
        }
    }

    @NeedsPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun saveToExternal(privateDir: Boolean) {
        val state = Environment.getExternalStorageState()
        if (state == Environment.MEDIA_MOUNTED) {
            val myDir = getExternalDir(privateDir)

            try {
                if (myDir?.exists() == false) {
                    myDir.mkdir()
                }

                val txtFile = File(myDir, "arquivo.txt")
                if (!txtFile.exists()) {
                    txtFile.createNewFile()
                }

                val fos = FileOutputStream(txtFile)
                save(fos)

            } catch (e: IOException) {
                Log.e("FLMWG", "Erro ao salvar o arquivo", e)
            }
        } else {
            Log.e("FLMWG", "Não é possível escrever no SD Card")
        }
    }

    @NeedsPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    fun loadFromExternal(privateDir: Boolean) {
        val state = Environment.getExternalStorageState()

        if (state == Environment.MEDIA_MOUNTED || state == Environment.MEDIA_MOUNTED_READ_ONLY) {
            val myDir = getExternalDir(privateDir)

            if (myDir?.exists() == true) {
                val txtFile = File(myDir, "arquivo.txt")

                if (txtFile.exists()) {
                    try {
                        txtFile.createNewFile()
                        val fis = FileInputStream(txtFile)
                        load(fis)

                    } catch (e: IOException) {
                        Log.e("FLMWG", "Erro ao carregar arquivo", e)
                    }
                }
            }
        } else {
            Log.e("FLMWG", "SD Card indisponível")
        }
    }

    private fun getExternalDir(privateDir: Boolean) =
        if (privateDir) getExternalFilesDir(null)   //SDCard/Android/data/pacote.da.app/files
        else Environment.getExternalStorageDirectory()    //SDCard/DCIM

    private fun save(fos: FileOutputStream) {
        val lines = TextUtils.split(edtText.text.toString(), "\n")
        val writer = PrintWriter(fos)

        for (line in lines) {
            writer.println(line)
        }

        writer.flush()
        writer.close()
        fos.close()
    }

    private fun load(fis: FileInputStream) {
        val reader = BufferedReader(InputStreamReader(fis))
        val sb = StringBuilder()

        do {
            val line = reader.readLine() ?: break
            if (sb.isNotEmpty()) sb.append('\n')

            sb.append(line)
        } while (true)

        reader.close()
        fis.close()

        txtText.text = sb.toString()
    }

    private fun readPref() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val city = prefs.getString(getString(R.string.pref_city), getString(R.string.pref_city_default))
        val socialNetwork = prefs.getString(getString(R.string.pref_social_network), getString(R.string.pref_social_network_default))
        val messages = prefs.getBoolean(getString(R.string.pref_messages), false)

        val msg = String.format("%s = %s\n%s = %s\n%s = %s",
            getString(R.string.title_city), city,
            getString(R.string.title_social_network), socialNetwork,
            getString(R.string.title_messages), messages.toString())

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
