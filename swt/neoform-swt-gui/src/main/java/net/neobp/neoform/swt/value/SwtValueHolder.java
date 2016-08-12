package net.neobp.neoform.swt.value;

import net.neobp.neoform.value.ValueHolder;

import org.eclipse.swt.widgets.Control;

public interface SwtValueHolder<V> extends ValueHolder<V, Control, SwtValueChangeListener<V>> {
}
