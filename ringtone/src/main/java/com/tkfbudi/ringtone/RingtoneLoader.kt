package com.tkfbudi.ringtone

import android.content.Context
import android.database.Cursor
import android.media.RingtoneManager
import android.net.Uri
import android.os.AsyncTask
import java.lang.ref.WeakReference

/**
 * Created on : 22/09/18
 * Author     : Taufik Budi S
 * GitHub     : https://github.com/tfkbudi
 */
class RingtoneLoader(private val context: WeakReference<Context>?, private var listener: RingtoneListener?) : AsyncTask<Void, Void, MutableList<Ringtone>>() {

    fun detach() {
        listener = null
    }

    override fun onPreExecute() {
        super.onPreExecute()
        listener?.showLoading()
    }

    override fun doInBackground(vararg p0: Void?): MutableList<Ringtone> {
        val ringtones: MutableList<Ringtone> = ArrayList()
        val ringtoneManager = RingtoneManager(context?.get())
        ringtoneManager.setType(RingtoneManager.TYPE_NOTIFICATION)

        val cursor: Cursor = ringtoneManager.cursor
        while (cursor.moveToNext()) {
            ringtones.add(Ringtone(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX),
                    Uri.parse(cursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/"
                            + cursor.getString(RingtoneManager.ID_COLUMN_INDEX))))

        }

        return ringtones
    }

    override fun onPostExecute(ringtones: MutableList<Ringtone>) {
        super.onPostExecute(ringtones)
        listener?.onLoadFinish(ringtones)
        listener?.hideLoading()

    }

    interface RingtoneListener {
        fun showLoading()
        fun onLoadFinish(ringtones: MutableList<Ringtone>)
        fun hideLoading()
    }
}