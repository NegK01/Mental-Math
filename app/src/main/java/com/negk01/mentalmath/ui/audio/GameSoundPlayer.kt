package com.negk01.mentalmath.ui.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.util.Log
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.GameSound
import java.util.concurrent.ConcurrentHashMap

class GameSoundPlayer(context: Context) {
    private val applicationContext = context.applicationContext
    private val audioManager = applicationContext.getSystemService(AudioManager::class.java)
    private val loadedSounds = ConcurrentHashMap.newKeySet<Int>()
    private var enabled = false
    private var foreground = false
    private var released = false
    private var activeStream = 0
    private val pool = runCatching {
        SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .build()
    }.onFailure { Log.w("GameSoundPlayer", "Unable to initialize audio", it) }.getOrNull()
    private val soundIds = mutableMapOf<GameSound, Int>()

    init {
        pool?.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) {
                loadedSounds.add(sampleId)
            } else {
                Log.w("GameSoundPlayer", "Unable to load audio sample $sampleId: $status")
            }
        }
        mapOf(
            GameSound.CORRECT to R.raw.feedback_correct,
            GameSound.INCORRECT to R.raw.feedback_incorrect,
            GameSound.TIMEOUT to R.raw.feedback_timeout,
            GameSound.PERSONAL_BEST to R.raw.feedback_personal_best
        ).forEach { (sound, resource) ->
            runCatching { pool?.load(applicationContext, resource, 1) ?: 0 }
                .onSuccess { sampleId -> if (sampleId != 0) soundIds[sound] = sampleId }
                .onFailure { Log.w("GameSoundPlayer", "Unable to load $sound", it) }
        }
    }

    fun setEnabled(value: Boolean) {
        enabled = value
        if (!value) stop()
    }

    fun setForeground(value: Boolean) {
        foreground = value
        if (!value) stop()
    }

    fun play(sound: GameSound) {
        if (released || !enabled || !foreground) return
        if ((audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: 0) == 0) return
        val sampleId = soundIds[sound] ?: return
        if (sampleId !in loadedSounds) return
        stop()
        activeStream = pool?.play(sampleId, 1f, 1f, 1, 0, 1f) ?: 0
    }

    fun stop() {
        if (!released && activeStream != 0) pool?.stop(activeStream)
        activeStream = 0
    }

    fun release() {
        if (released) return
        stop()
        released = true
        pool?.setOnLoadCompleteListener(null)
        pool?.release()
        loadedSounds.clear()
    }
}
