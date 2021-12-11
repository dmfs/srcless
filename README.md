[![Build Status](https://travis-ci.com/dmfs/srcless.svg?branch=master)](https://travis-ci.com/dmfs/srcless)
[![codecov](https://codecov.io/gh/dmfs/srcless/branch/master/graph/badge.svg)](https://codecov.io/gh/dmfs/srcless)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/dmfs/srcless.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/dmfs/srcless/context:java)

# srcless

Generate code too trivial to write it down.

## @Composable

Annotation for interfaces that creates an abstract base class that merely delegates to
another instance of the same type.

### Example

```java
/**
 * FooBar interface.
 */
@Composable
public interface FooBar<T>
{
    void foo(T foo);

    T bar(String bazz) throws IOException;
}
```

generates

```java
/**
 *  FooBar interface.
 * Automatically generated class for composition.
 */
public abstract class FooBarComposition<T> implements FooBar<T> {
    private final FooBar<T> mDelegate;

    public FooBarComposition(FooBar<T> delegate) {
        mDelegate = delegate;
    }

    public final void foo(T foo) {
        mDelegate.foo(foo);
    }

    public final T bar(String bazz) throws IOException {
        return mDelegate.bar(bazz);
    }
}
```

## @StaticFactories, @StaticFactory

Annotation for classes/constructors that generates static factory methods for each public constructor.

### Example
```java
@StaticFactories("Foo")
public final class FactoryFoo
{
    public FactoryFoo(@Nonnull final String s)
    {
        this(s, 0);
    }


    public FactoryFoo(@Nonnull final String s, int i)
    {
    }
}
```

generates

```java
/**
 * Automatically generated class with static factory methods.
 */
public final class Foo {
    private Foo() {
        // no-instances constructor
    }

    public static FactoryFoo factoryFoo(@Nonnull final String s) {
        return new FactoryFoo(s);
    }

    public static FactoryFoo factoryFoo(@Nonnull final String s, int i) {
        return new FactoryFoo(s,i);
    }
}
```

## License

Copyright 2021 dmfs GmbH

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

