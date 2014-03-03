package net.neobp.dev.codeGenerator.neoform.widget;


/** Meta info for Widget of type "Text"
 */
public class TextWidget extends GenericWidget
{
    public TextWidget() {
        super("net.neobp.neoform.swt.widget.NeoformText", "java.lang.String");
        addConstructorArg("getFormToolkit()");
    }
}
