package me.dsl.internal.processor;

import me.codegen.generator.CodeGeneratorBuilder;
import me.codegen.model.JavaClazz;
import me.codegen.model.JavaClazzBuilder;
import me.codegen.model.JavaKind;
import me.codegen.model.JavaMethod;
import me.codegen.utils.ModelUtils;
import me.dsl.annotations.EntryPoint;
import me.dsl.annotations.TargetName;
import me.dsl.internal.functions.FindTransitions;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static me.dsl.internal.processor.DslUtils.*;

@SupportedAnnotationTypes("me.dsl.annotations.Dsl")
public class DslProcessor extends AbstractProcessor {

    public static final String DEFAULT_TEMPLATE_LOCATION = "templates/dsl/dsl.vm";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        Elements elements = processingEnv.getElementUtils();
        Types types = processingEnv.getTypeUtils();
        DslProcessorContext context = new DslProcessorContext(elements, types);

        for (TypeElement annotation : annotations) {
            for (Element element : env.getElementsAnnotatedWith(annotation)) {
                if (element instanceof TypeElement) {
                    TypeElement typeElement = (TypeElement) element;
                    TargetName targetName = element.getAnnotation(TargetName.class);
                    String targetInterface = targetName.value();
                    Set<JavaClazz> interfacesToGenerate = new LinkedHashSet<>();


                    Collection<ExecutableElement> sorted = context.getDependencyManager().sort(ElementFilter.methodsIn(typeElement.getEnclosedElements()));
                    //1st step generate generic interface for all types.
                    Set<JavaClazz> genericInterfaces = createGenericInterfaces(context, sorted);
                    interfacesToGenerate.addAll(genericInterfaces);

                    //2nd step create dependency graph.
                    Set<JavaMethod> methods = new LinkedHashSet<>();
                    Set<Node<ExecutableElement>> graph = createGraph(context, new ArrayList<>(sorted));
                    for (Node<ExecutableElement> node : graph) {
                        ExecutableElement current = node.getItem();
                        methods.add(createEntryPointMethod(context, current));
                        interfacesToGenerate.addAll(createTransitionInterface(context, current, node.getTransitions()));
                    }

                    //Do generate the DSL interface
                    interfacesToGenerate.add(new JavaClazzBuilder().addType()
                            .withPackageName(ModelUtils.getPackageElement(element).toString())
                            .withClassName(targetInterface)
                            .withKind(JavaKind.INTERFACE)
                            .and()
                            .withMethods(methods)
                            .build());


                    try {
                        for (JavaClazz clazz : interfacesToGenerate) {
                            generateFromModel(clazz, processingEnv);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return true;
    }


    private Set<Node<ExecutableElement>> createGraph(DslProcessorContext context, List<ExecutableElement> elements) {
        Set<Node<ExecutableElement>> nodes = new LinkedHashSet<>();
        for (ExecutableElement executableElement : ModelUtils.filterByAnnotation(elements, EntryPoint.class)) {
            Set<AnnotationMirror> transitions = context.getToTransitionAnnotations().apply(executableElement);

            FindTransitions findTransitions = new FindTransitions(context.getElements(), transitions);
            nodes.add(new Node(executableElement, findTransitions.apply(executableElement)));
        }
        return nodes;
    }

    private void generateFromModel(JavaClazz model, ProcessingEnvironment processingEnvironment) throws IOException {
        PackageElement packageElement = processingEnvironment.getElementUtils().getPackageElement(model.getType().getPackageName());
        try {
            generateFromModel(model, processingEnv
                    .getFiler()
                    .createSourceFile(model.getType().getClassName(), packageElement), DEFAULT_TEMPLATE_LOCATION);
        } catch (Exception e) {
            //TODO: Need to avoid dublicate interfaces here.
        }
    }

    /**
     * Generates a source file from the specified {@link me.codegen.model.JavaClazz}.
     *
     * @param model        The model of the class to generate.
     * @param fileObject   Where to save the generated class.
     * @param resourceName Which is the template to use.
     * @throws IOException
     */
    private void generateFromModel(JavaClazz model, JavaFileObject fileObject, String resourceName) throws IOException {
        new CodeGeneratorBuilder<JavaClazz>()
                .withModel(model)
                .withWriter(fileObject.openWriter())
                .withTemplateResource(resourceName)
                .build()
                .generate();
    }

}
