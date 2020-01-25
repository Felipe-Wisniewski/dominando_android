package dominando.android.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSimple.setOnClickListener {
            NotificationUtils.notificationSimple(this)
        }

        btnTapAction.setOnClickListener {
            NotificationUtils.notificationWithTapAction(this)
        }

        btnBigText.setOnClickListener {
            NotificationUtils.notificationBigText(this)
        }

        btnActionButton.setOnClickListener {
            NotificationUtils.notificationWithButtonAction(this)
        }

        btnDirectReply.setOnClickListener {
            NotificationUtils.notificationAutoReply(this)
        }

        btnInbox.setOnClickListener {
            NotificationUtils.notificationInbox(this)
        }

        btnHeadsUp.setOnClickListener {
            NotificationUtils.notificationHeadsUp(this)
        }
    }
}
