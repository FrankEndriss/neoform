package net.neobp.neoform.android.gui;

import net.neobp.neoform.exec.NeoformExec;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public abstract class AbstractNeoformComponent<V> extends net.neobp.neoform.gui.AbstractNeoformComponent<V, View, LayoutParams> {
    public AbstractNeoformComponent(NeoformExec neoformExec) {
        super(neoformExec);
    }
}
