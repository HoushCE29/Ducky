package com.houshce29.ducky.framework.core;

import java.util.Set;
import java.util.function.Supplier;

/**
 * One option of supplying dependencies to be built for the environment.
 */
public interface InjectionDefinition extends Supplier<Set<Class<?>>> {
}
