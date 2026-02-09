package com.github.rvskele.toppercustomnamelookup.util;

import java.lang.reflect.Field;

public final class ReflectionUtil {

    private ReflectionUtil() {}

    /**
     * Reads the value of a {@link Field} from a given object instance,
     * automatically setting the field as accessible.
     *
     * @param field    the field to read
     * @param instance the object from which the field value should be extracted
     * @param <T>      the expected return type
     *
     * @return the field's value cast to the specified type
     *
     * @throws IllegalAccessException if the field cannot be accessed - (shouldn't happen)
     * @throws ClassCastException     if the field's value cannot be cast to type {@code T}
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Field field, Object instance) throws IllegalAccessException {
        field.setAccessible(true);
        return (T) field.get(instance);
    }
}
