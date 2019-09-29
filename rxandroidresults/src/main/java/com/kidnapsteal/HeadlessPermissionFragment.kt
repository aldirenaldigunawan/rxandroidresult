package com.kidnapsteal

import android.content.Context
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

internal class HeadlessPermissionFragment : Fragment() {

    private lateinit var publishSubject: PublishSubject<List<PermissionResult>>
    private lateinit var permissionRequest: List<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        retainInstance = true
    }

    fun requestPermission(vararg permissions: String): Observable<List<PermissionResult>> {
        publishSubject = PublishSubject.create()

        permissionRequest = permissions.toList()

        requestPermissions(
            permissionRequest.toTypedArray(),
            REQUEST_PERMISSION
        )

        return publishSubject
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION) {
            val grantedPermission = PermissionResult.Granted(permissions.mapIndexed { index, permissionString ->
                Permission.get(permissionString) to grantResults[index]
            }.filter { it.second == PackageManager.PERMISSION_GRANTED }.map { it.first })

            val deniedPermission = PermissionResult.Denied(permissions.mapIndexed { index, permissionString ->
                Permission.get(permissionString) to grantResults[index]
            }.filter { it.second == PackageManager.PERMISSION_DENIED }.map { it.first })

            publishSubject.onNext(listOf(grantedPermission, deniedPermission))
            publishSubject.onComplete()
        }

    }

    companion object {
        const val REQUEST_PERMISSION = 0x98
    }

}