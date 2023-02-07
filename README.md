[![Build](https://github.com/dmfs/srcless/actions/workflows/main.yml/badge.svg?label=main)](https://github.com/dmfs/srcless/actions/workflows/main.yml)
[![codecov](https://codecov.io/gh/dmfs/srcless/branch/main/graph/badge.svg)](https://codecov.io/gh/dmfs/srcless)
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
public abstract class FooBarComposition<T> implements FooBar<T>
{
    private final FooBar<T> mDelegate;


    public FooBarComposition(FooBar<T> delegate)
    {
        mDelegate = delegate;
    }


    public final void foo(T foo)
    {
        mDelegate.foo(foo);
    }


    public final T bar(String bazz) throws IOException
    {
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
public final class Foo
{
    private Foo()
    {
        // no-instances constructor
    }


    public static FactoryFoo factoryFoo(@Nonnull final String s)
    {
        return new FactoryFoo(s);
    }


    public static FactoryFoo factoryFoo(@Nonnull final String s, int i)
    {
        return new FactoryFoo(s, i);
    }
}
```

## nullless

The nullless annotation processor doesn't process any annotations.
It processes all packages in the project and generates a
`package-info.java` like this:

```Java
@NonNullByDefault
package

<packagename>

import org.eclipse.jdt.annotation.NonNullByDefault;
```

As the name says this makes everything in a package `NonNull` by default.

Applying the annotation processor like

```groovy
annotationProcessor 'org.dmfs:nullless-processors:0.3.0'
```

Applies the `@NonNullByDefault` annotation to all classes in the project.

To make this work you must also add a dependency on the respective eclipse annotation lib like so

```groovy
annotationProcessor 'org.dmfs:nullless-processors:0.3.0'
api 'org.eclipse.jdt:org.eclipse.jdt.annotation:2.2.600'
```

Because this applied automatically on all packages, it's not part of the scrless-processors artifact and has to be applied separately.

Note that you still can annotate members, methods and parameters with
`@NonNull` to override the default.

## License

Copyright 2023 dmfs GmbH

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

