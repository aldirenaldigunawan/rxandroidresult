package com.kidnapsteal

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

internal infix fun FragmentManager.add(fragment: Fragment) {
    beginTransaction()
        .add(fragment, fragment::class.java.simpleName)
        .commitNow()
}