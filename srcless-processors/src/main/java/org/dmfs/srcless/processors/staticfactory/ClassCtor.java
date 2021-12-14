package org.dmfs.srcless.processors.staticfactory;

import org.dmfs.srcless.annotations.staticfactory.StaticFactories;
import org.dmfs.srcless.annotations.staticfactory.StaticFactory;
import org.dmfs.jems2.optional.*;
import org.dmfs.jems2.predicate.Not;
import org.dmfs.srcless.utils.PackageName;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;


class ClassCtor implements AnnotatedCtor
{
    private final TypeElement clazz;
    private final ExecutableElement ctor;


    public ClassCtor(TypeElement clazz, ExecutableElement ctor)
    {
        this.clazz = clazz;
        this.ctor = ctor;
    }


    @Override
    public String targetPackage()
    {
        return new FirstPresent<>(
            new Sieved<>(new Not<>(String::isEmpty),
                new Mapped<>(StaticFactory::packageName, new NullSafe<>(ctor.getAnnotation(StaticFactory.class)))),
            new Sieved<>(new Not<>(String::isEmpty),
                new Mapped<>(StaticFactories::packageName, new NullSafe<>(clazz.getAnnotation(StaticFactories.class)))),
            new Just<>(new PackageName(clazz))
        ).value();
    }


    @Override
    public String targetClass()
    {
        return new FirstPresent<>(
            new Sieved<>(new Not<>(String::isEmpty),
                new Mapped<>(StaticFactory::value, new NullSafe<>(ctor.getAnnotation(StaticFactory.class)))),
            new Sieved<>(new Not<>(String::isEmpty),
                new Mapped<>(StaticFactories::value, new NullSafe<>(clazz.getAnnotation(StaticFactories.class))))
        ).value();
    }


    @Override
    public TypeElement clazz()
    {
        return clazz;
    }


    @Override
    public ExecutableElement ctor()
    {
        return ctor;
    }
}
