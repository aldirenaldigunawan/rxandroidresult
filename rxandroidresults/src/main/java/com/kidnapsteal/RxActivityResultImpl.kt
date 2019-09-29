package com.kidnapsteal

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import io.reactivex.Observable

class RxActivityResultImpl(private val activity: FragmentActivity) :
    RxActivityResult {

    private val fragment by lazy {
        HeadlessActivityResultFragment()
    }

    init {
        activity.supportFragmentManager add fragment
    }

    override fun startActivityForResult(
        intent: Intent,
        requestCode: Int
    ): Observable<ActivityResult> {

        val fragmentManager = activity.supportFragmentManager

        val headlessFragment =
            HeadlessActivityResultFragment.createFragment(intent, requestCode)

        fragmentManager.checkAndRemoveDuplicatedFragment()

        fragmentManager add headlessFragment

        return headlessFragment.start().onCompleteRemoveFragment(headlessFragment)

    }

    override fun startActivity(intent: Intent, requestCode: Int): Observable<ActivityResult> {
        return fragment.start(intent, requestCode).filter { it.requestCode == requestCode }
    }

    override fun listenToResult(requestCode: Int): Observable<ActivityResult> {
        return fragment.listen(requestCode)
    }

    private fun FragmentManager.checkAndRemoveDuplicatedFragment() {
        val prevFragment = findFragmentByTag(
            HeadlessActivityResultFragment::class.java.simpleName
        )

        prevFragment?.let { beginTransaction().remove(it).commit() }
    }


    private fun Observable<ActivityResult>.onCompleteRemoveFragment(
        fragment: Fragment
    ): Observable<ActivityResult> {
        return this.doOnComplete {
            activity.supportFragmentManager.beginTransaction().remove(fragment).commit()
        }
    }
}