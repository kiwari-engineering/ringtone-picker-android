package com.tkfbudi.ringtone

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.widget.Toast
import java.io.IOException

/**
 * Created on : 01/10/18
 * Author     : Taufik Budi S
 * GitHub     : https://github.com/tfkbudi
 */
class RingtoneMediaPlayer(val context: Context) {

    val player: MediaPlayer = MediaPlayer()

    fun playRingtone(ringtone: Ringtone) {
        if (player.isPlaying) {
            player.stop()
        }
        player.reset()

        if (ringtone.uri === Uri.EMPTY) {
            return
        }

        try {
            player.setDataSource(context, ringtone.uri)
            player.prepare()
            player.start()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

    }

    fun detach() {
        if(player.isPlaying) player.stop()
        player.release()
    }

}