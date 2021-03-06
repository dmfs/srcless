package org.dmfs.srcless.processors.staticfactory;

import javax.lang.model.element.TypeElement;


interface AnnotatedCtor
{
    String targetPackage();

    String targetClass();

    TypeElement clazz();

    CtorDescription description();
}
