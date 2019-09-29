package com.kidnapsteal

import androidx.fragment.app.FragmentActivity
import io.reactivex.Observable

class RxPermissionImpl(activity: FragmentActivity) : RxPermission {

    private val fragment by lazy {
        HeadlessPermissionFragment()
    }

    init {
        activity.supportFragmentManager add fragment
    }

    override fun checkAndRequest(vararg permissions: Permission): Observable<List<PermissionResult>> {
        return fragment.requestPermission(*permissions.map { it.permission }.toTypedArray())
    }
}