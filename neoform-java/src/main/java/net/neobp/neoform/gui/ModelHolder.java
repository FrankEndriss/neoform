package net.neobp.neoform.gui;


/** Interface for Objects holding Models objects. Like a Neoform.
 */
public interface ModelHolder<M>
{
    /** Sets the model object of this ModelHolder. Note that there is no getModel(), since the model object does not
     * change in another way as of calling this method. However, the models values could (and will) change.
     * Note also that there is no need for the model class M to send change events.
     * However, thru this call no listener will be called. Because it is usually called in between two
     * editing cycles, not while editing a model object.
     * @param model the model object
     * @param isNew flag to indicate that the model object is a new business object or not
     */
    public void setModel(M model, boolean isNew);
    
    /** Add a listener which will be called after any setModel(M) call to this ModelHolder
     * @param modelObjectChangeListener
     */
    public void addModelObjectChangeListener(ModelObjectChangeListener<M> modelObjectChangeListener);

    /**  Removes a previously set modelObjectChangeListener
     * @param modelObjectChangeListener
     */
    public void removeModelObjectChangeListener(ModelObjectChangeListener<M> modelObjectChangeListener);

    public M getModel();
}
