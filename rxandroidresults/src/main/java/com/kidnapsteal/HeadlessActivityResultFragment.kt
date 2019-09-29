package com.kidnapsteal


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

internal class HeadlessActivityResultFragment : Fragment() {

    private val publishSubject by lazy { PublishSubject.create<ActivityResult>() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.let {
            val intent = it.getParcelable<Intent>(KEY_INTENT)
            val requestCode = it.getInt(KEY_REQUEST_CODE)
            startActivityForResult(intent, requestCode)

        }
        retainInstance = true
    }

    fun start(): Observable<ActivityResult> {
        return publishSubject
    }

    fun start(intent: Intent, requestCode: Int): Observable<ActivityResult> {
        startActivityForResult(intent, requestCode)
        return publishSubject
    }

    fun listen(requestCode: Int): Observable<ActivityResult> = publishSubject.filter { it.requestCode == requestCode }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        publishSubject.onNext(
            ActivityResult(
                requestCode, data
                    ?: Intent(), resultCode
            )
        )
    }

    companion object {
        const val KEY_REQUEST_CODE = "HeadlessFragment.Key.request.code"
        const val KEY_INTENT = "HeadlessFragment.Key.intent"

        fun createFragment(intent: Intent, requestCode: Int): HeadlessActivityResultFragment {
            val bundle = Bundle().apply {
                putParcelable(KEY_INTENT, intent)
                putInt(KEY_REQUEST_CODE, requestCode)
            }

            return HeadlessActivityResultFragment().apply { arguments = bundle }
        }
    }
}