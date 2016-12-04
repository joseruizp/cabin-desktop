package com.cabin.websocket;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.cabin.desktop.NotificationDialog;
import com.cabin.desktop.PWLauncher;

@ServerEndpoint(value = "/recharge")
public class RechargeMessageEndpoint {

    private Session session;
    private String message;

    @OnOpen
    public void init(Session session) {
        System.out.println("init");
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("onMessage: " + message);
        this.message = message;
        if (NotificationDialog.isDialogVisible()) {
            NotificationDialog.updateBalance(Double.parseDouble(message));
        } else {
            PWLauncher.updateBalance(Double.parseDouble(message));
        }
    }

    public String getMessage() {
        return this.message;
    }

    public Session getSession() {
        return this.session;
    }

}
