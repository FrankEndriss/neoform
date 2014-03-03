package net.neobp.dev.codeGenerator.neoform;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


import net.neobp.dev.codeGenerator.JavaCodeTemplate;
import net.neobp.dev.codeGenerator.neoform.model.Classname;

public class NeoformLayoutGenerator extends NeoformGenerator
{
    public void generate() throws IOException
    {
        final Classname className=getModel().getLayoutClassnameTemplate().getGenClassname();
        final File outFile=fileForClass(className.getName());
        JavaCodeTemplate jct=new JavaCodeTemplate();
        jct.setPackageName(className.getPackageName());
        jct.setClassName(className.getSimpleName());
        
        doGenerate(jct);
        PrintWriter pw=new PrintWriter(new FileWriter(outFile));
        pw.print(jct.toString());
        pw.close();
    }

    private void doGenerate(JavaCodeTemplate jct)
    {
        jct.addImport("org.eclipse.draw2d.GridData");
        jct.addImport("org.eclipse.swt.layout.GridLayout");
        jct.addImport("org.eclipse.swt.widgets.Composite");
        jct.addImport("net.neobp.neoform.swt.gui.NeoformLayout");
        
        final Classname clsForm=getModel().getFormClassnameTemplate().getUsrClassName();
        jct.addImport(clsForm.getName());

        final String clsActions=clsForm.getSimpleName()+".Actions";
        final String clsProps=clsForm.getSimpleName()+".Props";

        jct.addImplements("NeoformLayout<"+clsProps+", "+clsActions+">");

        jct.addPropertyDef("private Composite buttonComposite");
        jct.addPropertyDef("private Composite contentComposite");
        jct.addPropertyDef("private boolean buttonsVisible=true");


        List<String> method=new ArrayList<String>();
        method.add("public void layoutParent(Composite parent) {");
        method.add("\tcontentComposite=new Composite(parent, 0);");
        method.add("\tcontentComposite.setLayout(new GridLayout());");
        method.add("\tbuttonComposite=new Composite(parent, 0);");
        method.add("\tbuttonComposite.setLayout(new GridLayout());");
        method.add("\tsetButtonsVisible(buttonsVisible);");
        method.add("}");
        jct.addMethod(method);

        method=new ArrayList<String>();
        method.add("public Composite getParentForButton("+clsActions+" action) {");
        method.add("    return buttonComposite;");
        method.add("}");
        jct.addMethod(method);

        method=new ArrayList<String>();
        method.add("public Object getLayoutDataForButton("+clsActions+" action) {");
        method.add("    return new GridData();");
        method.add("}");
        jct.addMethod(method);

        method=new ArrayList<String>();
        method.add("public Composite getParentForPropertyLabel("+clsProps+" prop) {");
        method.add("    return contentComposite;");
        method.add("}");
        jct.addMethod(method);

        method=new ArrayList<String>();
        method.add("public Object getLayoutDataForPropertyLabel("+clsProps+" prop) {");
        method.add("    return new GridData();");
        method.add("}");
        jct.addMethod(method);

        method=new ArrayList<String>();
        method.add("public Composite getParentForProperty("+clsProps+" prop) {");
        method.add("    return contentComposite;");
        method.add("}");
        jct.addMethod(method);

        method=new ArrayList<String>();
        method.add("public Object getLayoutDataForProperty("+clsProps+" prop) {");
        jct.addMethod(method);
        method.add("    return new GridData();");
        method.add("}");
        
        method=new ArrayList<String>();
        method.add("public void setButtonsVisible(final boolean visible) {");
        method.add("    buttonsVisible=visible;");
        method.add("    if(buttonComposite!=null)");
        method.add("        buttonComposite.setVisible(visible);");
        method.add("}");
        jct.addMethod(method);
    }

}
