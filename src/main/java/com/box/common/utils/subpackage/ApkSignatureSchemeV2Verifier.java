/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.box.common.utils.subpackage;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * APK Signature Scheme v2 verifier.
 * pms -> PackageParser.java -> ApkSignatureSchemeV2Verifier
 *
 * @hide for internal use only.
 */
public class ApkSignatureSchemeV2Verifier {

    /**
     * Returns the ZIP End of Central Directory (EoCD) and its offset in the file.
     * 获取apk中的EOCD和对应的偏移量
     *
     * @throws IOException                if an I/O error occurs while reading the file.
     * @throws SignatureNotFoundException if the EoCD could not be found.
     */
    public static Pair<ByteBuffer, Long> getEocd(RandomAccessFile apk)
            throws IOException, SignatureNotFoundException {
        Pair<ByteBuffer, Long> eocdAndOffsetInFile =
                ZipUtils.findZipEndOfCentralDirectoryRecord(apk);
        if (eocdAndOffsetInFile == null) {
            throw new SignatureNotFoundException(
                    "Not an APK file: ZIP End of Central Directory record not found");
        }
        return eocdAndOffsetInFile;
    }

    public static long getCentralDirOffset(ByteBuffer eocd, long eocdOffset)
            throws SignatureNotFoundException {
        // Look up the offset of ZIP Central Directory.
        long centralDirOffset = ZipUtils.getZipEocdCentralDirectoryOffset(eocd);
        if (centralDirOffset >= eocdOffset) {
            throw new SignatureNotFoundException(
                    "ZIP Central Directory offset out of range: " + centralDirOffset
                            + ". ZIP End of Central Directory offset: " + eocdOffset);
        }
        long centralDirSize = ZipUtils.getZipEocdCentralDirectorySizeBytes(eocd);
        if (centralDirOffset + centralDirSize != eocdOffset) {
            throw new SignatureNotFoundException(
                    "ZIP Central Directory is not immediately followed by End of Central"
                            + " Directory");
        }
        return centralDirOffset;
    }

    /**
     * Returns new byte buffer whose content is a shared subsequence of this buffer's content
     * between the specified start (inclusive) and end (exclusive) positions. As opposed to
     * {@link ByteBuffer#slice()}, the returned buffer's byte order is the same as the source
     * buffer's byte order.
     */
    public static ByteBuffer sliceFromTo(ByteBuffer source, int start, int end) {
        if (start < 0) {
            throw new IllegalArgumentException("start: " + start);
        }
        if (end < start) {
            throw new IllegalArgumentException("end < start: " + end + " < " + start);
        }
        int capacity = source.capacity();
        if (end > source.capacity()) {
            throw new IllegalArgumentException("end > capacity: " + end + " > " + capacity);
        }
        int originalLimit = source.limit();
        int originalPosition = source.position();
        try {
            source.position(0);
            source.limit(end);
            source.position(start);
            ByteBuffer result = source.slice();
            result.order(source.order());
            return result;
        } finally {
            source.position(0);
            source.limit(originalLimit);
            source.position(originalPosition);
        }
    }

    /**
     * Relative <em>get</em> method for reading {@code size} number of bytes from the current
     * position of this buffer.
     * <p>
     * <p>This method reads the next {@code size} bytes at this buffer's current position,
     * returning them as a {@code ByteBuffer} with start set to 0, limit and capacity set to
     * {@code size}, byte order set to this buffer's byte order; and then increments the position by
     * {@code size}.
     */
    public static ByteBuffer getByteBuffer(ByteBuffer source, int size)
            throws BufferUnderflowException {
        if (size < 0) {
            throw new IllegalArgumentException("size: " + size);
        }
        int originalLimit = source.limit();
        int position = source.position();
        int limit = position + size;
        if ((limit < position) || (limit > originalLimit)) {
            throw new BufferUnderflowException();
        }
        source.limit(limit);
        try {
            ByteBuffer result = source.slice();
            result.order(source.order());
            source.position(limit);
            return result;
        } finally {
            source.limit(originalLimit);
        }
    }

    public static final long APK_SIG_BLOCK_MAGIC_HI = 0x3234206b636f6c42L;
    public static final long APK_SIG_BLOCK_MAGIC_LO = 0x20676953204b5041L;
    private static final int APK_SIG_BLOCK_MIN_SIZE = 32;

    public static final int APK_SIGNATURE_SCHEME_V2_BLOCK_ID = 0x7109871a;
    /**
     * The padding in APK SIG BLOCK (V3 scheme introduced)
     * See https://android.googlesource.com/platform/tools/apksig/+/master/src/main/java/com/android/apksig/internal/apk/ApkSigningBlockUtils.java
     */
    public static final int VERITY_PADDING_BLOCK_ID = 0x42726577;

    public static final int ANDROID_COMMON_PAGE_ALIGNMENT_BYTES = 4096;

    public static Pair<ByteBuffer, Long> findApkSigningBlock(
            RandomAccessFile apk, long centralDirOffset)
            throws IOException, SignatureNotFoundException {
        // FORMAT:
        // OFFSET       DATA TYPE  DESCRIPTION
        // * @+0  bytes uint64:    size in bytes (excluding this field)
        // * @+8  bytes payload
        // * @-24 bytes uint64:    size in bytes (same as the one above)
        // * @-16 bytes uint128:   magic

        if (centralDirOffset < APK_SIG_BLOCK_MIN_SIZE) {
            throw new SignatureNotFoundException(
                    "APK too small for APK Signing Block. ZIP Central Directory offset: "
                            + centralDirOffset);
        }
        // Read the magic and offset in file from the footer section of the block:
        // * uint64:   size of block
        // * 16 bytes: magic
        ByteBuffer footer = ByteBuffer.allocate(24);
        footer.order(ByteOrder.LITTLE_ENDIAN);
        apk.seek(centralDirOffset - footer.capacity());
        apk.readFully(footer.array(), footer.arrayOffset(), footer.capacity());
        if ((footer.getLong(8) != APK_SIG_BLOCK_MAGIC_LO)
                || (footer.getLong(16) != APK_SIG_BLOCK_MAGIC_HI)) {
            throw new SignatureNotFoundException(
                    "No APK Signing Block before ZIP Central Directory");
        }
        // Read and compare size fields
        long apkSigBlockSizeInFooter = footer.getLong(0);
        if ((apkSigBlockSizeInFooter < footer.capacity())
                || (apkSigBlockSizeInFooter > Integer.MAX_VALUE - 8)) {
            throw new SignatureNotFoundException(
                    "APK Signing Block size out of range: " + apkSigBlockSizeInFooter);
        }
        int totalSize = (int) (apkSigBlockSizeInFooter + 8);
        long apkSigBlockOffset = centralDirOffset - totalSize;
        if (apkSigBlockOffset < 0) {
            throw new SignatureNotFoundException(
                    "APK Signing Block offset out of range: " + apkSigBlockOffset);
        }
        ByteBuffer apkSigBlock = ByteBuffer.allocate(totalSize);
        apkSigBlock.order(ByteOrder.LITTLE_ENDIAN);
        apk.seek(apkSigBlockOffset);
        apk.readFully(apkSigBlock.array(), apkSigBlock.arrayOffset(), apkSigBlock.capacity());
        long apkSigBlockSizeInHeader = apkSigBlock.getLong(0);
        if (apkSigBlockSizeInHeader != apkSigBlockSizeInFooter) {
            throw new SignatureNotFoundException(
                    "APK Signing Block sizes in header and footer do not match: "
                            + apkSigBlockSizeInHeader + " vs " + apkSigBlockSizeInFooter);
        }
        return Pair.create(apkSigBlock, apkSigBlockOffset);
    }

    public static void checkByteOrderLittleEndian(ByteBuffer buffer) {
        if (buffer.order() != ByteOrder.LITTLE_ENDIAN) {
            throw new IllegalArgumentException("ByteBuffer byte order must be little endian");
        }
    }

    public static class SignatureNotFoundException extends Exception {
        private static final long serialVersionUID = 1L;

        public SignatureNotFoundException(String message) {
            super(message);
        }
    }
}
