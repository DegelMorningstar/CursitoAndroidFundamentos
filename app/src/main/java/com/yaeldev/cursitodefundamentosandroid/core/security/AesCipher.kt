package com.yaeldev.cursitodefundamentosandroid.core.security

import android.util.Base64
import android.util.Base64.NO_WRAP
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AesCipher {

    private val SECRET_KEY = "Q3Vyc2l0b0FwcEFuZHJvaWQyMDI2UHJv"
    private val IV = "UHJvSEROb0Zha2U="
    private val keySpec = SecretKeySpec(
        SECRET_KEY.toByteArray(Charsets.UTF_8),
        "AES"
    )
    private val ivSpec = IvParameterSpec(
        IV.toByteArray(Charsets.UTF_8)
    )
    private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"

    fun encrypt(text:String):String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec)
        val encrypted = cipher.doFinal(
            text.toByteArray(Charsets.UTF_8)
        )
        return Base64.encodeToString(encrypted, Base64.NO_WRAP)
    }

    fun decrypt(base64EncryptedText:String):String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec)
        val decoded  = Base64.decode(base64EncryptedText, NO_WRAP)
        return String(cipher.doFinal(decoded ), Charsets.UTF_8)
    }

}