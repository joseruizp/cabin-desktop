package com.cabin.websocket;

import java.io.IOException;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/recharge")
public class RechargeMessageEndpoint {

    private Session session;

    @OnOpen
    public void init(Session session) {
        System.out.println("init");
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        try {
            System.out.println("onMessage: " + message);
            this.session.getBasicRemote().sendText("sending: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
