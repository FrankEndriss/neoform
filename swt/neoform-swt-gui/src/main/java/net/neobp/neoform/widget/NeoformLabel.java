package net.neobp.neoform.widget;


import net.neobp.neoform.gui.AbstractNeoformComponent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

/** A Label implementation, does not send any events. */
public class NeoformLabel extends AbstractNeoformComponent<String>
{
    private Label labelDelegate;
    
    public NeoformLabel(final FormToolkit formToolkit, final Composite parent, final String text) {
        this.labelDelegate=formToolkit.createLabel(parent, text, SWT.LEFT);
    }

    @Override
    protected void value2widget(final String value)
    {
        labelDelegate.setText(value);
    }

    @Override
    public void setLayoutData(Object layoutData) {
        labelDelegate.setLayoutData(layoutData);
    }

    @Override
    public Control getControl()
    {
        return labelDelegate;
    }
    
}
