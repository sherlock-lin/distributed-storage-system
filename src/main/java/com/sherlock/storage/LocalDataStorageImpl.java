package com.sherlock.storage;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * author: shalock.lin
 * date: 2024/1/30
 * describe:
 */
public class LocalDataStorageImpl implements DataStorage<String>{

    private static MappedByteBuffer mappedByteBuffer;

    private static Integer _1Gb = 1024*1024*1024;

    private static Integer _1MB = 1024*1024;

    public LocalDataStorageImpl() {
        try {
            FileChannel fileChannel = new RandomAccessFile("./testWrite", "rw").getChannel();
            mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, _1MB);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(String data) throws Exception{
        System.out.println("start writeDataToFile data is :"+data);
        mappedByteBuffer.put(data.getBytes());
        System.out.println("writeDataToFile end!");
    }
}
