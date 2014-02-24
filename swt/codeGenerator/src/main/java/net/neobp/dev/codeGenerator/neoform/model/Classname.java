package net.neobp.dev.codeGenerator.neoform.model;

import net.neobp.dev.codeGenerator.StrUtil;

/** Utility to handle Classnames in Generators
 */
public class Classname
{
    private final NamedForm nameCallback;
    private final String tag;
    private String classname;

    /** @param fullyQualifiedName the name of the class
     */
    public Classname(final String fullyQualifiedName) {
        this(null, null);
        classname=fullyQualifiedName;
    }

    /** Constructor if used with generated names for forms
     * @param nameCallback name of the form
     * @param tag tag of the generated class, i.e. "Controller" or "Form"
     */
    public Classname(final NamedForm nameCallback, final String tag) {
        this.nameCallback=nameCallback;
        this.tag=tag;
    }

    public void setName(String fullyQualifiedClassname) {
        this.classname=fullyQualifiedClassname;
    }
    
    public String getName() {
        if(classname==null)
            return "net.neobp.neo.nmw.neoforms."+StrUtil.firstUp(nameCallback.getFormName())+StrUtil.firstUp(tag);
        return classname;
    }
    
    public String toString() {
        return getName();
    }
    
    public String getSimpleName() {
        int idx=getName().lastIndexOf(".");
        if(idx<0)
            return getName();
        return getName().substring(idx+1);
    }
    
    public String getPackageName() {
        int idx=getName().lastIndexOf(".");
        if(idx<0)
            throw new IllegalStateException("missing package name: "+classname);
        return getName().substring(0, idx);
    }
}
