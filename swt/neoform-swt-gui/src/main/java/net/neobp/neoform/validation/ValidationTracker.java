package net.neobp.neoform.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import net.neobp.neo.nms.core.validation.ValidationMessage;
import net.neobp.neo.nms.core.validation.ValidationResult;

/** An object of this class keeps track of the state of validations on a form.
 * The state of an Validation is summarized in an ValidationState object.
 * Whenever this ValidationState object changes, the ValidationStateListeners are called.
 * There are two kinds of ValidationState.
 * -isValid(), usefull to trigger the enabled-State of a save-Button or the like
 * -validationPerformed(), a List of validation-Messages created while validation. Usefull for displaying
 *  info on screen.
 */
public class ValidationTracker implements ValidationResultListener {
    /** listeners of this tracker */
    private final Set<ValidationStateListener> validationStateListeners=new HashSet<ValidationStateListener>();
    
    /** Map of Providers to ValidationResults */
    private final Map<ValidationResultProvider, Collection<ValidationResult>> resultMap=
            new HashMap<ValidationResultProvider, Collection<ValidationResult>>();

    /** Count of current invalid validators. */
    private final AtomicInteger invalidCounter=new AtomicInteger(0);
    
    /** Usually this is called while the model object changes, since the validation state
     * always belongs to the current model object. If the current model object is set to
     * another model object, the old validation state has to be thrown away, too.
     * This fires a validationState of true, and and empty List of ValidationMessages.
     */
    public void clear() {
        invalidCounter.set(0);
        for(ValidationResultProvider vrp : resultMap.keySet())
            resultMap.put(vrp, null);
        fireValidState(true);
        fireMessagesChanged(new ArrayList<Collection<ValidationMessage>>()); // fire empty list
    }
    
    /** Configure a ValidationResultProvider to send its results to this Tracker.
     * @param validationResultProvider the tracked executor of validations.
     */
    public void trackValidations(final ValidationResultProvider validationResultProvider) {
        validationResultProvider.addValidationResultListener(this);

        // not really needed, but indeed mark this provider as registered and result state unknown/null
        synchronized(resultMap) {
            resultMap.put(validationResultProvider, null);
        }
    }

    public void validationPerformed(final ValidationResultProvider src, final Collection<ValidationResult> newResult)
    {
        final Collection<ValidationResult> oldResult;
        synchronized(resultMap) { // src could send results in parallel for some reason
            oldResult=resultMap.get(src);
            resultMap.put(src, newResult);
        }

        checkResultStates(oldResult, newResult);
        checkResultMessages(oldResult, newResult);
    }

    private void checkResultStates(final Collection<ValidationResult> oldResult, final Collection<ValidationResult> newResult) {

        // null/empty result is considered to be valid
        // TODO ValidationStrategy
        boolean oldResultValid=true;
        for(ValidationResult vr : oldResult) {
            if(!vr.isEmpty()) {
                oldResultValid=false;
                break;
            }
        }
        
        boolean newResultValid=true;
        for(ValidationResult vr : newResult) {
            if(!vr.isEmpty()) {
                newResultValid=false;
                break;
            }
        }
        
        if(oldResultValid && !newResultValid) {
            if(invalidCounter.incrementAndGet()==1)
                fireValidState(false);
        } else if(!oldResultValid && newResultValid) {
            if(invalidCounter.decrementAndGet()==0)
                fireValidState(true);
        }
    }
    
    private void checkResultMessages(final Collection<ValidationResult> oldResult, final Collection<ValidationResult> newResult) {
        final List<ValidationMessage> oldResultMessages=new ArrayList<ValidationMessage>();
        for(ValidationResult vr : oldResult)
            oldResultMessages.addAll(vr.getMessages());

        final List<ValidationMessage> newResultMessages=new ArrayList<ValidationMessage>();
        for(ValidationResult vr : newResult)
            newResultMessages.addAll(vr.getMessages());
        
        // TODO hope Collection<ValidationMessage> is comparable by equals() in a usefull sense
        // if not, a Collection.sort() on oldResultMessages and newResultMessages() should do the trick
        if(!oldResultMessages.equals(newResultMessages))
            fireMessagesChanged(collectCurrentMessages());
    }
    
    /** This collects all currently available ValidationMessages from the currently availabe
     * ValidationResults and returns them.
     * @return a List of not empty Collections of ValidationMessages. If the List is empty, there are no
     * ValidationMessages. If it is not empty, there are some.
     */
    private List<Collection<ValidationMessage>> collectCurrentMessages() {
        final List<Collection<ValidationMessage>> list=new ArrayList<Collection<ValidationMessage>>();
        synchronized(resultMap) {
            for(Collection<ValidationResult> vrCollection : resultMap.values())
                for(ValidationResult result : vrCollection)
                    if(result!=null && !result.isEmpty())
                        list.add(result.getMessages());
        }
        return list;
    }

    private void fireValidState(final boolean isValid) {
        for(ValidationStateListener l : validationStateListeners)
            l.validationStateChanged(isValid);
    }

    private void fireMessagesChanged(final List<Collection<ValidationMessage>> messages) {
        for(ValidationStateListener l : validationStateListeners)
            l.validationMessagesChanged(messages);
    }

    public void addValidationStateListener(final ValidationStateListener listener) {
        validationStateListeners.add(listener);
    }

    public void removeValidationStateListener(final ValidationStateListener listener)
    {
        validationStateListeners.remove(listener);
    }
}