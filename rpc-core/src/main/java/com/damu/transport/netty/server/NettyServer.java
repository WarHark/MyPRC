package com.damu.transport.netty.server;


import com.damu.codec.NettyKryoDecoder;
import com.damu.entity.RpcRequest;
import com.damu.entity.RpcResponse;
import com.damu.serializer.KryoSerializer;
import com.damu.serializer.NettyKryoEncoder;
import com.damu.transport.netty.client.NettyClientHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class NettyServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private int port;
    public NettyServer(int port){
        this.port = port;
    }
    public void run(){
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        KryoSerializer kryoSerializer = new KryoSerializer();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boosGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyKryoDecoder(kryoSerializer, RpcRequest.class));
                            socketChannel.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, RpcResponse.class));
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture f = bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
