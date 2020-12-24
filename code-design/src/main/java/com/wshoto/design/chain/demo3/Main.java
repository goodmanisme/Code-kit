package com.wshoto.design.chain.demo3;

import com.wshoto.design.chain.demo3.util.ClassUtil;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        new HandlerChain().handler(new Request(),new Response());
    }

}

class Request {
    String str = "Request";
}

class Response {
    String str = "Response";
}

interface IHandlerChain {
    void handler(Request request, Response response);
}

interface Handler {
    void handlerRequest(Request request, Response response, IHandlerChain chain);
}

class HandlerChain implements IHandlerChain {

    List<Handler> handlerList = new ArrayList<>();
    private int current;

     {
        ClassUtil.getAllInterfaceAchieveClass(Handler.class).forEach(c->{
            try {
                handlerList.add((Handler) c.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
    @Override
    public void handler(Request request, Response response) {
        if (current >= handlerList.size()) {
            return;
        }
        Handler handler = handlerList.get(current++);
        handler.handlerRequest(request, response, this);
    }
}

class FlowHandler implements Handler {

    @Override
    public void handlerRequest(Request request, Response response, IHandlerChain chain) {
        System.out.println("跟进请求");
        chain.handler(request, response);
    }
}

class SessionHandler implements Handler {

    @Override
    public void handlerRequest(Request request, Response response, IHandlerChain chain) {
        System.out.println("会话请求");
    }
}