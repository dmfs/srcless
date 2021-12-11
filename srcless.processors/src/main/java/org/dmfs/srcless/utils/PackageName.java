package org.dmfs.srcless.utils;

import org.dmfs.jems2.single.DelegatingSingle;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;


public final class PackageName extends DelegatingSingle<String>
{
    public PackageName(TypeElement typeElement)
    {
        super(() -> ((PackageElement) typeElement.getEnclosingElement()).getQualifiedName().toString());
    }
}
