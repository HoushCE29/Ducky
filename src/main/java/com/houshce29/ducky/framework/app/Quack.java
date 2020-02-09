package com.houshce29.ducky.framework.app;

import com.houshce29.ducky.framework.core.Environment;
import com.houshce29.ducky.framework.core.EnvironmentBuilder;
import com.houshce29.ducky.framework.core.IncludeDefinition;
import com.houshce29.ducky.framework.core.InjectionDefinition;

import java.util.function.Consumer;

/**
 * Quick application => quick app => QUACK!
 *
 * Defines a quick app and either bundles it up for later
 * or runs it immediately.
 */
public class Quack {
    private final Environment environment;

    private Quack(Environment environment) {
        this.environment = environment;
    }

    /**
     * @return The bundled environment for this quick app.
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Runs the application with the given runner.
     * @param mainClass Class to fetch instance from environment.
     * @param runner Consumer that does something with the main instance.
     * @param <T> Main class type.
     */
    public <T> void run(Class<T> mainClass, Consumer<T> runner) {
        T main = environment.get(mainClass);
        runner.accept(main);
    }

    /**
     * @return New builder.
     */
    public static Definer define() {
        return new Definer();
    }

    /**
     * Internal builder.
     */
    public static class Definer {
        private EnvironmentBuilder environmentBuilder;

        private Definer() {
            environmentBuilder = Environment.define();
        }

        /**
         * Defines classes that should be built into objects
         * for the environment. These can have constructors that
         * inject other included objects or built injections. In
         * turn, the injected classes here can be used as dependencies.
         * @param injections Classes to be build into objects.
         * @return This builder.
         */
        public Definer inject(Class<?>... injections) {
            environmentBuilder.inject(injections);
            return this;
        }

        /**
         * Defines objects that are already built to be used in
         * the environment. These can be injected into injections.
         * @param included Pre-built objects.
         * @return This builder.
         */
        public Definer include(Object... included) {
            environmentBuilder.include(included);
            return this;
        }

        /**
         * Convenience definition of included objects. This is used to tidy
         * up the build statements by pushing a bunch of included objects into
         * a different class. The objects are read by creating an instance of
         * the definition then pulling the result of the `get()` method.
         * @param definition External definition of objects to include.
         * @return This builder.
         */
        public Definer includeFrom(Class<? extends IncludeDefinition> definition) {
            environmentBuilder.includeFrom(definition);
            return this;
        }

        /**
         * Convenience definition of injections. This is used to tidy
         * up the build statements by pushing a bunch of injections into
         * a different class. The injections are read by creating an instance of
         * the definition then pulling the result of the `get()` method.
         * @param definition External definition of injections.
         * @return This builder.
         */
        public Definer injectFrom(Class<? extends InjectionDefinition> definition) {
            environmentBuilder.injectFrom(definition);
            return this;
        }

        /**
         * Bundles all of these definitions into a new quick app instance.
         * @return New quick app (Quack) instance.
         */
        public Quack bundle() {
            return new Quack(environmentBuilder.build());
        }

        /**
         * Runs the quick app inline. This is a convenience method and
         * a recommended approach if the environment is to be ran only
         * once and the bundled environment is not needed to be obtained.
         * @param mainClass Main class to run from.
         * @param runner Consumer that does something with an instance of the main class.
         * @param <T> Main class type.
         */
        public <T> void run(Class<T> mainClass, Consumer<T> runner) {
            bundle().run(mainClass, runner);
        }
    }
}
