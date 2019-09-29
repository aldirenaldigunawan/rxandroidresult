package com.kidnapsteal

import io.reactivex.Observable

interface RxPermission {
    fun checkAndRequest(vararg permissions: Permission): Observable<List<PermissionResult>>
}
