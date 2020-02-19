# Ducky
The application bean dependency management system that gets your ducks in a row!

# About
## What is This?
The Ducky library is a light-weight application bean dependency system. This is useful for defining quick service-style applications without the hassle of having to define single instances of classes/services.

## How it Works
The Ducky framework consumes a set of classes and automatically creates instances of them. These are typically easy to think of as services. These injectable services can even be injected into other services.

# How to Use
## Common Terminology
  * **Dependency**: A service that the application and/or other services have a dependency on.
  * **Application**: A quick application (also known as a `Quack` ... ba-dum-tss) that contains an environment, with one service being the entry point.
  * **Environment**: The main sandbox/context that contains all of the dependencies/services.
  
## Defining an Application's Dependencies/Services
There are four ways to define services for a `Quack`:
  1. Defining them using classes.
  2. Defining them using already-built instances.
  3. Defining them by class in an `InjectionDefinition`.
  4. Defining them by instances in an `IncludeDefinition`.
The above can be mixed and repeated in any fashion to create simple and complex applications.

Here is a code snippet of the service/dependency definitions for a `Quack`:
```java
Quack.define()
    .inject(DuttyService.class)
    .include(new GusGusService())
    .injectFrom(CustomInjectionDefinition.class)
    .includeFrom(CustomIncludeDefinition.class)
    .inject(OhWaitTheresMoreService.class, HolyCowEvenMoreService.class)
    .include(new BazService(), new QuuxService(new FooBarBazzer()))
    // . . .

// . . .

public class CustomInjectionDefinition implements InjectionDefinition {
    @Override
    public Set<Class<?>> get() {
        return Collections.singleton(FooService.class);
    }
}

public class CustomIncludeDefinition implements IncludeDefinition {
    @Override
    public Set<Object> get() {
        return Collections.singleton(new BarService());
    }
}
```

## Runing an Application Inline
One common way of running a `Quack` is to define it inline, run it, and be done with it. This can be thought of as a single-time-use application.

Here is a simple inline quick application being ran from an _injected_ service:
```java
Quack.define()
    .inject(Foo.class, Bar.class)
    .include(new Baz(), new Quux(new ThingDoer()))
    .inject(MainApplication.class)
    .run(MainApplication.class, mainApplication -> mainApplication.doSomething());
```

Alternatively, of course, the application can be ran from an _included_ service instance:
```java
Quack.define()
    .inject(Foo.class, Bar.class)
    .include(new Baz(), new Quux(new ThingDoer()))
    .include(new MainApplication())
    .run(MainApplication.class, mainApplication -> mainApplication.doSomething());
```

## Defining a Bundled Application
Another way of running a `Quack` is to bundle it and run it as many times as needed. This can be thought of as a multi-use quick application.

Using the previous example:
```java
Quack myQuickApplication = Quack.define()
    .inject(Foo.class, Bar.class)
    .include(new Baz(), new Quux(new ThingDoer()))
    .inject(MainApplication.class)
    .bundle();

boolean iWantToKeepRunningMyQuack = true;

while (iWantToKeepRunningMyQuack) {
    myQuickApplication.run(MainApplication.class, mainApplication -> mainApplication.doSomething());
}
```

# Building
Run `gradlew clean build` to build.
The `jar` will be generated to `build/libs/ducky.jar`.

# Reporting Issues
Please report any issues or requests to [the Ducky issues page](https://github.com/HoushCE29/Ducky/issues).
