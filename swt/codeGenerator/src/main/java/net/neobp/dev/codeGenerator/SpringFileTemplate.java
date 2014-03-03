package net.neobp.dev.codeGenerator;

import java.util.ArrayList;
import java.util.List;

public class SpringFileTemplate
{
    final List<XmlTag> tags=new ArrayList<XmlTag>();

    public SpringFileTemplate() {
        
    }
    
    public void addElement(XmlTag xmlTag) {
        tags.add(xmlTag);
    }
    
    public String toString() {
        final StringBuilder sb=new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<beans xmlns=\"http://www.springframework.org/schema/beans\"\n");
        sb.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
        sb.append("xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd\">\n\n");

        for(XmlTag xmlTag : tags)
            sb.append(xmlTag.toString());
        return  sb.toString();
    }

}
