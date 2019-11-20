package com.mrfsong.struct.data;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.openjdk.jol.info.GraphLayout;

/**
 * <p>
 * 链表数据结构
 * </P>
 *
 * @Author songfei20
 * @Date 2019/11/20
 */
@Slf4j
public class LinkedListTest {


    @Test
    public void build() {

        int depth = 3;
        Node header = null ;
        Node tail = null;

        for(int i=1;i<=depth;i++){
            Node node = new Node(i);
            if(header == null) {
                header = node ;
                tail = header;
                log.info("Tail Internal : {}", GraphLayout.parseInstance(tail).toPrintable());
            }else {
                tail.next = node;
                tail = node;
                log.info("Tail Internal : {}", GraphLayout.parseInstance(tail).toPrintable());
            }

            log.info("Header Internal : {}", GraphLayout.parseInstance(header).toPrintable());
        }
        log.info(header.iterator());

        /*log.info("Object Internal : {}", ClassLayout.parseInstance(header).toPrintable());

        log.info("Object External : {}", GraphLayout.parseInstance(header).toPrintable());

        log.info("Object size :　{}  bytes",GraphLayout.parseInstance(header).totalSize());*/





    }

    @Test
    public void testObjReference() {
        String s1 = new String("abc");
        String s2 = s1 ;
        log.info(String.valueOf(s2.equals(s1)));
        log.info(s1);

        s2 = new String("123");
        log.info(String.valueOf(s2.equals(s1)));
        log.info(s1);

    }





    class Node {
        private Integer num;
        private Node next;

        public Node(Integer num) {
            this.num = num;
        }

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }


        public String iterator () {
            String output = String.valueOf(this.getNum());
            Node header = this;
            while(header.getNext() != null){
                output += header.getNext().getNum();
                header = header.next;

            }
            return output;

        }


    }
}
