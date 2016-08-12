package net.neobp.neoform.swt.widget;

import net.neobp.neoform.exec.NeoformExec;
import net.neobp.neoform.swt.gui.AbstractSwtNeoformComponent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/** Neoform implementation of a Checkbox.
 */
public class NeoformCheckbox extends AbstractSwtNeoformComponent<Boolean>
{
    private final Button checkboxDelegate;

    public NeoformCheckbox(final Composite parent, NeoformExec neoformExec) {
        super(neoformExec);
        checkboxDelegate=new Button(parent, SWT.CHECK);
        
        checkboxDelegate.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e)
            {
                fireValueChanged(checkboxDelegate.getSelection());
            }

            public void widgetDefaultSelected(SelectionEvent e)
            {
                fireValueChanged(checkboxDelegate.getSelection());
            }
        });
    }

    @Override
    protected void value2widget(final Boolean value) {
        checkboxDelegate.setSelection(value);
    }

    @Override
    protected void layout2widget(Object layoutData) {
        checkboxDelegate.setLayoutData(layoutData);
    }

    @Override
    public Control getControl() {
        return checkboxDelegate;
    }

}
