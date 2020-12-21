package com.wshoto.design.chain.demo2;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        HandlerChain handlerChain = new HandlerChain();
        handlerChain.addHandler(new ChatHandler())
                .addHandler(new CustomerRecordsHandler())
                .addHandler(new FlowHandler());
        HandlerChain sessionChain = new HandlerChain();
        sessionChain.addHandler(new SessionHandler());
        handlerChain.addHandler(sessionChain).doHandler();
    }
}


interface Handler {
    boolean doHandler();
}

class ChatHandler implements Handler {

    @Override
    public boolean doHandler() {
        System.out.println("聊天处理器");
        return true;
    }
}

class SessionHandler implements Handler {

    @Override
    public boolean doHandler() {
        System.out.println("会话处理器");
        return true;
    }
}

class CustomerRecordsHandler implements Handler {

    @Override
    public boolean doHandler() {
        System.out.println("客户记录处理器");
        return true;
    }
}

class FlowHandler implements Handler {

    @Override
    public boolean doHandler() {
        System.out.println("跟进记录处理器");
        return true;
    }
}

class HandlerChain implements Handler {
    List<Handler> list = new ArrayList();

    public HandlerChain addHandler(Handler h) {
        list.add(h);
        return this;
    }

    public boolean doHandler() {
        for (Handler h : list) {
            if (!h.doHandler()) return false;
        }
        return true;
    }
}