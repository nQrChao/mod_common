/*
 * Tencent is pleased to support the open source community by making VasDolly available.
 *
 * Copyright (C) 2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License");you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.box.common.utils.subpackage;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Util {
    public static final int V1_MODE = 1;
    public static final int V2_MODE = 2;

    /**
     * 获取已知APK的渠道信息
     *
     * @param apkFile
     * @return
     */
    public static String readChannel(File apkFile) {
        String channel = ChannelReader.getChannelByV2(apkFile);
        if (channel == null) {
            channel = ChannelReader.getChannelByV1(apkFile);
        }
        return channel;
    }

    public static void main(String[] args) {
        File f = new File("C:/Users/caikun/Desktop/222.apk");
        writeChannel(f, "10000",false, false);
    }
    /**
     * 根据不同的方式写入渠道，并生成apk
     *
     * @param baseApk
     * @param channelStr
     */
    public static void writeChannel(File baseApk, String channelStr, boolean isMultiThread, boolean isFastMode) {
        if (TextUtils.isEmpty(channelStr)) {
            System.out.println("channel list is empty , please set channel list");
            return;
        }
        int mode = judgeChannelPackageMode(baseApk);
        if (mode == V1_MODE) {
            System.out.println("baseApk : " + baseApk.getAbsolutePath() + " , ChannelPackageMode : V1 Mode , isMultiThread : " + isMultiThread + " , isFastMode : " + isFastMode);
            generateV1ChannelApk(baseApk, channelStr, isFastMode);
        } else if (mode == V2_MODE) {
            System.out.println("baseApk : " + baseApk.getAbsolutePath() + " , ChannelPackageMode : V2 Mode" + " , isFastMode : " + isFastMode);
            generateV2ChannelApk(baseApk, channelStr, isFastMode);
        } else {
            throw new IllegalStateException("not have precise channel package mode");
        }
    }

    /**
     * 首先判断是否有V2签名，如果有就往V2签名段中写入渠道信息，否则就判断V1，如果有V1就在apk注释添加渠道
     * 第三种情况就是没有准确的签名
     *
     * @param baseApk
     * @return
     * @link https://source.android.com/security/apksigning/v2
     */
    private static int judgeChannelPackageMode(File baseApk) {
        if (ChannelReader.containV2Signature(baseApk)) {
            return V2_MODE;
        } else if (ChannelReader.containV1Signature(baseApk)) {
            return V1_MODE;
        } else {
            System.out.println("no signature, v1 operation.");
            return V1_MODE;
        }
    }

    /**
     * 如果目标是以.apk结尾则使用该路径，否则使用默认命名的路径
     *
     * @param dirOrApkPath 文件目录或指定的APK位置
     * @param apkName      原始apk名
     * @param channel      渠道号
     * @return
     */
    private static File getDestinationFile(File dirOrApkPath, String apkName, String channel) {
        if (dirOrApkPath.getAbsolutePath().endsWith(".apk")) {
            return dirOrApkPath;
        } else {
            return new File(dirOrApkPath, getChannelApkName(apkName, channel));
        }
    }

    /**
     * V1方式写入渠道
     *
     * @param baseApk
     * @param channelStr
     */
    private static void generateV1ChannelApk(File baseApk, String channelStr, boolean isFastMode) {
        if (!ChannelReader.containV1Signature(baseApk)) {
            System.out.println("File " + baseApk.getName() + " not signed by v1 , please check your signingConfig , if not have v1 signature , you can't install Apk below 7.0");
            return;
        }

        String apkName = baseApk.getName();
        //判断基础包是否已经包含渠道信息

        String testChannel = ChannelReader.getChannelByV1(baseApk);
        if (testChannel != null) {
            System.out.println("baseApk : " + baseApk.getAbsolutePath() + " has a channel : " + testChannel + ", only ignore");
            return;
        }

        long startTime = System.currentTimeMillis();
        System.out.println("------ File " + apkName + " generate v1 channel apk  , begin ------");

        try {
            //File destFile = getDestinationFile(outputDir, apkName, channelStr);
            //System.out.println("generatedV1ChannelApk , channel = " + channelStr + " , apkChannelName = " + destFile.getName());
            //copyFileUsingNio(baseApk, destFile);
            ChannelWriter.addChannelByV1(baseApk, channelStr);
            if (!isFastMode) {
                //1. verify channel info
                if (ChannelReader.verifyChannelByV1(baseApk, channelStr)) {
                    System.out.println("generateV1ChannelApk , " + baseApk + " add channel success");
                } else {
                    throw new RuntimeException("generateV1ChannelApk , " + baseApk + " add channel failure");
                }
            }
        } catch (Exception e) {
            System.out.println("generateV1ChannelApk error , please check it and fix it ，and that you should generate all V1 Channel Apk again!");
            e.printStackTrace();
        }

        System.out.println("------ File " + apkName + " generate v1 channel apk , end ------");
        long cost = System.currentTimeMillis() - startTime;
        System.out.println("------ total " + channelStr + " channel apk , cost : " + cost + " ------");
    }

    /**
     * V2方式写入渠道
     *
     * @param baseApk
     * @param channelStr
     */
    private static void generateV2ChannelApk(File baseApk, String channelStr, boolean isFastMode) {
        String apkName = baseApk.getName();
        long startTime = System.currentTimeMillis();
        System.out.println("------ File " + apkName + " generate v2 channel apk  , begin ------");

        try {
            ApkSectionInfo apkSectionInfo = IdValueWriter.getApkSectionInfo(baseApk, false);
            //File destFile = getDestinationFile(outputDir, apkName, channelStr);
            //System.out.println("generatedV2ChannelApk , channel = " + channelStr + " , apkChannelName = " + destFile.getName());
            //if (apkSectionInfo.lowMemory) {
            //    copyFileUsingNio(baseApk, destFile);
            //}
            ChannelWriter.addChannelByV2(apkSectionInfo, baseApk, channelStr);
            if (!isFastMode) {
                //1. verify channel info
                if (ChannelReader.verifyChannelByV2(baseApk, channelStr)) {
                    System.out.println("generateV2ChannelApk , " + baseApk + " add channel success");
                } else {
                    throw new RuntimeException("generateV2ChannelApk , " + baseApk + " add channel failure");
                }
            }
            apkSectionInfo.rewind();
            if (!isFastMode) {
                apkSectionInfo.checkEocdCentralDirOffset();
            }
        } catch (Exception e) {
            System.out.println("generateV2ChannelApk error , please check it and fix it ，and that you should generate all V2 Channel Apk again!");
            e.printStackTrace();
        }

        System.out.println("------ File " + apkName + " generate v2 channel apk , end ------");
        long cost = System.currentTimeMillis() - startTime;
        System.out.println("------ total " + channelStr + " channel apk , cost : " + cost + " ------");
    }

    /**
     * 配置添加渠道信息之后的apk名称
     *
     * @param baseApkName
     * @param channel
     * @return
     */
    public static String getChannelApkName(String baseApkName, String channel) {
        if (baseApkName.contains("base")) {
            return baseApkName.replace("base", channel);
        }
        return channel + "-" + baseApkName;
    }

    public static void copyFileUsingNio(File source, File dest) throws IOException {
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        File parent = dest.getParentFile();
        if (parent != null && (!parent.exists())) {
            parent.mkdirs();
        }
        if (source.getAbsolutePath().equals(dest.getAbsolutePath())) {
            System.out.println("No copying induces same absolute path, dest: " + dest.getAbsolutePath());
            return;
        }
        try {
            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(dest, false);
            in = inStream.getChannel();
            out = outStream.getChannel();
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                inStream.close();
            }
            if (outStream != null) {
                outStream.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
