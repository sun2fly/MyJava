package com.mrfsong.struct.pojo;

/**
 * 单链表
 */
public class LinkedNode {

    public int val;
    public LinkedNode next;

    public LinkedNode(int x) { val = x; }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public LinkedNode getNext() {
        return next;
    }

    public void setNext(LinkedNode next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "ListNode{" +
                "val=" + val + "}";
    }

}
