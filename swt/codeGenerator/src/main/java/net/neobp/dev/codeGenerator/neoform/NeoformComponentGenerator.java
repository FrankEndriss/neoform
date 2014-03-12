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
import net.neobp.dev.codeGenerator.neoform.model.NeoformProperty;

/** Creates a ValueHolder<> implementation which wraps a form.
 * Usefull if a form is used as a Componen, ie. as a subform or created programatically.
 */
public class NeoformComponentGenerator extends NeoformGenerator {

    public void generate() throws IOException {
        PrintWriter pw=null;
        try {
            final String className=getModel().getComponentClassnameTemplate().getGenClassname().getName();
            File file=fileForClass(className);
            pw=new PrintWriter(new FileWriter(file));
            pw.println(getCode());
        }finally{
            if(pw!=null)
                pw.close();
        }
   }
    
    public String getCode() {
        final Classname classname=getModel().getComponentClassnameTemplate().getGenClassname();

        JavaCodeTemplate jct=new JavaCodeTemplate();
        jct.setPackageName(classname.getPackageName());
        jct.setClassName(classname.getSimpleName());
        jct.addImport(getModel().getModelClass().getName());
        jct.addImport("net.neobp.neoform.swt.value.ValueHolder");
        jct.addImport("net.neobp.neoform.swt.value.ValueChangeListener");
        jct.addImport("org.eclipse.swt.widgets.Composite");
        jct.addImport("org.eclipse.swt.widgets.Control");
        jct.addImport("org.eclipse.swt.SWT");
        jct.addImport("net.neobp.neoform.gui.ModelObjectChangeListener"); // ???
        
        jct.addImplements("ValueHolder<"+getModel().getModelClass().getSimpleName()+">");
        
        jct.addPropertyDef("private final Composite control");
        jct.addImport(getModel().getContextClassnameTemplate().getUsrClassName().getName());
        jct.addPropertyDef("private final "+getModel().getContextClassnameTemplate().getUsrClassName().getSimpleName()+" context");
        jct.addImport(getModel().getFormClassnameTemplate().getUsrClassName().getName());
        jct.addPropertyDef("private final "+getModel().getFormClassnameTemplate().getUsrClassName().getSimpleName() +" form");
        jct.addImport(getModel().getControllerClassnameTemplate().getUsrClassName().getName());
        jct.addPropertyDef("private final "+getModel().getControllerClassnameTemplate().getUsrClassName().getSimpleName() +" controller");
        jct.addImport(getModel().getLayoutClassnameTemplate().getUsrClassName().getName());
        jct.addPropertyDef("private final "+getModel().getLayoutClassnameTemplate().getUsrClassName().getSimpleName() +" layout");

        
        jct.addImport(getModel().getModelClass().getName());
        /** Reference to model for event propagation while runtime. */
        jct.addPropertyDef("private "+getModel().getModelClass().getSimpleName()+" model");

        List<String> m=new ArrayList<String>();
        m.add("/** Creates and initializes the "+classname.getSimpleName()+" as a Component/ValueHolder<"+
                getModel().getModelClass().getSimpleName()+">");
        m.add("* @param parent the parent Component");
        m.add("* @param context the pre-created "+getModel().getModelClass().getSimpleName()+" (could be created by spring)");
        m.add("*/");
        m.add("public "+classname.getSimpleName()+"(final Composite parent, final "+
                getModel().getContextClassnameTemplate().getUsrClassName().getSimpleName()+" context) {");
        m.add("\tthis.context=context;");
        m.add("\tthis.control=new Composite(parent, SWT.NONE);");
        m.add("\tform=new "+getModel().getFormClassnameTemplate().getUsrClassName().getSimpleName()+"();");
        m.add("\tcontroller=new "+getModel().getControllerClassnameTemplate().getUsrClassName().getSimpleName()+"();");
        m.add("\tlayout=new "+getModel().getLayoutClassnameTemplate().getUsrClassName().getSimpleName()+"();");
        m.add("\tlayout.setButtonsVisible(false);\n");
            
        m.add("\tform.setContext(context);");
        m.add("\tform.setController(controller);");
        m.add("\tform.setLayout(layout);");
        m.add("\tform.createContents(parent);");
            
        m.add("\t// make sure there is always a reference to the current model object available");
        m.add("\tform.addModelObjectChangeListener(new ModelObjectChangeListener<"+
                getModel().getModelClass().getSimpleName()+">() {");
        m.add("\t\t@Override");
        m.add("\t\tpublic void modelObjectWasSet("+
                getModel().getModelClass().getSimpleName()+" model, boolean isNew) {");
        m.add("\t\t\t"+getModel().getComponentClassnameTemplate().getUsrClassName().getSimpleName()+".this.model=model;");
        m.add("\t\t}");
        m.add("\t});");
        m.add("}");
        jct.addMethod(m);
        
        m=new ArrayList<String>();
        m.add("@Override");
        m.add("public void setValue(final "+getModel().getModelClass().getSimpleName()+" value) {");
        m.add("\tform.setModel(value, false);");
        m.add("}");
        jct.addMethod(m);

        m=new ArrayList<String>();
        m.add("@Override");

        m.add("public void removeValueChangeListener(ValueChangeListener<"+
                getModel().getModelClass().getSimpleName()+"> valueChangeListener) {");
        m.add("\tthrow new RuntimeException(\"still not implemented\");");
        m.add("\t// TODO implement");
        m.add("}");
        jct.addMethod(m);

        m=new ArrayList<String>();
        m.add("@Override");
        m.add("public void addValueChangeListener(final ValueChangeListener<"+
                getModel().getModelClass().getSimpleName()+"> valueChangeListener) {");
        m.add("\t// not sure what a \"value change\" is in this context");
        m.add("\t// could be a \"model object change\", or a \"property of model object change\"");

        for(NeoformProperty prop : getModel().getProperties()) {
            if(prop.isReadonly()) {
                m.add("\t// no ValueChangeListenr for property "+prop.getName()+" because it is readonly");
            } else {
                jct.addImport(prop.getWidget().getEditedValueClassname().getName());
                m.add("\tform.get"+StrUtil.firstUp(prop.getModelPropertyName())+"Adapter().addValueChangeListener(new ValueChangeListener<"+
                    prop.getWidget().getEditedValueClassname().getSimpleName()+">() {");
                m.add("\t\t@Override");
                m.add("\t\tpublic void valueChanged(ValueHolder<"+prop.getWidget().getEditedValueClassname().getSimpleName()+"> src, "+
                    prop.getWidget().getEditedValueClassname().getSimpleName()+" newValue) {");
                m.add("\t\t\tvalueChangeListener.valueChanged("+
                    getModel().getComponentClassnameTemplate().getGenClassname().getSimpleName()+".this, model);");
                m.add("\t\t}");
                m.add("\t});");
            }
        } 

        m.add("\t// propagate model object changes as value changes");
        m.add("\tform.addModelObjectChangeListener(new ModelObjectChangeListener<"+
                getModel().getModelClass().getSimpleName()+">() {");
        m.add("\t\t@Override");
        m.add("\t\tpublic void modelObjectWasSet("+
                getModel().getModelClass().getSimpleName()+" model, boolean isNew) {");
        m.add("\t\t\tvalueChangeListener.valueChanged("+
                getModel().getComponentClassnameTemplate().getGenClassname().getSimpleName()+".this, model);");
        m.add("\t\t}");
        m.add("\t});");
        m.add("}");
        jct.addMethod(m);
        
        m=new ArrayList<String>();
        m.add("@Override");
        m.add("public Control getControl() {");
        m.add("\treturn control;");
        m.add("}");
        jct.addMethod(m);

        return jct.toString();
    }

}
