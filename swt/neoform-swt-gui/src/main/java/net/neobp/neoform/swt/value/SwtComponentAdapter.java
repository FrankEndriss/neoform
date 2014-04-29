package net.neobp.neoform.swt.value;

import net.neobp.neoform.value.ComponentAdapter;

import org.eclipse.swt.widgets.Control;


public class SwtComponentAdapter<H extends SwtValueHolder<V>, V>
extends ComponentAdapter<V, Control, SwtValueChangeListener<V>>
{
    public SwtComponentAdapter(final H component, final SwtValueChangeListener<V> firstValueChangeListener) {
        super(component, firstValueChangeListener);
    }
}
