package com.houshce29.ducky.framework.core;

import com.houshce29.ducky.exceptions.MultipleQualifyingObjectsFoundException;
import com.houshce29.ducky.exceptions.QualifiedObjectsNotFoundException;

import java.util.Set;

/**
 * Core object of the Ducky framework. An instance of some implementation
 * of this interface hosts services and components defined for it.
 */
public interface Environment {

    /**
     * @return New builder to begin building environment.
     */
    static EnvironmentBuilder define() {
        return new EnvironmentBuilder();
    }

    /**
     * Returns all the qualifying objects for the given type.
     * @param type Type of object(s).
     * @param errorIfNone Flag for throwing exception if none found.
     * @param <T> Type of object(s).
     * @return A set of all of the qualifying objects.
     * @throws QualifiedObjectsNotFoundException if no qualified objects are present,
     *         and the errorIfNone flag is set to true.
     */
    <T> Set<T> getAll(Class<T> type, boolean errorIfNone) throws QualifiedObjectsNotFoundException;

    /**
     * Returns all the qualifying objects for the given type.
     * If no objects are found, the Set will be empty.
     * @param type Type of object(s).
     * @param <T> Type of object(s).
     * @return A set of all of the qualifying objects.
     */
    <T> Set<T> getAll(Class<T> type);

    /**
     * Returns the object assigned to the type. This assumes
     * that exactly one object exists for the requested type.
     * @param type Type of object.
     * @param <T> Type of object.
     * @return Object of the requested type.
     * @throws QualifiedObjectsNotFoundException if object is not found.
     * @throws MultipleQualifyingObjectsFoundException if multiple objects are found.
     */
    <T> T get(Class<T> type);

    /**
     * Returns an object assigned to the requested type.
     * If the `errorIfMultiple` flag is set to `true` and
     * multiple objects are found, then an exception is thrown.
     * Otherwise, the first object found is returned.
     * @param type Type of the object.
     * @param errorIfMultiple Flag to denote if an exception should be thrown
     *                        if multiple objects are found.
     * @param <T> Type of the object.
     * @return Object of the requested type.
     */
    <T> T get(Class<T> type, boolean errorIfMultiple);

    /**
     * Returns the number of objects of the given type
     * in this environment.
     * @param type Type to query for.
     * @return Number of objects of the given type.
     */
    int count(Class<?> type);

    /**
     * Returns `true` if the environment contains one or more objects
     * of the given type.
     * @param type Type to search for.
     * @return `true` if the environment contains one or more.
     */
    boolean contains(Class<?> type);
}
