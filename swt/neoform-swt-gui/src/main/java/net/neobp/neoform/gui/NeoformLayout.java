package net.neobp.neoform.gui;

import org.eclipse.swt.widgets.Composite;

/** Interface for Neoforms usefull to do layout things.
 * Since layouts are highly platform dependend this interface seems a bit odd. Make the best out of it.
 * The methods are called while initialisation of a form, calling order is:
 * -layoutParent()
 * then for all properties:
 * -getParentForPropertyLabel()
 * -getLayoutDataForPropertyLabel()
 * -getParentForPorpery()
 * -getLayoutDataForProperty
 * then for all actions:
 * -getParenForButton()
 * -getLayoutDataForButton()
 * In SWT, the return value of getParentForXXX() is used as the parent-Argument in the Constructor
 * of the Widgets. Then, the data returned by getLayoutDataForXXX() is set via setLayoutData().
 */
public interface NeoformLayout<P, A>
{
    /** Called once, should layout the parent composite, usually by setting the layout of the parent
     * and/or adding child composites.
     * @param parent the parent composite which is laid out by this layout
     */
    public void layoutParent(Composite parent);
    
    /**
     * @param name of the button
     * @return the parent for a action/button
     */
    public Composite getParentForButton(A action);
    /**
     * @param name of the button
     * @return a platform dependend layout constraint for this button
     */
    public Object getLayoutDataForButton(A action);

    /**
     * @param prop the property
     * @return the parent for the properties label
     */
    public Composite getParentForPropertyLabel(P prop);
    /**
     * @param prop the property
     * @return a platform depended layout contstraint for the properties label
     */
    public Object getLayoutDataForPropertyLabel(P prop);

    /**
     * @param prop the property
     * @return the parent for the property
     */
    public Composite getParentForProperty(P prop);
    /**
     * @param prop the property
     * @return platform dependend layout constraint for the properties component
     */
    public Object getLayoutDataForProperty(P prop);

    /** Configures the Layout in a way that the Buttons/Actions are not visible.
     * @param visible visible or not visible
     */
    public void setButtonsVisible(boolean visible);
}
