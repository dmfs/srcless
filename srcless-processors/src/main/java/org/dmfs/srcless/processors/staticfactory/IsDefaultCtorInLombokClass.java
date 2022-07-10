package org.dmfs.srcless.processors.staticfactory;

import org.dmfs.jems2.Predicate;

import javax.lang.model.element.ExecutableElement;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


/**
 * A {@link Predicate} that matches default constructors in classes with Lombok generated constructors.
 */
public final class IsDefaultCtorInLombokClass implements Predicate<ExecutableElement>
{
    @Override
    public boolean satisfiedBy(ExecutableElement testedInstance)
    {
        return testedInstance.getParameters().isEmpty()
            &&
            (testedInstance.getEnclosingElement().getAnnotation(AllArgsConstructor.class) != null
                || testedInstance.getEnclosingElement().getAnnotation(NoArgsConstructor.class) != null
                || testedInstance.getEnclosingElement().getAnnotation(RequiredArgsConstructor.class) != null);
    }
}
