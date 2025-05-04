package com.example.myapplication.utils

import android.util.Log
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object ZaloPayUtils {
    fun createMac(data: String, key: String): String {
        try {
            val algorithm = "HmacSHA256"
            val mac = Mac.getInstance(algorithm)
            val secretKeySpec = SecretKeySpec(key.toByteArray(Charsets.UTF_8), algorithm)
            mac.init(secretKeySpec)
            val hash = mac.doFinal(data.toByteArray(Charsets.UTF_8))
            return hash.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            Log.e("ZaloPayUtils", "Error creating MAC", e)
            throw e
        }
    }
}