package com.mrfsong.common;

/**
 * <p>
 *      static关键字测试
 *      【代码块  静态代码块 静态常量  静态变量 静态方法】
 * </P>
 *
 * @Author songfei20
 * @Date 2020/5/12
 */
public class StaticTest {

    private static StaticTest obj = new StaticTest();

    {
        System.out.println("==========StaticTest common block start==========");
        System.out.println("i=" + i + ",j=" + j + ",k=" + k);

        i = 1;
        k = 1;
        System.out.println("i=" + i + ",j=" + j + ",k=" + k);
        System.out.println("==========StaticTest common block end==========");

    }

    private static int i = 100;
    private static int j = 101;
    private static int k ;
    private static final int l = 105 ;

    static {
        System.out.println("StaticTest========== static block start==========");
        System.out.println("i=" + i + ",j=" + j + ",k=" + k);
        System.out.println("StaticTest========== static block end==========");
    }

    StaticTest() {
        System.out.println("==========StaticTest constructor start==========");
        System.out.println("i=" + i + ",j=" + j + ",k=" + k);
        i++;
        k = 103;
        System.out.println("i=" + i + ",j=" + j + ",k=" + k);
        System.out.println("==========StaticTest constructor end==========");

    }

    static void method(){
        System.out.println("==========StaticTest static method start==========");
        System.out.println("i=" + i + ",j=" + j + ",k=" + k);
        i = 0;
        j = 0;
        k = 0;
        System.out.println("==========StaticTest static method end==========");
    }



    static class ChildStaticTest extends StaticTest {


        private static ChildStaticTest obj = new ChildStaticTest();

        {
            System.out.println("==========ChildStaticTest common block start==========");
            System.out.println("si=" + si + ",sj=" + sj +",sk=" +sk);
            si++;
            System.out.println("si=" + si + ",sj=" + sj +",sk=" +sk);
            System.out.println("==========ChildStaticTest common block end==========");
        }

        protected static int si = 200;
        protected static int sj = 201;
        protected static int sk;



        static {
            System.out.println("==========ChildStaticTest static block start==========");
            System.out.println("si=" + si + ",sj=" + sj +",sk=" +sk);
            System.out.println("==========ChildStaticTest static block end==========");
        }

        ChildStaticTest() {
            System.out.println("==========ChildStaticTest constructor start==========");
            System.out.println("si=" + si + " sj=" + sj +" sk=" +sk);
            si++;
            sj = 302;
            sk = 202;
            System.out.println("si=" + si + " sj=" + sj +" sk=" +sk);
            System.out.println("==========ChildStaticTest constructor end==========");

        }

        public static void main(String[] args) {
            System.out.println("==========> i=" + i + ",j=" + j + ",k=" + k + "<==========");
            System.out.println("==========> si=" + si + ",sj=" + sj + ",sk=" + sk + "<==========");
        }
    }
}
