package net.neobp.neoform.gui;

/** Interface for objects interested in events indicating that a model object was set on a ModelHolder<M>
 * @param M the class of the model object
 */
public interface ModelObjectChangeListener<M>
{
    /** Called if setModel(M, boolean) was called on a ModelHolder<M>
     * @param model the new model object, may be null
     * @param isNew the flag indicating if this is a newly created buisness object
     */
    public void modelObjectWasSet(M model, boolean isNew);
}
