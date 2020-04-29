package com.mrfsong.storage.rocks;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 序列化
 * </P>
 *
 * @Author songfei20
 * @Date 2020/4/27
 */
@Slf4j
public class Serialize {

    public static void main(String[] args) {
       /* KryoSerializer<String> kryoSerializer = new KryoSerializer<>();
        TypeResolver typeResolver = new TypeResolver();
        ResolvedType type = typeResolver.resolve(kryoSerializer.getClass());
        log.info("type.getFullDescription() : {}" , type.getFullDescription());
        log.info("type.getErasedType() : {}" , type.getErasedType().getName());
        TypeBindings typeBindings = type.getTypeBindings();
        log.info("typeBindings.getClass():{}",typeBindings.getClass().getName());
        List<ResolvedType> typeParameters = typeBindings.getTypeParameters();
        for(ResolvedType resolvedType : typeParameters){
            log.info(resolvedType.getErasedType().getName());
        }*/


        /** ========== TypeToken ==========*/

        ArrayList<String> stringList = Lists.newArrayList();
        ArrayList<Integer> intList = Lists.newArrayList();
        System.out.println("intList type is " + intList.getClass());
        System.out.println("stringList type is " + stringList.getClass());
        System.out.println(stringList.getClass().isAssignableFrom(intList.getClass()));

        TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
        ParameterizedType type = (ParameterizedType) typeToken.getType();
        System.out.println(type.getTypeName());

        Type type1 = new TypeToken<List<String>>() {}.getType();
        System.out.println(type1.getClass());




    }


}
