package net.neobp.dev.codeGenerator.neoform;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.neobp.dev.codeGenerator.JavaCodeTemplate;
import net.neobp.dev.codeGenerator.StrUtil;
import net.neobp.dev.codeGenerator.neoform.model.NeoformModel;
import net.neobp.dev.codeGenerator.neoform.model.NeoformProperty;

public class NeoformFactoryGenerator extends NeoformGenerator {

    public void generate()
        throws IOException
    {
        final File outputFile = fileForClass(getModel().getFactoryClassnameTemplate().getGenClassname().getName());
        
        PrintWriter pw=null;
        try {
            pw = new PrintWriter(new FileWriter(outputFile));
            pw.println(getCode());
        }
        catch(IntrospectionException e)
        {
            e.printStackTrace();
            throw new RuntimeException("exception while generating", e);
        }finally{
            if(pw!=null)
                pw.close();
        }
    }

    private String getCode() throws IntrospectionException {
        JavaCodeTemplate jct=new JavaCodeTemplate();
        jct.setPackageName(getModel().getFactoryClassnameTemplate().getGenClassname().getPackageName());
        jct.setClassName(getModel().getFactoryClassnameTemplate().getGenClassname().getSimpleName());
        if(getModel().getFactoryClassnameTemplate().getExtClassname()!=null) {
            jct.addImport(getModel().getFactoryClassnameTemplate().getExtClassname().getName());
            jct.setExtends(getModel().getFactoryClassnameTemplate().getExtClassname().getSimpleName());
        }

        jct.addImport(getModel().getModelClass().getName());
        jct.addImport(getModel().getFormClassnameTemplate().getGenClassname().getName());
        jct.addImport(getModel().getFormClassnameTemplate().getGenClassname().getName()+".Props");
        jct.addImport(getModel().getFormClassnameTemplate().getGenClassname().getName()+".Actions");
        jct.addImport(getModel().getContextClassnameTemplate().getGenClassname().getName());
        jct.addImport(getModel().getControllerClassnameTemplate().getGenClassname().getName());
        jct.addImport("net.neobp.neoform.gui.ModelHolder");
        jct.addImport("net.neobp.neoform.swt.value.ComponentAdapter");
        jct.addImport("net.neobp.neoform.swt.value.ValueChangeListener");
        jct.addImport("net.neobp.neoform.value.ValueHolder");
        jct.addImport("net.neobp.neoform.swt.widget.NeoformLabel");
        jct.addImport("net.neobp.neoform.swt.widget.NeoformTable.TableColDesc");
        jct.addImport("net.neobp.neoform.swt.gui.NeoformLayout");
        jct.addImport("net.neobp.neoform.swt.gui.NeoformTableLayout");
        jct.addImport("org.eclipse.swt.widgets.Composite");
        jct.addImport("org.eclipse.swt.widgets.Control");
        jct.addImport("java.util.ArrayList");
        jct.addImport("java.util.List");


        for(NeoformProperty prop : getModel().getProperties()) {
            List<String> m=new ArrayList<String>();
            m.add("public NeoformLabel create"+StrUtil.firstUp(prop.getName())+"Label(final "+
                    getModel().getContextClassnameTemplate().getGenClassname().getSimpleName()+ " context, "+
                    "final Composite parent) {");
            // constructor call to create label
            m.add("\treturn new NeoformLabel(context.getFormToolkit(), parent, "+
                    "context.getMessageSource().getMessage(\""+prop.getLabelKey()+"\"), context.getNeoformExec());");
            m.add("}");
            jct.addMethod(m);
            
            final List<String> imports=new ArrayList<String>();
            final String typeofProp=ClazzUtil.getJavaType(getModel().getModelClass(), prop, imports);
            jct.addImport(getModel().getModelClass().getName());
            for(String imp : imports)
                jct.addImport(imp);
            
            String widgetSimpleName=prop.getWidget().getClassname().getSimpleName();
            final boolean isNeoformTable="NeoformTable".equals(widgetSimpleName);
            // Hack to add type params for NeoformTable
            // TODO: add generic type support to Widget
            if(isNeoformTable) {
                // typeofProp is "Collection<something>"
                // what we need is "something"
                String uncardinalTypeofProp=typeofProp.replaceFirst("Collection<", "");
                uncardinalTypeofProp=uncardinalTypeofProp.substring(0, uncardinalTypeofProp.length()-1);
                widgetSimpleName+=("<"+typeofProp+", "+uncardinalTypeofProp+">");
            }
            /*
            final OrderDocumentTableContext subContext=context.getOrderDocumentTableContext();
            final OrderDocumentTableFactory factory=subContext.getOrderDocumentTableFactory();
            return new NeoformTable<Collection<OrderDocumentViewTransfer>, OrderDocumentViewTransfer>(
                        parent, factory.createTableDesc(subContext));
            */

            m=new ArrayList<String>();
            m.add("public "+widgetSimpleName+" create"+StrUtil.firstUp(prop.getName())+"Control("+
                    getModel().getContextClassnameTemplate().getGenClassname().getSimpleName()+" context, "+
                    "Composite parent) {");
            if(isNeoformTable) {
                NeoformModel subform=getContext().getNeoformCallback().getNeoformModel(prop.getSubform());
                final String subformContextName=subform.getContextClassnameTemplate().getGenClassname().getSimpleName();
                m.add("\t// get the context of the subform");
                m.add("\tfinal "+subformContextName+" subContext=context.get"+StrUtil.firstUp(subformContextName)+"();");
                final String subformFactoryName=subform.getFactoryClassnameTemplate().getGenClassname().getSimpleName();
                m.add("\t// get the factory from the context of the subform");
                m.add("\tfinal "+subformFactoryName+" factory=subContext.get"+StrUtil.firstUp(subformFactoryName)+"();");
            }
            String line="\treturn new "+widgetSimpleName+"(";
            boolean firstArg=true;
            for(String cArg : prop.getWidget().getConstructorArgs()) {
                if(!firstArg)
                    line+=", ";
                line+="context."+cArg;
                firstArg=false;
            }
            if(!firstArg)
                line+=", ";
            if(isNeoformTable)
                line+="parent, factory.createTableDesc(subContext), context.getNeoformExec());";
            else
                line+="parent, context.getNeoformExec());";
            m.add(line);
            m.add("}");
            jct.addMethod(m);
            
            m=new ArrayList<String>();
            jct.addImport(prop.getWidget().getClassname().getName());

            final String adapterType="ComponentAdapter<"+widgetSimpleName+", "+typeofProp+"> ";

            m.add("public "+adapterType+" "+
                    "create"+StrUtil.firstUp(prop.getName())+"Adapter(\n\t\tfinal "+
                    widgetSimpleName+" control,\n\t\tfinal "+
                    getModel().getControllerClassnameTemplate().getGenClassname().getSimpleName()+" controller,\n\t\tfinal "+
                    "ModelHolder<"+getModel().getModelClass().getSimpleName()+"> modelHolder) {");

            if(prop.isReadonly()) {
                m.add("\t// no ValueChangeListener because property is readonly");
                m.add("\tfinal ValueChangeListener<"+typeofProp+"> vcl=null;");
            } else {
                m.add("\tfinal ValueChangeListener<"+typeofProp+"> vcl=new ValueChangeListener<"+typeofProp+">() {");
                m.add("\t\tpublic void valueChanged(ValueHolder<"+typeofProp+", Control> src, "+typeofProp+" newValue) {");
                m.add("\t\t\tfinal "+getModel().getModelClass().getSimpleName()+" model=modelHolder.getModel();");
                m.add("\t\t\tif(model!=null) {");
                if(!"this".equals(prop.getModelPropertyName()))
                        m.add("\t\t\t\tmodel."+ClazzUtil.getSetterMethod(getModel().getModelClass(), prop.getModelPropertyName()).getName()+"(newValue);");
                m.add("\t\t\t\tcontroller.propertyChanged(Props."+prop.getName()+");");
                m.add("\t\t\t}");
                m.add("\t\t}");
                m.add("\t};");
            }
            m.add("\treturn new ComponentAdapter<"+widgetSimpleName+", "+typeofProp+">(control, vcl);");
            m.add("\t// TODO: ValueValidators of property");
            m.add("\t// like: nameAdapter.addValueValidator(valueValidator);");
            m.add("}");
            jct.addMethod(m);


        }
        
        List<String> m=new ArrayList<String>();
        // method to create a table descriptor/handler
        m.add("/** Creates a table descriptor/handler usable with NeoformTable widget.");
        m.add(" * @param context Context object used by all subforms. while runtime, one");
        m.add(" * subform per table-row is created.");
        m.add(" * @return the table descriptor, which is a List of Colunm descriptor");
        m.add(" */");

        // Model Simple Name
        final String msn=getModel().getModelClass().getSimpleName();

        m.add("public List<TableColDesc<"+msn+">> createTableDesc("+
                "final "+getModel().getContextClassnameTemplate().getGenClassname().getSimpleName()+" context) {");
        m.add("\t// list of TableColDesc");
        m.add("\tfinal List<TableColDesc<"+msn+">> list="+
                    "new ArrayList<TableColDesc<"+msn+">>();");

        m.add("\t// list of \"Form per row\" created while runtime");
        m.add("\tfinal ArrayList<"+getModel().getFormClassnameTemplate().getGenClassname().getSimpleName()+
                "> forms=new ArrayList<"+getModel().getFormClassnameTemplate().getGenClassname().getSimpleName()+">();");
            
        int i=0;
        for(NeoformProperty prop : getModel().getProperties()) {
            i++;
            m.add("\t// column"+prop.getName());
            m.add("\tTableColDesc<"+msn+"> col"+i+"=new TableColDesc<"+msn+">() {");

            m.add("\t\t@Override");
            m.add("\t\tpublic String getHeader() {");
            m.add("\t\t\treturn context.getMessageSource().getMessage(\""+prop.getLabelKey()+"\");");
            m.add("\t\t}");

            m.add("public Control createControl(Composite parent, int idx, final "+msn+" model) {");
            m.add("forms.ensureCapacity(idx+1);");
            m.add(getModel().getFormClassnameTemplate().getGenClassname().getSimpleName()+" form=forms.get(idx);");
            m.add("if(form==null) {");
            m.add("// create the form");
            m.add("NeoformLayout<Props, Actions> layout=new NeoformTableLayout<Props, Actions>(parent);");
            m.add("layout.setButtonsVisible(false); // TODO: make programmatically changeable");
            m.add("form=new "+getModel().getBuilderClassnameTemplate().getGenClassname().getSimpleName()+
                    "().setLayout(layout).createForm(context);");
            m.add("form.createContents(parent);");
            m.add("forms.set(idx, form);");
            m.add("}");
            m.add("form.setModel(model, false); // TODO: handle model object creation");
            m.add("return form.get"+StrUtil.firstUp(prop.getName())+"Adapter().getComponent().getControl();");
            m.add("}");

            m.add("@Override");
            m.add("public void model2screen("+msn+" model, int idx) {");
            m.add("// better move whole model to screen: forms.get(idx).setModel(model)");
            m.add("//forms.get(idx).get"+StrUtil.firstUp(prop.getName())+"Adapter().setComponentValue(model.get"+
                    StrUtil.firstUp(prop.getName())+"());");
            m.add("}");
            m.add("};");
            m.add("list.add(col"+i+");");

        }
        m.add("return list;");
        m.add("}");
        jct.addMethod(m);

        return jct.toString();
    }
}
