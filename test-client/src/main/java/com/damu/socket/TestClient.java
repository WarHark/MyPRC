package com.damu.socket;

import com.damu.HelloObject;
import com.damu.HelloService;
import com.damu.transport.socket.client.RpcClientProxy;

public class TestClient {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9000);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.sayhello(object);
        System.out.println(res);
    }
}
