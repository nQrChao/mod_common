package com.box.common.utils.totp

open class Base32String protected constructor(
    val alphaBet: String
) : Cloneable {
    private val digits: CharArray = alphaBet.toCharArray()
    private val mask: Int = digits.size - 1
    private val shift: Int = Integer.numberOfTrailingZeros(digits.size)
    private val charMap: HashMap<Char, Int> = HashMap()

    init {
        for (i in digits.indices) {
            charMap[digits[i]] = i
        }
    }

    @Throws(DecodingException::class)
    protected fun decodeInternal(encoded: String): ByteArray {
        var encoded = encoded
        encoded = encoded.trim { it <= ' ' }.replace(SEPARATOR.toRegex(), "").replace(" ".toRegex(), "")

        // Remove padding. Note: the padding is used as hint to determine how many
        // bits to decode from the last incomplete chunk (which is commented out
        // below, so this may have been wrong to start with).
        encoded = encoded.replaceFirst("[=]*$".toRegex(), "")

        // Canonicalize to all upper case
        encoded = encoded.uppercase()
        if (encoded.isEmpty()) {
            return ByteArray(0)
        }
        val encodedLength = encoded.length
        val outLength = encodedLength * shift / 8
        val result = ByteArray(outLength)
        var buffer = 0
        var next = 0
        var bitsLeft = 0
        for (c in encoded.toCharArray()) {
            if (!charMap.containsKey(c)) {
                throw DecodingException("Illegal character: $c")
            }
            buffer = buffer shl shift
            buffer = buffer or (charMap[c]!! and mask)
            bitsLeft += shift
            if (bitsLeft >= 8) {
                result[next++] = (buffer shr bitsLeft - 8).toByte()
                bitsLeft -= 8
            }
        }
        // We'll ignore leftover bits for now.
        //
        // if (next != outLength || bitsLeft >= SHIFT) {
        //  throw new DecodingException("Bits left: " + bitsLeft);
        // }
        return result
    }

    protected fun encodeInternal(data: ByteArray): String {
        if (data.isEmpty()) {
            return ""
        }

        // SHIFT is the number of bits per output character, so the length of the
        // output is the length of the input multiplied by 8/SHIFT, rounded up.
        // The computation below will fail, so don't do it.
        require(!(data.size >= 1 shl 28))
        val outputLength = (data.size * 8 + shift - 1) / shift
        val result = StringBuilder(outputLength)
        var buffer = data[0].toInt()
        var next = 1
        var bitsLeft = 8
        while (bitsLeft > 0 || next < data.size) {
            if (bitsLeft < shift) {
                if (next < data.size) {
                    buffer = buffer shl 8
                    buffer = buffer or (data[next++].toInt() and 0xff)
                    bitsLeft += 8
                } else {
                    val pad = shift - bitsLeft
                    buffer = buffer shl pad
                    bitsLeft += pad
                }
            }
            val index = mask and (buffer shr bitsLeft - shift)
            bitsLeft -= shift
            result.append(digits[index])
        }
        return result.toString()
    }

    @Throws(CloneNotSupportedException::class)  // enforce that this class is a singleton
    public override fun clone(): Any {
        throw CloneNotSupportedException()
    }

    class DecodingException(message: String?) : Exception(message)
    companion object {
        //new Base32String("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"); // RFC 4648/3548
        val instance = Base32String("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789")
        const val SEPARATOR = "-"
        @JvmStatic
        @Throws(DecodingException::class)
        fun decode(encoded: String): ByteArray {
            return instance.decodeInternal(encoded)
        }

        fun encode(data: ByteArray): String {
            return instance.encodeInternal(data)
        }
    }
}