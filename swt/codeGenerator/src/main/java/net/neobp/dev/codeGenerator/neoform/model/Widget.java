package net.neobp.dev.codeGenerator.neoform.model;

public interface Widget
{
    public void setClassname(String classname);

    /**
     * @return the classname of the widget
     */
    public Classname getClassname();

    public void addConstructorArg(String arg);

    /**
     * @return prgramatic constructor arguments
     */
    public String[] getConstructorArgs();

    public void setEditedValueClassname(String name);
    
    /**
     * @return the classname of the value this widget can work with
     */
    public Classname getEditedValueClassname();

}
