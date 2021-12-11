package org.dmfs.srcless.utils;

import org.dmfs.jems2.iterable.DelegatingIterable;
import org.dmfs.jems2.iterable.Mapped;
import org.dmfs.jems2.iterable.Sieved;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;


public final class Constructors extends DelegatingIterable<ExecutableElement>
{
    public Constructors(TypeElement typeElement)
    {
        super(new Mapped<>(element -> (ExecutableElement) element,
            new Sieved<>(element -> element.getKind() == ElementKind.CONSTRUCTOR,
                typeElement.getEnclosedElements())));
    }
}
