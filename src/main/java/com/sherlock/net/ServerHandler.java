package com.sherlock.net;

import com.sherlock.storage.DataStorage;
import com.sherlock.storage.LocalDataStorageImpl;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.nio.MappedByteBuffer;
import java.util.Date;

/**
 * author: shalock.lin
 * date: 2024/1/30
 * describe:
 */
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger LOG = LoggerFactory.getLogger(ServerHandler.class);

    private DataStorage dataStorage = new LocalDataStorageImpl();


    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        channelHandlerContext.write("Welcome to sherlock home!");
        channelHandlerContext.write("It is "+ new Date()+"\n");
        channelHandlerContext.flush();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
        LOG.info("========readdata, request is {}=========", request);
        //异步通过专门的EventLoop线程池进行处理

        dataStorage.save(request);
        String response;
        boolean close = false;
        if (request.isEmpty()) {
            response = "Please type something.\r\n";
        } else if ("bye".equals(request.toLowerCase())) {
            response = "Have a good day!\r\n";
            close = true;
        } else {
            response = "Did you say '" + request + "'?\r\n";
        }

        ChannelFuture future = ctx.write(response);

        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}