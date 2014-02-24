package net.neobp.dev.codeGenerator.neoform.widget;

import java.util.ArrayList;
import java.util.List;

import net.neobp.dev.codeGenerator.neoform.model.Classname;
import net.neobp.dev.codeGenerator.neoform.model.Widget;

/** Generic Widget Metadata
 */
public class GenericWidget implements Widget
{
    private Classname classname;
    private List<String> cArgs=new ArrayList<String>();
    private Classname editedValueClassname;

    /** @param widgetClassname Class of the widget
     *  @param editedValueClassname Class of the value that widget does edit/show
     */
    public GenericWidget(String widgetClassname, String editedValueClassname) {
        this.classname=new Classname(widgetClassname);
        this.editedValueClassname=new Classname(editedValueClassname);
    }

    public void setClassname(final String classname) {
        this.classname=new Classname(classname);
    }

    public Classname getClassname()
    {
        return classname;
    }

    public void addConstructorArg(String arg) {
        cArgs.add(arg);
    }

    public String[] getConstructorArgs()
    {
        return cArgs.toArray(new String[0]);
    }

    public void setEditedValueClassname(final String name) {
        this.editedValueClassname=new Classname(name);
    }

    public Classname getEditedValueClassname()
    {
        return editedValueClassname;
    }
}
