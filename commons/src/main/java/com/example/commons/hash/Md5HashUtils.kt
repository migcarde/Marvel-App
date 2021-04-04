package com.example.commons.hash

import java.math.BigInteger
import java.security.MessageDigest

const val MD5 = "MD5"
const val MD5_FORMAT = "%032x"

fun String.toMD5Hash(): String {
    val md5 = MessageDigest.getInstance(MD5)
    val stringBigInt = BigInteger(1, md5.digest(this.toByteArray(Charsets.UTF_8)))

    return String.format(MD5_FORMAT, stringBigInt)
}