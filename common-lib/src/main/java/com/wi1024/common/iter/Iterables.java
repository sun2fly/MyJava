package com.wi1024.common.iter;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * <p>
 *      Iterable工具类
 * </P>
 *
 * @Author songfei20
 * @Date 2018/10/31 16:32
 * @Version 1.0
 */
public class Iterables {
    public static <E> void forEach(
            Iterable<? extends E> elements, BiConsumer<Integer, ? super E> action) {
        Objects.requireNonNull(elements);
        Objects.requireNonNull(action);

        int index = 0;
        for (E element : elements) {
            action.accept(index++, element);
        }
    }
}
