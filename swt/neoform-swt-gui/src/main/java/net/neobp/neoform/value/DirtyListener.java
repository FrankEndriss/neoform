package net.neobp.neoform.value;


/** Interface for most likely parents of editable gui components to keep track of their childrens dirty state.
 */
public interface DirtyListener
{
    /** Called whenever the dirty state of the DirtyProvider changes
     * @param src the DirtyProvider whichs dirty state has changed
     * @param isDirty the new State of the dirty provider
     */
    public void isDirty(DirtyProvider src, boolean isDirty);
}
