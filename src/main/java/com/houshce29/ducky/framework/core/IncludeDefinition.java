package com.houshce29.ducky.framework.core;

import java.util.Set;
import java.util.function.Supplier;

/**
 * One option of supplying pre-built dependencies to the environment.
 */
public interface IncludeDefinition extends Supplier<Set<Object>> {
}
