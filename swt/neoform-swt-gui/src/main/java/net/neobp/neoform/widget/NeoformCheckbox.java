package net.neobp.neoform.widget;

import net.neobp.neo.nms.view.ImageRegistryUtil;
import net.neobp.neo.nms.view.swt.control.custom.NmsCustomCheckbox;
import net.neobp.neoform.adapter.NeoformContext;
import net.neobp.neoform.gui.AbstractNeoformComponent;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/** Neoform implementation of a Checkbox.
 */
public class NeoformCheckbox extends AbstractNeoformComponent<Boolean>
{
    private final NmsCustomCheckbox checkboxDelegate;

    public NeoformCheckbox(final ImageRegistryUtil imageRegistryUtil, final Composite parent) {
        checkboxDelegate=new NmsCustomCheckbox(parent, imageRegistryUtil);
        
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
    protected void value2widget(final Boolean value)
    {
        checkboxDelegate.setCustomSelection(value);
    }

    @Override
    public void setLayoutData(Object layoutData)
    {
        checkboxDelegate.setLayoutData(layoutData);
    }

    @Override
    public Control getControl()
    {
        return checkboxDelegate;
    }

}
