package com.sherlock.service;


import com.sherlock.net.ServerInitializer;
import com.sherlock.storage.DataStorage;
import com.sherlock.storage.LocalDataStorageImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * author: shalock.lin
 * date: 2024/1/30
 * describe:
 */
public class NetServiceImpl implements NetService{

    private static final Logger LOG = LoggerFactory.getLogger(NetServiceImpl.class);

    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;

    public void start() {
        //bossGroup就是parentGroup，是负责处理TCP/IP连接的
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //workerGroup就是childGroup,是负责处理Channel(通道)
        EventLoopGroup workerGroup = new NioEventLoopGroup(30);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //初始化服务端可连接队列,指定了队列的大小128
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
                    .option(ChannelOption.TCP_NODELAY, true)
                    //保持长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ServerInitializer());
            ChannelFuture future = bootstrap.bind(8888).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e){

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
