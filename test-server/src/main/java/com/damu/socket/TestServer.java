package com.damu.socket;

import com.damu.HelloService;
import com.damu.HelloServiceImpl;
import com.damu.transport.socket.server.RpcServer;

public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServer rpcServer = new RpcServer();
        rpcServer.register(helloService, 9000);
    }
}
