package com.damu.netty;

import com.damu.entity.RpcRequest;
import com.damu.entity.RpcResponse;
import com.damu.transport.netty.client.NettyClient;

public class NettyTestClient {
    public static void main(String[] args) {
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName("interface")
                .methodName("hello").build();
        NettyClient nettyClient = new NettyClient("127.0.0.1", 8889);
        for (int i = 0; i < 3; i++) {
            nettyClient.SendMessage(rpcRequest);
        }
        System.out.println(nettyClient.SendMessage(rpcRequest).getMessage());
    }
}
