package com.houshce29.ducky.internal.processing;

import com.houshce29.ducky.exceptions.ReservedTypeException;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.util.Set;

/**
 * Mother of all plucking utils!
 */
public final class MotherPlucker {
    private MotherPlucker() {
    }

    /**
     * Plucks out a Set's parameter type defined in the constructor.
     * @param constructor Constructor that a Set was defined in.
     * @param parameter Index of the target Set within the constructor.
     * @return Set's parameter type.
     */
    public static Class<?> pluckSetType(Constructor<?> constructor, int parameter) {
        try {
            // Get the Set out of the constructor.
            ParameterizedType set = (ParameterizedType) constructor
                    .getGenericParameterTypes()[parameter];

            // Set should only have exactly one type arg in this framework.
            return (Class<?>) set.getActualTypeArguments()[0];
        }
        catch (Exception ex) {
            throw new ReservedTypeException(Set.class,
                    "Constructor arg -> Set<TypeOfSet> paramName", ex);
        }
    }
}
