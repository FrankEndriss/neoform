package net.neobp.dev.codeGenerator.neoform;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.neobp.dev.codeGenerator.CodeGenerator;
import net.neobp.dev.codeGenerator.JavaCodeTemplate;

/** This generator creates a Proxy for a given model class.
 * The Proxy supports PropertyChangeEvents and DirtyStateProvider
 * Unused.
 */
public class ModelProxyGenerator implements CodeGenerator
{
    private final Class<?> modelClass;

    public ModelProxyGenerator(Class<?> modelClass, File outputDir) {
        this.modelClass=modelClass;
    }

    public void generate() throws IOException
    {
        JavaCodeTemplate jct=new JavaCodeTemplate();
        final String proxyClassName=modelClass.getSimpleName()+"Proxy";
        jct.setClassName(proxyClassName);
        jct.setPackageName("net.neobp.neoform.models");
        jct.addImport(modelClass.getPackage().getName());
        
        jct.addPropertyDef("final "+modelClass.getSimpleName()+" delegate;");
     
        final List<String> constructor=new ArrayList<String>();
        constructor.add("public "+proxyClassName+"("+modelClass.getSimpleName()+" delegate) {");
        constructor.add("\tthis.delegate=delegate;");
        constructor.add("}");
        jct.addMethod(constructor);
        

        BeanInfo info;
        try
        {
            info = Introspector.getBeanInfo(modelClass);
        }
        catch(IntrospectionException e)
        {
            throw new RuntimeException("Exception while introspection", e);
        }

        final PropertyDescriptor[] descriptors=info.getPropertyDescriptors();
        for(PropertyDescriptor pd : descriptors) {
            final Method getter=pd.getReadMethod();
            if(getter!=null) {
                final List<String> getterCode=new ArrayList<String>();
                String line="public "+getter.getReturnType().getName()+" "+getter.getName()+"() {";
                getterCode.add(line);
                
                line="\treturn delegate."+getter.getName()+"()";
                getterCode.add(line);
                getterCode.add("}");
                jct.addMethod(getterCode);
            }

            final Method setter=pd.getWriteMethod();
            if(setter!=null) {
                final List<String> setterCode=new ArrayList<String>();
                String line="public void "+setter.getName()+"(";
                int i=1;
                for(Class<?> argClass : getter.getParameterTypes()) {
                    if(i>1)
                        line+=", ";
                    line+=argClass.getName()+" arg"+i;
                    i++;
                }
                line+=") {";
                setterCode.add(line);
                
                line="\tdelegate."+setter.getName()+"(";
                for(Class<?> argClass : getter.getParameterTypes()) {
                    if(i>1)
                        line+=", ";
                    line+=" arg"+i;
                    i++;
                }
                line+=");";
                setterCode.add(line);
                setterCode.add("}");
                jct.addMethod(setterCode);
            }
        }
    }

}
