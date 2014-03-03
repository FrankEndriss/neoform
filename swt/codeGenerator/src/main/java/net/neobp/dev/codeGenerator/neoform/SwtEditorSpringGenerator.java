package net.neobp.dev.codeGenerator.neoform;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import net.neobp.dev.codeGenerator.SpringFileTemplate;
import net.neobp.dev.codeGenerator.XmlTag;
import net.neobp.dev.codeGenerator.neoform.model.NeoformContextField;

/** Generator to generate a spring beans definiton file.
 * It defines the injections of the context object of the neoform.
 */
public class SwtEditorSpringGenerator extends NeoformGenerator {

    public void generate() throws IOException
    {
        File lOutDir=new File(getOutputDir(), "META-INF/spring");
        lOutDir.mkdirs();
        final File springFile=new File(lOutDir, getModel().getFormName()+"-spring.xml");
        System.out.println("writing spring to: "+springFile.getAbsolutePath());

        final SpringFileTemplate sFile=new SpringFileTemplate();
        
        sFile.addElement(createContextTag());
        sFile.addElement(createFactoryTag());
        sFile.addElement(createIPageTag());

        PrintWriter pw=new PrintWriter(new FileWriter(springFile));
        pw.println(sFile.toString());
        pw.close();
        System.out.println("written spring to: "+springFile.getAbsolutePath());
    }

    public XmlTag createIPageTag() {
        final XmlTag iPageTag=new XmlTag("bean");
        iPageTag.addAttribute("id", getModel().getIPageWrapperClassnameTemplate().getSpringName());
        iPageTag.addAttribute("class", getModel().getIPageWrapperClassnameTemplate().getUsrClassName().getName());
        iPageTag.addAttribute("scope", "prototype");
        
        XmlTag contextRef=new XmlTag("property");
        contextRef.addAttribute("name", "context");
        contextRef.addAttribute("ref", getModel().getContextClassnameTemplate().getSpringName());
        iPageTag.addChild(contextRef);
        
        return iPageTag;
    }

    public XmlTag createFactoryTag() {
        final XmlTag factoryTag=new XmlTag("bean");
        factoryTag.addAttribute("id", getModel().getFactoryClassnameTemplate().getSpringName());
        factoryTag.addAttribute("class", getModel().getFactoryClassnameTemplate().getUsrClassName().getName());
        factoryTag.addAttribute("scope", "singleton");
        return factoryTag;
    }

    public XmlTag createContextTag() {
        final XmlTag contextTag=new XmlTag("bean");
        contextTag.addAttribute("id", getModel().getContextClassnameTemplate().getSpringName());
        contextTag.addAttribute("class", getModel().getContextClassnameTemplate().getUsrClassName().getName());
        contextTag.addAttribute("scope", "singleton");
        
        for(NeoformContextField field : getModel().getContextFields()) {
            final XmlTag prop=new XmlTag("property");
            prop.addAttribute("name", field.getFieldName());
            prop.addAttribute("ref", field.getBeanRef());
            contextTag.addChild(prop);
        }
        
        return contextTag;
    }
}
