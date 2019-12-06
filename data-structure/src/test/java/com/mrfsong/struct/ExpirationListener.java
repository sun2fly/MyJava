package com.mrfsong.struct;

/**
 * <p>
 *
 * </P>
 *
 * @Author songfei20
 * @Date 2019/12/2
 */
public interface ExpirationListener<E> {

    /**
     * Invoking when a expired event occurs.
     *
     * @param expiredObject
     */
    void expired(E expiredObject);

}
