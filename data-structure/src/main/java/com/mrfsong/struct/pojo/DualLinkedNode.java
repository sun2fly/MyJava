package com.mrfsong.struct.pojo;

/**
 * 双链表
 */
public class DualLinkedNode {

    public int val;
    public LinkedNode pre;
    public LinkedNode next;

    public DualLinkedNode(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public LinkedNode getPre() {
        return pre;
    }

    public void setPre(LinkedNode pre) {
        this.pre = pre;
    }

    public LinkedNode getNext() {
        return next;
    }

    public void setNext(LinkedNode next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "DualLinkNode{" +
                "val=" + val +
                '}';
    }
}
