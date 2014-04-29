package net.neobp.neoform.swt.widget;


import net.neobp.neoform.exec.NeoformExec;
import net.neobp.neoform.swt.gui.AbstractSwtNeoformComponent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

/** A Label implementation, does not send any events. */
public class NeoformLabel extends AbstractSwtNeoformComponent<String>
{
    private Label labelDelegate;
    
    public NeoformLabel(final FormToolkit formToolkit, final Composite parent, final String text, NeoformExec neoformExec) {
        super(neoformExec);
        this.labelDelegate=formToolkit.createLabel(parent, text, SWT.LEFT);
    }

    @Override
    protected void value2widget(final String value) {
        labelDelegate.setText(value);
    }

    @Override
    protected void layout2widget(Object layoutData) {
        labelDelegate.setLayoutData(layoutData);
    }

    @Override
    public Control getControl() {
        return labelDelegate;
    }
    
}
