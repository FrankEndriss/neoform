package net.neobp.neoform.gui;

import org.eclipse.swt.widgets.Composite;

/** A NeoformLayout usable for rendering the form as a table row within NeoformTable
 */
public class NeoformTableLayout<P, A> implements NeoformLayout<P, A> {

    private final Composite parent;

    public NeoformTableLayout(final Composite parent) {
        this.parent=parent;
    }

    @Override
    public void layoutParent(Composite parent) {
        // empty
    }

    @Override
    public Composite getParentForButton(A action)
    {
        return parent;
    }

    @Override
    public Object getLayoutDataForButton(A action)
    {
        return null;
    }

    @Override
    public Composite getParentForPropertyLabel(P prop)
    {
        return null;
    }

    @Override
    public Object getLayoutDataForPropertyLabel(P prop)
    {
        return null;
    }

    @Override
    public Composite getParentForProperty(P prop)
    {
        return parent;
    }

    @Override
    public Object getLayoutDataForProperty(P prop)
    {
        return null;
    }

    @Override
    public void setButtonsVisible(boolean visible)
    {
        // ignored
    }
}