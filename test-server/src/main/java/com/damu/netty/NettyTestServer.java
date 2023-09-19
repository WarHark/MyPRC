package com.damu.netty;

import com.damu.transport.netty.server.NettyServer;

public class NettyTestServer {
    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer(8889);
        nettyServer.run();

    }
}
