package net.neobp.dev.codeGenerator.neoform;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.neobp.dev.codeGenerator.JavaCodeTemplate;
import net.neobp.dev.codeGenerator.neoform.model.Classname;

/** This creates the Java code of a class wich wraps a Neoform into an SWT jface IDetailsPage
 */
public class NeoformIPageWrapperGenerator extends NeoformGenerator {

    public void generate() throws IOException
    {
        PrintWriter pw=null;
        try {
            final String className=getModel().getIPageWrapperClassnameTemplate().getGenClassname().getName();
            File file=fileForClass(className);
            pw=new PrintWriter(new FileWriter(file));
            pw.println(getCode());
        }finally{
            if(pw!=null)
                pw.close();
        }
    }
    
    private String getCode() {
        final Classname classname=getModel().getIPageWrapperClassnameTemplate().getGenClassname();

        JavaCodeTemplate jct=new JavaCodeTemplate();
        jct.setPackageName(classname.getPackageName());
        jct.setClassName(classname.getSimpleName());
        jct.addImplements("IDetailsPage");

        jct.addImport(getModel().getModelClass().getName());
        jct.addImport("net.neobp.neoform.value.DirtyListener");
        jct.addImport("net.neobp.neoform.value.DirtyProvider");
        jct.addImport("org.eclipse.jface.viewers.ISelection");
        jct.addImport("org.eclipse.swt.widgets.Composite");
        jct.addImport("org.eclipse.ui.forms.IDetailsPage");
        jct.addImport("org.eclipse.ui.forms.IFormPart");
        jct.addImport("org.eclipse.ui.forms.IManagedForm");

        jct.addPropertyDef("private "+getModel().getContextClassnameTemplate().getUsrClassName().getSimpleName()+" context");
        jct.addImport(getModel().getContextClassnameTemplate().getUsrClassName().getName());
        jct.addPropertyDef("private "+getModel().getFormClassnameTemplate().getUsrClassName().getSimpleName()+ " form");
        jct.addImport(getModel().getFormClassnameTemplate().getUsrClassName().getName());
        //jct.addPropertyDef("private "+getModel().getControllerClassnameTemplate().getUsrClassName().getSimpleName()+" controller");
        jct.addImport(getModel().getControllerClassnameTemplate().getUsrClassName().getName());

        /** Flag used while runtime indicating the dirty state of the current model object */
        jct.addPropertyDef("private boolean dirty=false");

        List<String> m=new ArrayList<String>();
        m.add("/** inject me */");
        m.add("public void setContext(final "+
                getModel().getContextClassnameTemplate().getUsrClassName().getSimpleName()+" context) {");
        m.add("\tthis.context = context;");
        m.add("}");
        jct.addMethod(m);
        
        m=new ArrayList<String>();
        m.add("@Override");
        m.add("public void initialize(final IManagedForm swtForm) {");
        m.add("\tthis.form=new "+getModel().getBuilderClassnameTemplate().getUsrClassName().getSimpleName()+
                "().createForm(context);");
            
        m.add("\tform.getController().addDirtyListener(new DirtyListener() {");
        m.add("\t@Override");
        m.add("\t\tpublic void isDirty(final DirtyProvider src, final boolean isDirty) {");
        m.add("\t\t\tdirty=isDirty;");
        m.add("\t\t}");
        m.add("\t});");
        m.add("\t// TODO add more listeners");
        m.add("}");
        jct.addMethod(m);

        m=new ArrayList<String>();
        m.add("@Override");
        m.add("public void dispose() {");
        m.add("\t// empty");
        m.add("}");
        jct.addMethod(m);

        m=new ArrayList<String>();
        m.add("@Override");
        m.add("public boolean isDirty() {");
        m.add("\treturn dirty;");
        m.add("}");
        jct.addMethod(m);

        m=new ArrayList<String>();
        m.add("@Override");
        m.add("public void commit(boolean onSave) {");
        m.add("\tthrow new RuntimeException(\"commit(boolean onSave) not implemented, onSave:\"+onSave);");
        m.add("}");
        jct.addMethod(m);

        m=new ArrayList<String>();
        m.add("@Override");
        m.add("public boolean setFormInput(Object input) {");
        m.add("\tif(input instanceof "+
                getModel().getModelClass().getSimpleName()+") {");
        m.add("\t\tform.setModel(("+getModel().getModelClass().getSimpleName()+")input, false);");
        m.add("\t\treturn true;");
        m.add("\t}");
        m.add("\treturn false;");
        m.add("}");
        jct.addMethod(m);

        m=new ArrayList<String>();
        m.add("@Override");
        m.add("public void setFocus() {");
        m.add("\t// ignored");
        m.add("}");
        jct.addMethod(m);

        m=new ArrayList<String>();
        m.add("@Override");
        m.add("public boolean isStale() {");
        m.add("\t// ignored");
        m.add("\treturn false;");
        m.add("}");
        jct.addMethod(m);

        m=new ArrayList<String>();
        m.add("@Override");
        m.add("public void refresh() {");
        m.add("\t// ignored");
        m.add("}");
        jct.addMethod(m);

        m=new ArrayList<String>();
        m.add("@Override");
        m.add("public void selectionChanged(IFormPart part, ISelection selection) {");
        m.add("\tif(selection instanceof "+getModel().getModelClass().getSimpleName()+")");
        m.add("\t\tform.setModel(("+getModel().getModelClass().getSimpleName()+")selection, false);");
        m.add("\telse");
        m.add("\t\tthrow new RuntimeException(\"gumpy class in selectionChanged: \"+selection.getClass()+\"\\nit is not "+
                getModel().getModelClass().getSimpleName()+"\");");
        m.add("}");
        jct.addMethod(m);

        m=new ArrayList<String>();
        m.add("@Override");
        m.add("public void createContents(Composite parent) {");
        m.add("\tform.createContents(parent);");
        m.add("}");
        jct.addMethod(m);
        return jct.toString();
    }

}
