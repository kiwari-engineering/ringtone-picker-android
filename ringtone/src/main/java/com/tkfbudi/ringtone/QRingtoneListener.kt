package com.tkfbudi.ringtone

import java.io.Serializable

/**
 * Created on : 07/10/18
 * Author     : Taufik Budi S
 * GitHub     : https://github.com/tfkbudi
 */
interface QRingtoneListener: Serializable {

    fun onItemRingtoneSelected(ringtone: Ringtone)
}