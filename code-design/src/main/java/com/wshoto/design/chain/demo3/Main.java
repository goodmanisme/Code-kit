package com.wshoto.design.chain.demo3;

import com.wshoto.design.chain.demo3.util.ClassUtil;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        /*new HandlerChain()
                .add(new FlowHandler())
                .add(new SessionHandler())
                .handler(new Request(),new Response());*/
        List<Handler> dogList = new ArrayList<>();
        List<Class> classList = ClassUtil.getAllInterfaceAchieveClass(Handler.class);
        for (Class c : classList) {
            dogList.add((Handler) c.newInstance());
        }

        for (Handler d : dogList) {
            System.out.println(d.getClass());
        }

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

    //private List<Handler> handlerList;
    List<Handler> handlerList = new ArrayList<>();

    private int current;

    /*public HandlerChain(List<Handler> handlerList) {
        this.handlerList = handlerList;
        current = 0;
    }*/

    public HandlerChain add(Handler handler) {
        handlerList.add(handler);
        return this;
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