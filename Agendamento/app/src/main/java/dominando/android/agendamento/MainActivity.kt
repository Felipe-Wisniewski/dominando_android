package dominando.android.agendamento

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val wm = WorkManager.getInstance()
    private var workId: UUID? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnOneTime.setOnClickListener {
            val input = Data.Builder()
                .putString(MyWork.PARAM_FIRST_NAME, "Felipe")
                .build()

            val request = OneTimeWorkRequest.Builder(MyWork::class.java)
                .setInputData(input)
                .build()

            observeAndEnqueue(request)
        }

        btnPeriodic.setOnClickListener {
            val input = Data.Builder()
                .putString(MyWork.PARAM_FIRST_NAME, "Felipe")
                .build()

            val request = PeriodicWorkRequest.Builder(MyWork::class.java, 5, TimeUnit.MINUTES)
                .setInputData(input)
                .build()

            observeAndEnqueue(request)
        }

        btnStop.setOnClickListener {
            workId?.let { uuid ->
                wm.cancelWorkById(uuid)
            }
        //  wm.cancelAllWork()
        }
    }

    private fun observeAndEnqueue(request: WorkRequest) {
        wm.enqueue(request)
        workId = request.id

        wm.getWorkInfoByIdLiveData(request.id).observe(this, Observer { status ->
            txtStatus.text = when (status?.state) {
                WorkInfo.State.ENQUEUED -> "Enfileirado"
                WorkInfo.State.BLOCKED -> "Bloqueado"
                WorkInfo.State.CANCELLED -> "Cancelado"
                WorkInfo.State.RUNNING -> "Executando"
                WorkInfo.State.SUCCEEDED -> "Sucesso"
                WorkInfo.State.FAILED -> "Falhou"
                else -> "Indefinido"
            }

            txtOutput.text = status?.outputData?.run {
                """${getString(MyWork.PARAM_NAME)}
                    |${getInt(MyWork.PARAM_AGE, 0)}
                    |${getLong(MyWork.PARAM_TIME, 0)}""".trimMargin()
            }
        })
    }
}
