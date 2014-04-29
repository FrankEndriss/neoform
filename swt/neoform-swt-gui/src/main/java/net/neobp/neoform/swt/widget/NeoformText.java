package net.neobp.neoform.swt.widget;


import net.neobp.neoform.exec.NeoformExec;
import net.neobp.neoform.swt.gui.AbstractSwtNeoformComponent;
import net.neobp.neoform.swt.value.SwtValueChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

/** SWT implementation of NeoformText, a simple TextField
 */
public class NeoformText extends AbstractSwtNeoformComponent<String>
{
    /** The SWT delegate for this component. */
    private final Text textDelegate;
    
    /**
     * @param formToolkit The platform dependent Toolkit
     * @param parent The platform dependent parent Composite
     */
    public NeoformText(final FormToolkit formToolkit, final Composite parent, NeoformExec neoformExec) {
        super(neoformExec);
        this.textDelegate=formToolkit.createText(parent, "", SWT.SINGLE | SWT.BORDER);
        
        textDelegate.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                fireValueChanged(textDelegate.getText());
            }
        });
    }

    @Override
    protected void value2widget(final String value)
    {
        textDelegate.setText(value);
    }
    
    @Override
    protected void layout2widget(Object layoutData) {
        textDelegate.setLayoutData(layoutData);
    }

    @Override
    public Control getControl()
    {
        return textDelegate;
    }
}
