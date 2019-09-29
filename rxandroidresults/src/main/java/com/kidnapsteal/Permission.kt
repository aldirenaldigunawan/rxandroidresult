package com.kidnapsteal


import android.Manifest

sealed class Permission(val permission: String) {
    object Camera : Permission(Manifest.permission.CAMERA)
    object ReadContact : Permission(Manifest.permission.READ_CONTACTS)
    object WriteContact : Permission(Manifest.permission.WRITE_CONTACTS)
    object ReadStorage : Permission(Manifest.permission.READ_EXTERNAL_STORAGE)
    object WriteStorage : Permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    object Location : Permission(Manifest.permission.ACCESS_FINE_LOCATION)

    companion object {
        fun get(permissionString: String): Permission {
            return when (permissionString) {
                Camera.permission -> Camera
                ReadContact.permission -> ReadContact
                WriteContact.permission -> WriteContact
                ReadStorage.permission -> ReadStorage
                WriteStorage.permission -> WriteStorage
                Location.permission -> Location
                else -> throw IllegalArgumentException("Unsupported permission")
            }
        }
    }
}