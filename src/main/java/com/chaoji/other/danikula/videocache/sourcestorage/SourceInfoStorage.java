package com.chaoji.other.danikula.videocache.sourcestorage;

import com.chaoji.other.danikula.videocache.SourceInfo;

/**
 * Storage for {@link SourceInfo}.
 */
public interface SourceInfoStorage {

    SourceInfo get(String url);

    void put(String url, SourceInfo sourceInfo);

    void release();
}
