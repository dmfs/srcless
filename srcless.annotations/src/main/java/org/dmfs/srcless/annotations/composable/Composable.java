package org.dmfs.srcless.annotations.composable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Generate an abstract class for compositions.
 * <p>
 * The resulting class is abstract. Its constructor takes an instance of the same type and each method is final and delegates to the same
 * method of the delegate.
 *
 * <h2>Example</h2>
 *
 * <pre>
 * {@literal @}Composable
 *  public interface FooBar&lt;T&gt;
 *  {
 *      void foo(T foo);
 *
 *      T bar(String bazz) throws IOException;
 *  }
 * </pre>
 * <p>
 * will generate this code
 *
 * <pre>{@code
 * public abstract class FooBarComposition<T> implements FooBar<T> {
 *     private final FooBar<T> mDelegate;
 *
 *     public FooBarComposition(FooBar<T> delegate) {
 *         mDelegate = delegate;
 *     }
 *
 *     public final void foo(T foo) {
 *         mDelegate.foo(foo);
 *     }
 *
 *     public final T bar(String bazz) throws IOException {
 *         return mDelegate.bar(bazz);
 *     }
 * }
 * }</pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Composable
{
    /**
     * The package name of the generated class.
     * <p>
     * Defaults to the package name of the annotated interface.
     */
    String packageName() default "";

    /**
     * The name of the generated class.
     * <p>
     * Defaults to the name of the annotated interface plus the word "Composition".
     */
    String className() default "";
}
