package me.builder.internal.processor.model;

import me.codegen.Clazz;
import me.codegen.Method;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class JavaClazz extends AttributeSupport implements Clazz<JavaType, JavaProperty> {

    private final JavaType type;
    private final Method<JavaType, JavaProperty> constructor;
    private final Set<Method<JavaType, JavaProperty>> methods;
    private final Set<JavaProperty> fields;
    private final Set<JavaType> imports;

    public JavaClazz(JavaType type, Method<JavaType, JavaProperty> constructor, Set<Method<JavaType, JavaProperty>> methods, Set<JavaProperty> fields, Set<JavaType> imports, Map<String, Object> attributes) {
        super(attributes);
        this.type = type;
        this.constructor = constructor;
        this.methods = methods != null ? methods : Collections.<Method<JavaType, JavaProperty>>emptySet();
        this.fields = fields != null ? fields : Collections.<JavaProperty>emptySet();
        this.imports = imports != null ? imports : Collections.<JavaType>emptySet();
    }

    public Method<JavaType, JavaProperty> getConstructor() {
        return constructor;
    }

    @Override
    public JavaType getType() {
        return type;
    }

    @Override
    public Set<Method<JavaType, JavaProperty>> getMethods() {
        return methods;
    }

    @Override
    public Set<JavaProperty> getFields() {
        return fields;
    }

    @Override
    public Set<JavaType> getImports() {
        return imports;
    }
}
