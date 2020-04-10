package com.mrfsong.storage.rocks;

/**
 * Hello world!
 *
 */
public class App 
{
    static {

        System.out.println("============ Hey , I'm static block  ============");

    }

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        synchronized (App.class) {
            while (true) {
                try {
                    App.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
