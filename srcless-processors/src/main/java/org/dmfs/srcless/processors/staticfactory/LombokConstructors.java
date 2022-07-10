package org.dmfs.srcless.processors.staticfactory;

import org.dmfs.jems2.iterable.DelegatingIterable;
import org.dmfs.jems2.iterable.Mapped;
import org.dmfs.jems2.iterable.Seq;
import org.dmfs.jems2.iterable.Sieved;

import javax.lang.model.element.TypeElement;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;


public final class LombokConstructors extends DelegatingIterable<AnnotatedCtor>
{
    public LombokConstructors(TypeElement typeElement)
    {
        super(new Mapped<>(annotation -> new LombokAllArgsCtor(typeElement),
            new Sieved<>(annotation -> annotation.access() == AccessLevel.PUBLIC && annotation.staticName().isEmpty(),
                new Seq<>(typeElement.getAnnotationsByType(AllArgsConstructor.class)))));
    }
}