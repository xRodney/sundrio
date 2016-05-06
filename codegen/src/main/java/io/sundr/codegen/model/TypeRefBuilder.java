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

package io.sundr.codegen.model;

import io.sundr.builder.VisitableBuilder;

public class TypeRefBuilder extends TypeRefFluentImpl<TypeRefBuilder> implements VisitableBuilder<TypeRef, TypeRefBuilder> {

    TypeRefFluent<?> fluent;

    public TypeRefBuilder() {
        this.fluent = this;
    }

    public TypeRefBuilder(TypeRefFluent<?> fluent) {
        this.fluent = fluent;
    }

    public TypeRefBuilder(TypeRefFluent<?> fluent, TypeRef instance) {
        this.fluent = fluent;
        fluent.withDefinition(instance.getDefinition());
        fluent.withDimensions(instance.getDimensions());
        fluent.withArguments(instance.getArguments());
        fluent.withAttributes(instance.getAttributes());
    }

    public TypeRefBuilder(TypeRef instance) {
        this.fluent = this;
        this.withDefinition(instance.getDefinition());
        this.withDimensions(instance.getDimensions());
        this.withArguments(instance.getArguments());
        this.withAttributes(instance.getAttributes());
    }

    public EditableTypeRef build() {
        EditableTypeRef buildable = new EditableTypeRef(fluent.getDefinition(), fluent.getDimensions(), fluent.getArguments(), fluent.getAttributes());
        validate(buildable);
        return buildable;

    }

    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TypeRefBuilder that = (TypeRefBuilder) o;
        if (fluent != null && fluent != this ? !fluent.equals(that.fluent) : that.fluent != null && fluent != this)
            return false;
        return true;

    }

    private <T> void validate(T item) {
    }


}
    
