package org.dmfs.srcless.annotations.staticfactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Generate a static factory method for each public constructor of the annotated class.
 *
 * <h2>Example</h2>
 *
 * <pre>
 * {@literal @}StaticFactories("Foo")
 * public final class FactoryFoo
 * {
 *     public FactoryFoo(@Nonnull final String s)
 *     {
 *         this(s, 0);
 *     }
 *
 *
 *     public FactoryFoo(@Nonnull final String s, int i)
 *     {
 *     }
 * }
 * </pre>
 * <p>
 * generates
 *
 * <pre>{@code
 * public final class Foo {
 *     private Foo() {
 *         // no-instances constructor
 *     }
 *
 *     public static FactoryFoo factoryFoo(@Nonnull final String s) {
 *         return new FactoryFoo(s);
 *     }
 *
 *     public static FactoryFoo factoryFoo(@Nonnull final String s, int i) {
 *         return new FactoryFoo(s,i);
 *     }
 * }
 * }</pre>
 *
 * @see StaticFactory
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface StaticFactories
{
    /**
     * The name of the package where to put the class containing the static factory methods.
     * <p>
     * By default, the class will be created in the same package as the annotated class.
     */
    String packageName() default "";

    /**
     * The name of the class containing the static factory methods.
     */
    String value();
}
