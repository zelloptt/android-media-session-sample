package com.zello.mediasessionsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zello.mediasessionsample.headset.MediaSession
import com.zello.mediasessionsample.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    private var session: MediaSession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
        session = MediaSession().also { it.start(this) }
    }

    override fun onDestroy() {
        super.onDestroy()
        session?.stop()
        session = null
    }

}
