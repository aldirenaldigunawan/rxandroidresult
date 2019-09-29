package com.kidnapsteal


sealed class PermissionResult(val permissions: List<Permission>) {
    data class Granted(val permission: List<Permission>) : PermissionResult(permission)
    data class Denied(val permission: List<Permission>) : PermissionResult(permission)
}