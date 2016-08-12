package net.neobp.neoform.validation;



public interface ValidationStateProvider
{
    public void addValidationStateListener(ValidationStateListener listener);
    public void removeValidationStateListener(ValidationStateListener listener);
}
