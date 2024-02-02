package com.sherlock.storage;

import com.sherlock.service.NetServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

/**
 * author: shalock.lin
 * date: 2024/1/30
 * describe:
 */
public class LocalDataStorageImpl implements DataStorage<String>{

    private static final Logger LOG = LoggerFactory.getLogger(LocalDataStorageImpl.class);

    public static final String NAME = "storage-service";

    protected MappedFile mappedFile;

    private static Integer _1Gb = 1024*1024*1024;

    private static Integer _1MB = 1024*1024;


    public LocalDataStorageImpl() {
        try {
            //fileRecords = FileRecords.open(new File("./testWrite"),false, 0, false);
            mappedFile = new DefaultMappedFile("testWrite", 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(String data) throws Exception{
        LOG.info("start writeDataToFile data is :"+data);
        mappedFile.appendMessage(data.getBytes());
        LOG.info("writeDataToFile end!");
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void close() {

    }
}
