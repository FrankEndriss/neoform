package net.neobp.neoform.gui;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.widgets.Composite;

/** Base class for generated Neoforms. The initialisation stuff is handeled here.
 */
public abstract class AbstractNeoform<M> implements ModelHolder<M>
{
    private M model;

    private Set<ModelObjectChangeListener<M>> modelObjectChangeListeners=new HashSet<ModelObjectChangeListener<M>>();

    /** Initializes this Neoform by creating all Components and wiring them
     * to the forms controller.
     * @param parent the parent element
     */
    public abstract void createContents(final Composite parent);
    
    /** Implement this as "simply" put all model values to the component adapters by 
     * ComponentAdapter.resetValue(getModel().getXXX()).
     * Note that model-object getModel() could be null. 
     * If your form is visible while model is null this method should clear all widgets.
     **/
    protected abstract void model2Screen();

    // no constructor needed
    
    /** Implementation of ModelHolder<M>.setModel(M, boolean).
     * The implementation is a bit tricky, because of the probably sent events.
     * While this method is called it is not clear to which model object a change event
     * belongs.
     **/
    public void setModel(final M model, boolean isNew)
    {
        this.model=model;
        fireModelObjectChanged(model, isNew);
        model2Screen(); // fires dirty-state==false
    }
    
    /**
     * @return the last set model object
     */
    public M getModel() {
        return model;
    }
    
    /** Call this to propagate a model object change. It calls all registered ModelObjectChangeListeners.
     * @param model the newly set model object
     * @param isNew indicates that the model object is a new created (not persistent) object, or not. This
     *  usually triggers the enabled-state of the save-Button in respect to the dirty-state.
     */
    protected void fireModelObjectChanged(final M model, boolean isNew) {
        for(ModelObjectChangeListener<M> mocl : modelObjectChangeListeners)
            mocl.modelObjectWasSet(model, isNew);
    }

    public void addModelObjectChangeListener(final ModelObjectChangeListener<M> modelObjectChangeListener)
    {
        modelObjectChangeListeners.add(modelObjectChangeListener);
    }

    public void removeModelObjectChangeListener(final ModelObjectChangeListener<M> modelObjectChangeListener)
    {
        modelObjectChangeListeners.remove(modelObjectChangeListener);
    }
}
