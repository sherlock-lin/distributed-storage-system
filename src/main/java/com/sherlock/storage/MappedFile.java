package com.sherlock.storage;

import java.io.IOException;

/**
 * author: shalock.lin
 * date: 2024/2/1
 * describe:
 */
public interface MappedFile {
    String getFileName();
    boolean renameTo(String fileName);
    int getFileSize();
    boolean isFull();
    boolean isAvailable();
    boolean appendMessage(byte[] data);
    void init(String fileName, int fileSize) throws IOException;
    int flush(int flushLeastPages);
}
