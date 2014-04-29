package net.neobp.dev.codeGenerator.neoform;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.neobp.dev.codeGenerator.JavaCodeTemplate;
import net.neobp.dev.codeGenerator.StrUtil;
import net.neobp.dev.codeGenerator.neoform.model.Classname;
import net.neobp.dev.codeGenerator.neoform.model.NeoformContextField;

/** Generates the Context-Class for a neoform.
 * The Context-Class is a fairly simple class with fields/getters/setters to external objects, configured in the 
 * forms configuration.
 */
public class NeoformContextGenerator extends NeoformGenerator {

    public void generate() throws IOException {

        PrintWriter pw=null;
        try {
            final String className=getModel().getContextClassnameTemplate().getGenClassname().getName();
            File file=fileForClass(className);
            pw=new PrintWriter(new FileWriter(file));
            pw.println(getCode());
        }finally{
            if(pw!=null)
                pw.close();
        }
    }
    
    private String getCode() {
        final Classname classname=getModel().getContextClassnameTemplate().getGenClassname();

        final JavaCodeTemplate jct=new JavaCodeTemplate();
        jct.setPackageName(classname.getPackageName());
        jct.setClassName(classname.getSimpleName());

        if(getModel().getContextClassnameTemplate().getExtClassname()!=null) {
            jct.addImport(getModel().getContextClassnameTemplate().getExtClassname().getName());
            jct.setExtends(getModel().getContextClassnameTemplate().getExtClassname().getSimpleName());
        }

        for(NeoformContextField contextField : getModel().getContextFields()) {
            Classname fieldClassname=contextField.getFieldClassname();
            if(fieldClassname==null) {
                // SpringCallback usage disabled per 2014-04-29
                throw new RuntimeException("fieldClassname unknown, form="+getModel().getFormName()+" "+"field="+contextField.getFieldName());
                //fieldClassname=new Classname(getContext().getSpringCallback().getType(contextField.getBeanRef()).getName());
            }
            jct.addImport(fieldClassname.getName());
            jct.addPropertyDef("private "+fieldClassname.getSimpleName()+" "+contextField.getFieldName());
            
            List<String> mGetter=new ArrayList<String>();
            mGetter.add("public "+fieldClassname.getSimpleName()+" get"+StrUtil.firstUp(contextField.getFieldName())+"() {");
            mGetter.add("\treturn "+contextField.getFieldName()+";");
            mGetter.add("}");
            jct.addMethod(mGetter);

            List<String> mSetter=new ArrayList<String>();
            mSetter.add("public void set"+StrUtil.firstUp(contextField.getFieldName())+"(final "+
                    fieldClassname.getSimpleName()+" "+contextField.getFieldName()+") {");
            mSetter.add("\tthis."+contextField.getFieldName()+"="+contextField.getFieldName()+";");
            mSetter.add("}");
            jct.addMethod(mSetter);
        }

        return jct.toString();
    }
}
