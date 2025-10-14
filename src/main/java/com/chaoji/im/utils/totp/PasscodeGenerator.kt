package com.chaoji.im.utils.totp

import com.chaoji.im.utils.totp.Base32String.Companion.decode
import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream
import java.io.IOException
import java.nio.ByteBuffer
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.math.pow

open class PasscodeGenerator {
    private fun padOutput(value: Int): String {
        val result = StringBuilder(value.toString())
        for (i in result.length until PASS_CODE_LENGTH) {
            result.insert(0, "0")
        }
        return result.toString()
    }

    private fun long2bytes(state: Long): ByteArray {
        return ByteBuffer.allocate(8).putLong(state).array()
    }

    private fun bytes2int(hash: ByteArray): Int {
        val offset = hash[hash.size - 1].toInt() and 0xF
        return hashToInt(hash, offset) and 0x7FFFFFFF
    }

    /**
     * hash to int
     */
    private fun hashToInt(bytes: ByteArray, start: Int): Int {
        val input: DataInput = DataInputStream(
            ByteArrayInputStream(bytes, start, bytes.size - start)
        )
        val `val`: Int = try {
            input.readInt()
        } catch (e: IOException) {
            throw IllegalStateException(e)
        }
        return `val`
    }

    private fun generateResponseCode(Key: String, dataString: String): String {
        try {
            val KeyBytes = decode(Key + dataString)
            val mac = Mac.getInstance("HMACSHA1")
            mac.init(SecretKeySpec(KeyBytes, ""))
            val time = valueAtTime
            val data = long2bytes(time)
            val hash = mac.doFinal(data)
            val truncatedHash = bytes2int(hash)
            val pinValue = truncatedHash % java.lang.Double.valueOf(10.0.pow(PASS_CODE_LENGTH.toDouble())).toInt()
            return padOutput(pinValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    companion object {
        protected const val mStartTime: Long = 0

        // 600s变换一次密码
        private const val mTimeStep: Long = 600

        //默认密码长度
        private const val PASS_CODE_LENGTH = 6
        private var mInstance: PasscodeGenerator? = null
        val instance: PasscodeGenerator?
            get() {
                if (mInstance == null) {
                    mInstance = PasscodeGenerator()
                }
                return mInstance
            }

        fun generateTotpNum(Key: String, dataString: String): String {
            return instance!!.generateResponseCode(Key, dataString)
        }

        fun currentTimeMillis(): Long {
            return System.currentTimeMillis()
        }

        private fun currentTimeSeconds(): Long {
            return currentTimeMillis() / 1000
        }

        private fun getValueAtTime(time: Long): Long {
            val timeSinceStartTime = time - mStartTime
            return if (timeSinceStartTime >= 0) {
                timeSinceStartTime / mTimeStep
            } else {
                (timeSinceStartTime - (mTimeStep - 1)) / mTimeStep
            }
        }

        val valueAtTime: Long
            get() = getValueAtTime(currentTimeSeconds())

        fun getValueStartTime(value: Long): Long {
            return mStartTime + value * mTimeStep
        }
    }
}