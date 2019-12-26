package com.mrfsong.common;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        /*App2 app2 = new App2();
        System.out.println(app2.getClass().getClassLoader().toString());*/
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
