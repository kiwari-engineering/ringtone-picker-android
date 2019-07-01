package com.tkfbudi.ringtone

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.qringtone_dialog.*
import java.lang.ref.WeakReference

/**
 * Created on : 22/09/18
 * Author     : Taufik Budi S
 * GitHub     : https://github.com/tfkbudi
 */

class QRingtoneDialog : DialogFragment(), RingtoneLoader.RingtoneListener, RingtoneAdapter.RingtoneClickedListener {
    private lateinit var adapter: RingtoneAdapter
    private lateinit var loader: RingtoneLoader
    private lateinit var player: RingtoneMediaPlayer
    private var ringtones: MutableList<Ringtone> = ArrayList()
    private var listener: QRingtoneListener? = null
    private lateinit var ringtone: Ringtone

    companion object {
        const val ARG_TITLE: String = "qringtone_title"
        const val ARG_POSITIVE_BUTTON = "qringtone_positive_button"
        const val ARG_NEGATIVE_BUTTON = "qringtone_negative_button"
        const val ARG_CURRENT_RINGTONE = "qringtone_current"
        const val ARG_DEFAULT_RINGTONE = "qringtone_default"
        const val ARG_SILENT_RINGTONE = "qringtone_silent"

        fun instance(fragmentManager: FragmentManager,
                     title: String,
                     positiveButton: String,
                     negativeButton: String,
                     currentRingtone: String?,
                     qRingtoneListener: QRingtoneListener?,
                     defaultRingtone: Boolean,
                     silentRingtone: Boolean) {

            val bundle = Bundle()
            bundle.putString(ARG_TITLE, title)
            bundle.putString(ARG_POSITIVE_BUTTON, positiveButton)
            bundle.putString(ARG_NEGATIVE_BUTTON, negativeButton)
            bundle.putString(ARG_CURRENT_RINGTONE, currentRingtone)
            bundle.putBoolean(ARG_DEFAULT_RINGTONE, defaultRingtone)
            bundle.putBoolean(ARG_SILENT_RINGTONE, silentRingtone)

            val dialog = QRingtoneDialog()
            dialog.retainInstance = true
            dialog.arguments = bundle
            dialog.setListener(qRingtoneListener)
            dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.qringtone_style)
            dialog.show(fragmentManager, QRingtoneDialog::class.simpleName)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = RingtoneAdapter(ringtones, this)
        loader = RingtoneLoader(WeakReference<Context>(context), this)
        player = RingtoneMediaPlayer(context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val myView = inflater.inflate(R.layout.qringtone_dialog, container, false)
        dialog.setTitle(arguments?.getString(ARG_TITLE))
        return myView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initRecyclerView()
        loader.execute()
        btnCancel.setOnClickListener { dismiss() }
        btnSelect.setOnClickListener { itemSelected() }
    }

    fun setListener(listener: QRingtoneListener?) {
        this.listener = listener
    }

    private fun initView() {
        btnCancel.text = arguments?.getString(ARG_NEGATIVE_BUTTON)
        btnSelect.text = arguments?.getString(ARG_POSITIVE_BUTTON)
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false)
        rvRingtone.layoutManager = layoutManager
        rvRingtone.adapter = adapter
    }

    override fun onDetach() {
        super.onDetach()
        loader.detach()
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onLoadFinish(ringtones: MutableList<Ringtone>) {
        arguments?.getBoolean(ARG_DEFAULT_RINGTONE)?.let {
            if(it) this.ringtones.add(getDefaultRingtone())
        }

        arguments?.getBoolean(ARG_SILENT_RINGTONE)?.let {
            if(it) this.ringtones.add(getSilentRingtone())
        }

        this.ringtones.addAll(ringtones)
        adapter.notifyDataSetChanged()

        val position = getCurrentRingtonePosition(this.ringtones)
        adapter.setLastPosition(position)
        rvRingtone.scrollToPosition(position)
    }

    private fun itemSelected() {
        if (this::ringtone.isInitialized) {
            listener?.onItemRingtoneSelected(ringtone)
        }
        dismiss()
    }

    private fun getCurrentRingtonePosition(ringtones: List<Ringtone>): Int {
        val currentRingtone = arguments?.get(ARG_CURRENT_RINGTONE) ?: return -1

        for ((i, ringtone) in ringtones.withIndex()) {
            if (currentRingtone == ringtone.uri.toString()) {
                this.ringtone = ringtone
                return i
            }
        }
        return -1
    }

    override fun onItemClicked(ringtone: Ringtone) {
        player.playRingtone(ringtone)
        this.ringtone = ringtone
    }


    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        player.detach()
    }

    fun getDefaultRingtone(): Ringtone {
//        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        return Ringtone("Default", uri)
    }

    fun getSilentRingtone(): Ringtone {
        return Ringtone("Silent", Uri.EMPTY)
    }


}