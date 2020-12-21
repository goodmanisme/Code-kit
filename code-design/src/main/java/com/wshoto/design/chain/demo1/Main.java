package com.wshoto.design.chain.demo1;

public class Main {

    public static void main(String[] args) {
        CustomerRecordHandler activityRecord = new ActivityRecordImpl();
        CustomerRecordHandler buddyRecord = new BuddyRecordImpl();
        CustomerRecordHandler followUpRecord = new FollowUpRecordImpl();
        activityRecord.setNext(buddyRecord);
        buddyRecord.setNext(followUpRecord);
        String s = activityRecord.executeChain("3");
        System.out.println(s);
    }

}

abstract class CustomerRecordHandler {

    protected CustomerRecordHandler next;

    public abstract String executeChain(String req);

    public CustomerRecordHandler getNext() {
        return next;
    }

    public void setNext(CustomerRecordHandler next) {
        this.next = next;
    }
}

class ActivityRecordImpl extends CustomerRecordHandler {

    @Override
    public String executeChain(String req) {
        if (req.equals("1")) {
            return "活动记录";
        }
        if (getNext() != null) {
            return getNext().executeChain(req);
        }
        return "没有人处理该请求";
    }

}

class BuddyRecordImpl extends CustomerRecordHandler {
    @Override
    public String executeChain(String req) {
        if (req.equals("2")) {
            return "好友记录";
        }
        if (getNext() != null) {
            return getNext().executeChain(req);
        }
        return "没有人处理该请求";
    }
}


class FollowUpRecordImpl extends CustomerRecordHandler {
    @Override
    public String executeChain(String req) {
        if (req.equals("3")) {
            return "跟进记录";
        }
        if (getNext() != null) {
            return getNext().executeChain(req);
        }
        return "没有人处理该请求";
    }
}
