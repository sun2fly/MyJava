package com.mrfsong.pattern.creat.factory.simple;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/09 14:11
 **/
public class SimpleShapeFactory {

    public static final String TAG = "SimpleShapeFactory";

    public static Shape getShape(String type) {
        Shape shape = null;
        if (type.equalsIgnoreCase("circle")) {
            shape = new CircleShape();
        } else if (type.equalsIgnoreCase("rect")) {
            shape = new RectShape();
        } else if (type.equalsIgnoreCase("triangle")) {
            shape = new TriangleShape();
        }
        return shape;
    }

}
