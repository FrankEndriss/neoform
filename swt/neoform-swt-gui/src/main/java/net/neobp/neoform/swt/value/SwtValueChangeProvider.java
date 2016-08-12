package net.neobp.neoform.swt.value;

import net.neobp.neoform.value.ValueChangeProvider;

import org.eclipse.swt.widgets.Control;

public interface SwtValueChangeProvider<V> extends ValueChangeProvider<V, Control, SwtValueChangeListener<V>> 
{
}
