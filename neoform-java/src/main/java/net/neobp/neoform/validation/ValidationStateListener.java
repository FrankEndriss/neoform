package net.neobp.neoform.validation;

import java.util.Collection;
import java.util.List;

/** Interface for Listeners of ValidationState. ValidationStates includes the information if
 * at least one of the validations performed as "not isValid()".
 */
public interface ValidationStateListener
{
    /** This is called every time the validation state changes in a Neoform.
     * @param valid flag if the current state is valid or invalid.
     */
    public void validationStateChanged(boolean isValid);
    
    /** This is called when the list of ValidationMessages change (and therefor should be redisplayed)
     * @param messages the list of Messages, sorted by some more or less usefull schema
     */
    public void validationMessagesChanged(List<Collection<ValidationMessage>> messages);
    
}
