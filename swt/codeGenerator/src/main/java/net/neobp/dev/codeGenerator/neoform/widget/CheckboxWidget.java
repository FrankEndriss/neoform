package net.neobp.dev.codeGenerator.neoform.widget;


public class CheckboxWidget extends GenericWidget
{
    public CheckboxWidget() {
        super("net.neobp.neoform.swt.widget.NeoformCheckbox", "java.lang.Boolean");
        addConstructorArg("getImageRegistryUtil()");
    }
}
