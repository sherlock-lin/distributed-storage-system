package com.sherlock.ha;

/**
 * author: shalock.lin
 * date: 2024/2/4
 * describe:
 */
public interface RegistrationClient extends AutoCloseable {
    @Override
    void close();
}
