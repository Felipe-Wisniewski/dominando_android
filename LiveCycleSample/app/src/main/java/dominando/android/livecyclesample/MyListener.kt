package dominando.android.livecyclesample

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class MyListener : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun iniciar() {
        Log.d("FLMWG", "fun iniciar")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun parar() {
        Log.d("FLMWG", "fun parar")
    }
}