package com.damu.transport.netty.client;

import com.damu.codec.NettyKryoDecoder;
import com.damu.entity.RpcRequest;
import com.damu.entity.RpcResponse;
import com.damu.serializer.KryoSerializer;
import com.damu.serializer.NettyKryoEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class NettyClient {
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private final String host;
    private final int port;
    private static Bootstrap bootstrap;
    
    public NettyClient(String host,int port){
        this.host = host;
        this.port = port;
    }

    /**
     * 初始化资源
     */
    static {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        KryoSerializer kryoSerializer = new KryoSerializer();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                //连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //  ByteBuf -> RpcResponse
                            socketChannel.pipeline().addLast(new NettyKryoDecoder(kryoSerializer,RpcResponse.class));
                            //  RpcRequest -> ByteBuf
                            socketChannel.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, RpcRequest.class));
                            socketChannel.pipeline().addLast(new NettyClientHandler());
                    }
                });
    }

    /**
     * 通过netty发送消息RpcRequest给服务器，服务器返回RpcResponse
     *
     */
    public RpcResponse SendMessage(RpcRequest rpcRequest){
            try{
                ChannelFuture cf = bootstrap.connect(host,port).sync();
                logger.info("client connect {}",host + ":"+ port);
                Channel fc = cf.channel();
                logger.info("send message");
                if(fc != null){
                    fc.writeAndFlush(rpcRequest).addListener(future -> {
                        if(future.isSuccess()){
                            logger.info("client send meg :{}",rpcRequest.toString());
                        }else{
                            logger.info("client send message failed!");
                        }
                    });
                    fc.closeFuture().sync();
                    AttributeKey<RpcResponse>key = AttributeKey.valueOf("rpcResponse");
                    System.out.println("fc.attr(key).get:"+fc.attr(key).get());
                    return fc.attr(key).get();
                }
            }catch (InterruptedException e){

            }
            return null;
    }

    
}
