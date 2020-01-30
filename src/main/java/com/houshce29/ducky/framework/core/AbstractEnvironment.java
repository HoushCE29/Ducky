package com.houshce29.ducky.framework.core;

import com.houshce29.ducky.exceptions.MultipleQualifyingObjectsFoundException;
import com.houshce29.ducky.exceptions.QualifiedObjectsNotFoundException;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Core class that holds the polished built objects.
 */
public abstract class AbstractEnvironment implements Environment {
    private Set<Object> environment;

    public AbstractEnvironment(Set<Object> environment) {
        this.environment = environment;
    }

    @Override
    public <T> Set<T> getAll(Class<T> type, boolean errorIfNone) throws QualifiedObjectsNotFoundException {
        Set<T> qualified = environment.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toSet());

        if (qualified.isEmpty() && errorIfNone) {
            throw new QualifiedObjectsNotFoundException(type);
        }
        return qualified;
    }

    @Override
    public <T> Set<T> getAll(Class<T> type) {
        return getAll(type, false);
    }

    @Override
    public <T> T get(Class<T> type) {
        return get(type, true);
    }

    @Override
    public <T> T get(Class<T> type, boolean errorIfMultiple) {
        Set<T> qualified = getAll(type, true);
        if (errorIfMultiple && qualified.size() > 1) {
            throw new MultipleQualifyingObjectsFoundException(type);
        }
        return qualified.stream()
                .findFirst()
                // If no objects are found, an exception will have
                // been thrown by the call to `getAll()`.
                .get();
    }

    @Override
    public int count(Class<?> type) {
        return getAll(type).size();
    }

    @Override
    public boolean contains(Class<?> type) {
        return count(type) > 0;
    }

    // INTERNAL IMPL USE
    Set<Object> toSet() {
        return environment;
    }
}
