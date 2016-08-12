package net.neobp.dev.codeGenerator.neoform;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.neobp.dev.codeGenerator.JavaCodeTemplate;

/** Generates the SWT Builder class of a Neoform
 */
public class NeoformBuilderGenerator extends NeoformGenerator {
    public void generate() throws IOException {
            final File outputFile = fileForClass(getModel().getBuilderClassnameTemplate().getGenClassname().getName());
            
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
            
            jct.setPackageName(getModel().getBuilderClassnameTemplate().getGenClassname().getPackageName());
            jct.setClassName(getModel().getBuilderClassnameTemplate().getGenClassname().getSimpleName());

            jct.addImport(getModel().getContextClassnameTemplate().getGenClassname().getName());
            jct.addImport(getModel().getControllerClassnameTemplate().getGenClassname().getName());
            jct.addImport(getModel().getFactoryClassnameTemplate().getGenClassname().getName());
            jct.addImport(getModel().getFormClassnameTemplate().getGenClassname().getName());
            jct.addImport(getModel().getFormClassnameTemplate().getGenClassname().getName()+".Actions");
            jct.addImport(getModel().getFormClassnameTemplate().getGenClassname().getName()+".Props");
            //jct.addImport(getModel().getLayoutClassnameTemplate().getGenClassname().getName());
            jct.addImport("net.neobp.neoform.swt.gui.NeoformLayout");

            jct.addPropertyDef("private NeoformLayout<Props, Actions> layout;");
            jct.addPropertyDef("private "+getModel().getControllerClassnameTemplate().getGenClassname().getSimpleName()+" controller;");
            jct.addPropertyDef("private "+getModel().getFactoryClassnameTemplate().getGenClassname().getSimpleName()+" factory;");

            // Builder Simple Name
            final String bsn=getModel().getBuilderClassnameTemplate().getGenClassname().getSimpleName();

            List<String> m=new ArrayList<String>();
            m.add("/** Sets the layout object for the next createXXX()-calls. Set to null if a new default Layout object");
            m.add("* should be created per createXXX-call.");
            m.add("* @param layout");
            m.add("*/");
            m.add("public "+bsn+" setLayout(final NeoformLayout<Props, Actions> layout) {");
            m.add("\tthis.layout=layout;");
            m.add("\treturn this;");
            m.add("}");
            jct.addMethod(m);
            
            m=new ArrayList<String>();
            m.add("/** Sets the controller object for the next createXXX()-calls. Set to null if a new default controller");
            m.add("* should be created per createXXX()-call.");
            m.add("* @param controller");
            m.add("*/");
            m.add("public "+bsn+
                    " setController(final "+ getModel().getControllerClassnameTemplate().getGenClassname().getSimpleName()+
                    " controller) {");
            m.add("\tthis.controller=controller;");
            m.add("\treturn this;");
            m.add("}");
            jct.addMethod(m);
            
            m=new ArrayList<String>();
            m.add("/** Sets the factory object for the next createXXX()-calls. Set to null if a new default factory object");
            m.add("* should be created per createXXX()-call.");
            m.add("* @param controller");
            m.add("*/");
            m.add("public "+bsn+
                    " setFactory(final "+getModel().getFactoryClassnameTemplate().getGenClassname().getSimpleName()+
                    " factory) {");
            m.add("\tthis.factory=factory;");
            m.add("\treturn this;");
            m.add("}");
            jct.addMethod(m);
     
            // Form Simple Name
            final String fsn=getModel().getFormClassnameTemplate().getGenClassname().getSimpleName();
            final String fun=getModel().getFormClassnameTemplate().getUsrClassName().getSimpleName();
            // Context Simple Name
            final String csn=getModel().getContextClassnameTemplate().getGenClassname().getSimpleName();
            // Layout Simple Name
            final String lsn=getModel().getLayoutClassnameTemplate().getGenClassname().getSimpleName();
            final String lun=getModel().getLayoutClassnameTemplate().getUsrClassName().getSimpleName();
            // Kontroller Simple Name
            final String ksn=getModel().getControllerClassnameTemplate().getGenClassname().getSimpleName();
            final String kun=getModel().getControllerClassnameTemplate().getUsrClassName().getSimpleName();
            // Vactory Simple Name
            final String vsn=getModel().getFactoryClassnameTemplate().getGenClassname().getSimpleName();
            final String vun=getModel().getFactoryClassnameTemplate().getUsrClassName().getSimpleName();

            m=new ArrayList<String>();
            m.add("/** Creates a new "+fun);
            m.add("* @param context the Neoforms context");
            m.add("* @return a new, not initialized, "+fsn+". Call createContents(...) on it to run.");
            m.add("*/");
            m.add("public "+fun+" createForm(final "+csn+" context) {");

            m.add("\tfinal NeoformLayout<Props, Actions> lLayout=layout==null?new "+lun+"():layout;");
            m.add("\tfinal "+ksn+" lController=controller==null?new "+kun+"():controller;");
            m.add("\tfinal "+vsn+" lFactory=factory==null?new "+vun+"():factory;");

            m.add("\tfinal "+fun+" form=new "+fun+"();");
            m.add("\tform.setContext(context);");
            m.add("\tform.setController(lController);");
            m.add("\tform.setLayout(lLayout);");
            m.add("\tform.setFactory(lFactory);");
                
            m.add("\treturn form;");
            m.add("}");
            jct.addMethod(m);
            
            return jct.toString();
        }
}
