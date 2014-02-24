package net.neobp.neoform.value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.neobp.neo.nms.core.validation.ValidationResult;
import net.neobp.neoform.validation.ValidationResultListener;
import net.neobp.neoform.validation.ValidationResultProvider;
import net.neobp.neoform.validation.ValueValidator;

/** Class to adapt a fairly simple ValueHolder<V> to a Neoform.
 * It provides support for a dirtyState, validation and valueChange detection.
 * On value changes in the component (and via resetValue()) events are triggered in that order:
 * -firstValueChangeListener given in the constructor is called
 * -other valueChangeListeners are called
 * -if dirty state changed, dirtyStateListeners are called
 * -validation is performed
 * -validationResultListeners are called
 * 
 * @param C the type of the adapted ValueHolder<V>
 * @param V the type of the edited value (must be same V as in ValueHolder<V>, its only needed for syntactical reasons).
 */
public class ComponentAdapter<C extends ValueHolder<V>, V> implements DirtyProvider, ValueChangeProvider<V>, ValidationResultProvider {
    /** A set of DirtyListeners, called on any change of the dirty state of this component. */
    private Set<DirtyListener> dirtyListeners=new HashSet<DirtyListener>();

    /** A set of ValidationResultListeners, called after any validation. Validation is triggered after any change
     * to the value of the component. */
    private Set<ValidationResultListener> validationResultListeners=new HashSet<ValidationResultListener>();

    /** A set of ValueChangeListeners, called when the value of the component changes. */
    private Set<ValueChangeListener<V>> valueChangeListeners=new HashSet<ValueChangeListener<V>>();

    /** A set of validators called during validation. */
    private Set<ValueValidator<V>> valueValidators=new HashSet<ValueValidator<V>>();

    /** The Component adapted by this adapter. */
    private final C component;
    
    /** A mostly private field to keep track of the last set value (setValue(value))
     * to compute the current dirty state. */
    private V lastSetValue;

    /** This is used together with lastSetValue to determine if a DirtyStateChange occurred or not. */
    private boolean lastSentDirtyState=false;

    /** A super private field used to prevent changeEvents to be sent while in setValue(value) */
    private boolean ignoreChangeEvents=false;

    /** Inner class listener implementation, added to the component given in the constructor.
     */
    private MyValueChangeListener componentListener=new MyValueChangeListener();
    
    /** First triggered ValueChangeListener on value changes, set in constructor. */
    private final ValueChangeListener<V> firstValueChangeListener;
    
    /** Straightforward constructor
     * @param component the adapted component
     * @param a special ValueChangeListener, it is triggered as first listener on change events. Usually this is
     * the listener which sets the new Value on the model object.
     */
    public ComponentAdapter(final C component, ValueChangeListener<V> firstValueChangeListener) {
        this.component=component;
        this.firstValueChangeListener=firstValueChangeListener;
        component.addValueChangeListener(componentListener);
    }

    /** @return the ValueHolder<V> given in the constructor */
    public C getComponent() {
        return component;
    }

    /** Add a value validator to this Adapter. It is called afer any change to this components
     * value. And its result is sent to any ValidationResultListener after any validation.
     * @param validator the validator to use for validation of this components value
     */
    public void addValueValidator(ValueValidator<V> validator) {
        valueValidators.add(validator);
    }

    /** Sets the components value, by getComponent().setValue(value).
     * It behaves like the user had entered the value on screen.
     * @param value the value to be set
     */
    public void setComponentValue(final V value) {
        // TODO add ignoreChangeEvents handling like in resetValue
        // TODO make it so, that value is only set if it differs
        // from the lastSetValue, or at least in this case no event is triggered
        getComponent().setValue(value);
    }

    /** Use this method to set the components value if you use a new model object.
     * Don't use it to programmatically set the components value. In this case 
     * use setComponentValue(value).
     * Calling this method does not trigger any propertyChangeEvent.
     * But it triggers a firing of dirtyListener.isDirty(false), and
     * it does trigger a value validation which will do a call to the ValidationResultListeners.
     * @param value the new value of this component
     */
    public void resetValue(final V value) {
        try {
            ignoreChangeEvents=true;
            lastSetValue=value;
            component.setValue(value);
        }finally {
            ignoreChangeEvents=false;
        }
        fireDirty(false);
        doValueValidation(value);
    }

    public void addDirtyListener(DirtyListener listener)
    {
        dirtyListeners.add(listener);
    }

    public void removeDirtyListener(DirtyListener listener)
    {
        dirtyListeners.remove(listener);
    }
    
    protected void fireDirty(boolean isDirty) {
        for(DirtyListener dl : dirtyListeners)
            dl.isDirty(this, isDirty);
    }

    private class MyValueChangeListener implements ValueChangeListener<V> {
        /** Called by the NeoformComponent.
         * If ignoreChangeEvents is set, this method does nothing.
         * Else it does:
         * -fire a valueChanged event
         * -probably fires a dirtyStateChanged event
         * -does the valueValidations by calling doValueValidation(newValue)
         * @see net.neobp.neoform.value.ValueChangeListener#valueChanged(net.neobp.neoform.value.ValueHolder, java.lang.Object)
         */
        public void valueChanged(ValueHolder<V> src, V newValue)
        {
            if(ignoreChangeEvents)
                return;
        
            fireValueChanged(getComponent(), newValue);

            // TODO check for nulls of newValue and lastSetValue
            if(newValue.equals(lastSetValue) && !lastSentDirtyState) {
                fireDirty(lastSentDirtyState=true);
            } else if(!newValue.equals(lastSetValue) && lastSentDirtyState)
                fireDirty(lastSentDirtyState=false);
        
            doValueValidation(newValue);
        }
    }
    
    /** Performs validation on all ValueValidators and fires List of ValidationResult.
     * @param newValue the new value which is validated
     */
    protected void doValueValidation(final V newValue) {
        final List<ValidationResult> result=new ArrayList<ValidationResult>();
        for(ValueValidator<V> validator : valueValidators) {
            final ValidationResult vr=validator.validateValue(newValue);
            if(vr!=null)
                result.add(vr);
        }

        fireValidationResult(result);
    }

    /** Fires the given result to all registered ValidationResultListeners
     * @param result Collection of validation results (might be empty to indicate "no ValidationResult messages")
     */
    protected void fireValidationResult(final Collection<ValidationResult> result) {
        for(ValidationResultListener l : validationResultListeners)
            l.validationPerformed(this, result);
    }

    public void addValidationResultListener(ValidationResultListener listener)
    {
        validationResultListeners.add(listener);
    }

    public void removeValidationResultListener(ValidationResultListener listener)
    {
        validationResultListeners.remove(listener);
    }

    protected void fireValueChanged(final ValueHolder<V> component, final V newValue) {
        if(firstValueChangeListener!=null)
            firstValueChangeListener.valueChanged(getComponent(), newValue);
        for(ValueChangeListener<V> l : valueChangeListeners)
            l.valueChanged(component, newValue);
    }

    public void addValueChangeListener(ValueChangeListener<V> listener)
    {
        valueChangeListeners.add(listener);
    }

    public void removeValueChangeListener(ValueChangeListener<V> listener)
    {
        valueChangeListeners.remove(listener);
    }
}