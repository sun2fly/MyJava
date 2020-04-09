package com.mrfsong.struct.data;

/**
 * <p>
 *      HashMap模拟
 * </P>
 *
 * @Author songfei20
 * @Date 2020/1/2
 */
public class HashMapTest {


    private final Integer INIT_SIZE = 8;

    private int capacity = INIT_SIZE;
    private Entry[] table = new Entry[capacity];

    private float loadFactory = 0.75f;


    public void put(String key , String value) {
        int index;
        if(key == null){
            index = hashForNull();
        }
        index = hash(key) % capacity;




    }

    public void get(String key) {

    }

    private int hash(String key){

        return 0;
    }

    private int hashForNull() {
        return 0;
    }

    private void resize(int newSize){}



    class Entry {

        private String key ;
        private String value ;

        private Entry next ;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Entry getNext() {
            return next;
        }

        public void setNext(Entry next) {
            this.next = next;
        }
    }
}
