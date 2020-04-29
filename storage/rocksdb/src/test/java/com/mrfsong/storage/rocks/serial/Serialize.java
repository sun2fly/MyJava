package com.mrfsong.storage.rocks.serial;

import com.google.common.reflect.TypeToken;
import com.mrfsong.storage.rocks.vo.User;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

    public static void main(String[] args) throws Exception {

        /** ========== TypeToken ==========*/

        /*TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
        ParameterizedType type = (ParameterizedType) typeToken.getType();
        Type[] basicActualTypeArguments = type.getActualTypeArguments();
        for(Type tp : basicActualTypeArguments){
            System.out.println("Basic Generic Type ===> " + tp.getTypeName());
        }*/

        TypeToken<List<User>> objTypeToken = new TypeToken<List<User>>() {};
        System.out.println("Type ===> " + objTypeToken.getType());
        TypeToken<?> genericTypeToken = objTypeToken.resolveType(List.class.getTypeParameters()[0]);
        System.out.println("Generic Type ===> " + genericTypeToken.getType());
        System.out.println("Raw Type ===> " + genericTypeToken.getRawType());



        ParameterizedType uType = (ParameterizedType) objTypeToken.getType();
        Type[] actualTypeArguments = uType.getActualTypeArguments();
        for(Type tp : actualTypeArguments){
            System.out.println("Bean Generic Type ===> " + tp.getTypeName());
        }
    }


}
