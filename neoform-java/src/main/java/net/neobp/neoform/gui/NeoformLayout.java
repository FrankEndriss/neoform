package net.neobp.neoform.gui;

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
 * @param P the class of objects identifying a Property, usually an enum
 * @param A the class of objects identifying an Action, usually an enum
 * @param C the platform dependent Composite class
 * @param L the platform dependent LayoutData class
 */
public interface NeoformLayout<P, A, C, L>
{
    /** Called once, should layout the parent composite, usually by setting the layout of the parent
     * and/or adding child composites.
     * @param parent the parent composite which is laid out by this layout
     */
    public void layoutParent(C parent);
    
    /**
     * @param name of the button
     * @return the parent for a action/button
     */
    public C getParentForButton(A action);
    /**
     * @param name of the button
     * @return a platform dependend layout constraint for this button
     */
    public L getLayoutDataForButton(A action);

    /**
     * @param prop the property
     * @return the parent for the properties label
     */
    public C getParentForPropertyLabel(P prop);
    /**
     * @param prop the property
     * @return a platform depended layout contstraint for the properties label
     */
    public L getLayoutDataForPropertyLabel(P prop);

    /**
     * @param prop the property
     * @return the parent for the property
     */
    public C getParentForProperty(P prop);
    /**
     * @param prop the property
     * @return platform dependend layout constraint for the properties component
     */
    public L getLayoutDataForProperty(P prop);

    /** Configures the Layout in a way that the Buttons/Actions are not visible.
     * @param visible visible or not visible
     */
    public void setButtonsVisible(boolean visible);
}
