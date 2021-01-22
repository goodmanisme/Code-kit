//package com.wshoto.design.chain.demo4;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.BeanUtils;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ObjectUtils;
//import org.springframework.util.StringUtils;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.List;
//
//public class Main {
//
//}
//
///**
// * 客户记录处理器
// *
// * @author AGoodMan
// */
//interface CustomerRecordHandler {
//
//    /**
//     * 请求处理器(一)
//     *
//     * @param request
//     * @param response
//     * @param chain
//     * @return
//     */
//    Object handlerRequest(HttpServletRequest request, HttpServletResponse response, CustomerRecordHandlerChain chain);
//
//    /**
//     * 请求处理器(二)
//     *
//     * @param type
//     * @param obj
//     * @param chain
//     * @return
//     */
//    Object handlerRequest(String type, Object obj, CustomerRecordHandlerChain chain);
//}
//
///**
// * 客户记录处理器链
// *
// * @author AGoodMan
// */
//interface CustomerRecordHandlerChain {
//
//    /**
//     * 处理方式(一)
//     *
//     * @param request  请求模型
//     * @param response 响应模型
//     * @return
//     */
//    Object handler(HttpServletRequest request, HttpServletResponse response);
//
//    /**
//     * 处理方式(二)
//     *
//     * @param type 类型
//     * @param obj  处理对象
//     * @return
//     */
//    Object handler(String type, Object obj);
//}
//
///**
// * 客户记录工厂类
// *
// * @author AGoodMan
// */
//@Slf4j
//@Component
//class CustomerRecordFactory implements CustomerRecordHandlerChain {
//
//    @Resource
//    private List<CustomerRecordHandler> handlers;
//
//    private int current;
//
//
//    @Override
//    public Object handler(HttpServletRequest request, HttpServletResponse response) {
//        if (current >= handlers.size()) {
//            log.info("该请求超出此处理器的处理范围!");
//            return null;
//        }
//        CustomerRecordHandler handler = handlers.get(current++);
//        return handler.handlerRequest(request, response, this);
//    }
//
//    @Override
//    public Object handler(String type, Object obj) {
//        if (StringUtils.isEmpty(type)) {
//            return "标签类型不能为空!";
//        }
//        if (ObjectUtils.isEmpty(obj)) {
//            return "该请求对象不能为空!";
//        }
//        if (current >= handlers.size()) {
//            log.info("该请求超出此处理器的处理范围!");
//            return null;
//        }
//        CustomerRecordHandler handler = handlers.get(current++);
//        return handler.handlerRequest(type, obj, this);
//    }
//}
//
///**
// * 活动记录
// *
// * @author AGoodMan
// */
//@Component
//@Order(1)
//class ActivityRecordImpl implements CustomerRecordHandler {
//
//    @Resource
//    private CustomerRecordService customerRecordService;
//
//    @Override
//    public Object handlerRequest(HttpServletRequest request, HttpServletResponse response, CustomerRecordHandlerChain chain) {
//        return null;
//    }
//
//    @Override
//    public Object handlerRequest(String type, Object obj, CustomerRecordHandlerChain chain) {
//        if (CustomerRecordConstant.ACTIVITY_RECORD.equals(type)) {
//            return "暂时还没有实现活动记录";
//        }
//        return chain.handler(type, obj);
//    }
//}
//
///**
// * 跟进记录
// *
// * @author AGoodMan
// */
//@Component
//@Order(2)
//class FollowUpRecordImpl implements CustomerRecordHandler {
//
//    @Resource
//    private CustomerRecordService customerRecordService;
//
//    @Override
//    public Object handlerRequest(HttpServletRequest request, HttpServletResponse response, CustomerRecordHandlerChain chain) {
//        return null;
//    }
//
//    @Override
//    public Object handlerRequest(String type, Object obj, CustomerRecordHandlerChain chain) {
//        if (CustomerRecordConstant.FOLLOW_UP_RECORD.equals(type)) {
//            QueryFollowUpRecordReq req = new QueryFollowUpRecordReq();
//            BeanUtils.copyProperties(obj, req);
//            return customerRecordService.getPageFollowUpRecordInfo(req);
//        }
//        return chain.handler(type, obj);
//    }
//}
