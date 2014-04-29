package net.neobp.neoform.swt.gui;

import net.neobp.neoform.exec.NeoformExec;
import net.neobp.neoform.gui.AbstractNeoformComponent;
import net.neobp.neoform.swt.value.SwtValueChangeListener;
import net.neobp.neoform.swt.value.SwtValueHolder;

import org.eclipse.swt.widgets.Control;

public abstract class AbstractSwtNeoformComponent<V> 
extends AbstractNeoformComponent<V, Control, Object, SwtValueChangeListener<V>> 
implements SwtValueHolder<V> {

    public AbstractSwtNeoformComponent(final NeoformExec neoformExec) {
        super(neoformExec);
    }
}
