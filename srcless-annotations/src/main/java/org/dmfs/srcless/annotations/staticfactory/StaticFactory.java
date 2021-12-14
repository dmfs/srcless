package org.dmfs.srcless.annotations.staticfactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Generate a static factory method for the annotated constructor.
 * <p>
 * Note private constructors will be ignored.
 * <p>
 * This annotation takes precedence over @{@link StaticFactories} and it can be applied to any non-private constructor.
 *
 * <h2>Example</h2>
 * <pre>
 * public final class FactoryFoo
 * {
 *     public FactoryFoo(@Nonnull final String s)
 *     {
 *         this(s, 0);
 *     }
 *
 *     {@literal @}StaticFactory("Foo")
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
 *     public static FactoryFoo factoryFoo(@Nonnull final String s, int i) {
 *         return new FactoryFoo(s,i);
 *     }
 * }
 * }</pre>
 *
 * @see StaticFactories
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.SOURCE)
public @interface StaticFactory
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

    /**
     * The name of the factory method. By default, it's a lower case version of the annotated class' name.
     * <p>
     * Setting this may be necessary when the lower case version of a class name results in a reserved keyword.
     */
    String methodName() default "";
}
