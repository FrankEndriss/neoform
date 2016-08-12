package net.neobp.neo.nms.forms.orderHeader;

import net.neobp.neoform.exec.NeoformExec;
import net.neobp.neoform.gui.MessageSource;

import org.eclipse.ui.forms.widgets.FormToolkit;

/** CommonContext as Context base class for all forms
 */
public class CommonContext {

    private FormToolkit formToolkit;
    private NeoformExec neoformExec;
    private MessageSource messageSource;

    public FormToolkit getFormToolkit()
    {
        return formToolkit;
    }

    public void setFormToolkit(FormToolkit formToolkit)
    {
        this.formToolkit = formToolkit;
    }

    public NeoformExec getNeoformExec()
    {
        return neoformExec;
    }

    public void setNeoformExec(NeoformExec neoformExec)
    {
        this.neoformExec = neoformExec;
    }

    public MessageSource getMessageSource()
    {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

}
