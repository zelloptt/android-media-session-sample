package com.zello.mediasessionsample.headset

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.KeyEvent
import androidx.media.session.MediaButtonReceiver

class MediaSession {

    private var session: MediaSessionCompat? = null
    private var player: SilencePlayer? = null
    private var callback: MediaSessionCompat.Callback? = null

    fun start(context: Context) {
        callback = object : MediaSessionCompat.Callback() {
            override fun onMediaButtonEvent(intent: Intent): Boolean {
                if (intent.action != Intent.ACTION_MEDIA_BUTTON) return false
                val event = intent.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT) ?: return false
                Model.onMediaKeyEvent(event)
                return true
            }
        }
        val mediaButtonReceiver = ComponentName(context.applicationContext, MediaButtonReceiver::class.java)
        session = MediaSessionCompat(context.applicationContext, "media session test", mediaButtonReceiver, null).also { session ->
            session.setCallback(callback)
            val mediaButtonIntent = Intent(Intent.ACTION_MEDIA_BUTTON).run {
                setClass(context.applicationContext, MediaButtonReceiver::class.java)
            }
            session.setMediaButtonReceiver(PendingIntent.getBroadcast(
                context.applicationContext, 0, mediaButtonIntent, if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) PendingIntent.FLAG_IMMUTABLE else 0))
            session.isActive = true;
            // Switch the session to "playing" state
            val playbackState = PlaybackStateCompat.Builder().run {
                setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_PAUSE)
                setState(PlaybackStateCompat.STATE_PLAYING, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0f)
            }.build()
            session.setPlaybackState(playbackState)
        }
        startPlayer(context)
    }

    fun stop() {
        session?.release()
        session = null
        callback = null
        stopPlayer()
    }

    private fun startPlayer(context: Context) {
        player = SilencePlayer(context).also {
            // Playing a dummy sound should steal an active media session from another app on Android O and newer
            it.playForever()
        }
    }

    private fun stopPlayer() {
        player?.release()
        player = null
    }

}
