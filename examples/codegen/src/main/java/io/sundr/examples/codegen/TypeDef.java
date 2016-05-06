/*
 * Copyright 2016 The original authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.sundr.examples.codegen;

import io.sundr.builder.annotations.Buildable;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

@Buildable
public class TypeDef extends ModifierSupport {

    private final Kind kind;
    private final String packageName;
    private final String name;

    private final Set<ClassRef> annotations;
    private final Set<ClassRef> extendsList;
    private final Set<ClassRef> implementsList;
    private final TypeParamDef[] parameters;

    private final Set<Property> properties;
    private final Set<Method> constructors;
    private final Set<Method> methods;

    public TypeDef(Kind kind, String packageName, String name, Set<ClassRef> annotations, Set<ClassRef> extendsList, Set<ClassRef> implementsList, TypeParamDef[] parameters, Set<Property> properties, Set<Method> constructors, Set<Method> methods, int modifiers, Map<String, Object> attributes) {
        super(modifiers, attributes);
        this.kind = kind;
        this.packageName = packageName;
        this.name = name;
        this.annotations = annotations;
        this.extendsList = extendsList;
        this.implementsList = implementsList;
        this.parameters = parameters;
        this.properties = properties;
        this.constructors = constructors;
        this.methods = methods;
    }

    /**
     * Returns the fully qualified name of the type.
     *
     * @return
     */
    public String getFullyQualifiedName() {
        if (packageName != null && !packageName.isEmpty()) {
            return getPackageName() + "." + getName();
        } else {
            return getName();
        }
    }

    public boolean isAssignable(TypeDef o) {
        if (this == o || this.equals(o)) {
            return true;
        }

        if (packageName == null && "java.lang".equals(o.packageName) && name.equalsIgnoreCase(o.name)) {
            return true;
        }
        if (o.packageName == null && "java.lang".equals(packageName) && name.equalsIgnoreCase(o.name)) {
            return true;
        }

        return false;
    }

    public Kind getKind() {
        return kind;
    }

    public Set<ClassRef> getAnnotations() {
        return annotations;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getName() {
        return name;
    }

    public Set<ClassRef> getExtendsList() {
        return extendsList;
    }

    public Set<ClassRef> getImplementsList() {
        return implementsList;
    }

    public TypeParamDef[] getParameters() {
        return parameters;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    public Set<Method> getConstructors() {
        return constructors;
    }

    public Set<Method> getMethods() {
        return methods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeDef type = (TypeDef) o;

        if (packageName != null ? !packageName.equals(type.packageName) : type.packageName != null) return false;
        if (name != null ? !name.equals(type.name) : type.name != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(parameters, type.parameters);

    }

    @Override
    public int hashCode() {
        int result = packageName != null ? packageName.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (parameters != null ? Arrays.hashCode(parameters) : 0);
        return result;
    }

    @Override
    public String toString() {
        return name;
    }
}
