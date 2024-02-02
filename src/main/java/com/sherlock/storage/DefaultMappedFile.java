package com.sherlock.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * author: shalock.lin
 * date: 2024/2/1
 * describe:
 */
public class DefaultMappedFile implements MappedFile {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultMappedFile.class);

    protected File file;
    protected String preFilepath;
    protected String fileName;
    protected int fileSize;

    protected FileChannel fileChannel;
    protected MappedByteBuffer mappedByteBuffer;

    protected volatile int wrotePosition;

    protected AtomicIntegerFieldUpdater<DefaultMappedFile> WROTE_POSITION_UPDATER
            = AtomicIntegerFieldUpdater.newUpdater(DefaultMappedFile.class, "wrotePosition");


    public DefaultMappedFile(AtomicIntegerFieldUpdater<DefaultMappedFile> wrote_position_updater) {
    }

    public DefaultMappedFile(final String fileName, final int fileSize) throws IOException {
        init(fileName, fileSize);
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean renameTo(String fileName) {
        File newFile = new File(fileName);
        boolean rename = file.renameTo(newFile);
        if (rename) {
            this.fileName = fileName;
            this.file = newFile;
        }
        return rename;
    }

    @Override
    public int getFileSize() {
        return fileSize;
    }

    @Override
    public boolean isFull() {
        return this.fileSize == WROTE_POSITION_UPDATER.get(this);
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public boolean appendMessage(byte[] data) {
        int currentPos = WROTE_POSITION_UPDATER.get(this);
        if (currentPos+data.length >= this.fileSize) {
            try {
                LOG.error("start rollingFile!");
                rollingFile();
                LOG.error("end rollingFile!");
            } catch (IOException e) {
                LOG.error("rollingFile fail!");
                return false;
            }
        }

        mappedByteBuffer.put(data);
        currentPos = WROTE_POSITION_UPDATER.get(this);
        LOG.info("current currentPos is {}, this.fileSize is {}", currentPos, this.fileSize);
        WROTE_POSITION_UPDATER.addAndGet(this, data.length);
        int afterPos = WROTE_POSITION_UPDATER.get(this);
        LOG.info("after write currentPos is {}, this.fileSize is {}", afterPos, this.fileSize);

        return true;
    }

    @Override
    public void init(String fileName, int fileSize) throws IOException {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.file = new File(fileName);
        boolean successInit = false;

        try {
            this.fileChannel = new RandomAccessFile(this.file, "rw").getChannel();
            this.mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
            successInit = true;
        } catch (FileNotFoundException e) {
            LOG.error("Failed to create file " + this.fileName, e);
            throw e;
        } catch (IOException e) {
            LOG.error("Failed to map file " + this.fileName, e);
            throw e;
        } finally {
            if (!successInit && this.fileChannel != null) {
                this.fileChannel.close();
            }
        }
    }

    @Override
    public int flush(int flushLeastPages) {
        this.mappedByteBuffer.force();
        return 0;
    }

    private boolean rollingFile() throws IOException {
        preFilepath =
                fileName+"-"+LocalDateTime.now().toString().replace(":","-").substring(0,19);
        File preFile = new File(preFilepath);
        boolean preFileExists = preFile.exists();
        if (!preFileExists) {
            this.fileChannel.force(false);
            boolean rename = file.renameTo(preFile);
            if (rename) {
                this.fileChannel = new RandomAccessFile(new File(fileName), "rw")
                        .getChannel();
                this.mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
                WROTE_POSITION_UPDATER.set(this, 0);
                return true;
            } else {
                LOG.error("TieredIndexFile#rollingFile: rename current file failed");
                return false;
            }
        }
        return false;
    }
}
