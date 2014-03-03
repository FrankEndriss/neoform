package net.neobp.dev.codeGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlTag
{
    private final String tag;
    private final Map<String, String> attrs=new HashMap<String, String>();
    private final List<XmlTag> children=new ArrayList<XmlTag>();
    
    public XmlTag(final String tag) {
        this.tag=tag;
    }
    
    public void addAttribute(String name, String value) {
        attrs.put(name, value);
    }
    
    public void addChild(XmlTag child) {
        children.add(child);
    }

    public String toString() {
        return toString("");
    }
    
    public String toString(final String prefix) {
        final StringBuilder sb=new StringBuilder("");
        sb.append(prefix+"<"+tag+" ");
        for(String name : attrs.keySet())
            sb.append("\n\t"+prefix+name+"=\""+attrs.get(name)+"\" ");
        sb.append(">\n");
        
        for(XmlTag child : children)
            sb.append(child.toString(prefix+"\t"));

        sb.append(prefix+"</"+tag+">\n");
        return sb.toString();
    }
}
