package com.cabin.rest;

import java.net.URI;
import java.net.URISyntaxException;

public class RechargeSocket extends BaseRest {

    private WebsocketClientEndpoint clientEndPoint;

    public RechargeSocket(Long clientId) {
        // clientEndPoint = new WebsocketClientEndpoint(new URI(getHost() + "/cabin/recharge" + clientId));
        try {
            clientEndPoint = new WebsocketClientEndpoint(new URI(getHost() + "gs-guide-websocket/cabin/rechargeSocket"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void listener() {
        System.out.println("in listener");
        clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
            public void handleMessage(String message) {
                System.out.println(message);
            }
        });
    }
}
