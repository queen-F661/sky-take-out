package com.sky.annoation;

import com.sky.enumeration.OperationType;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
     OperationType value();
}
