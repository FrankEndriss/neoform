package net.neobp.neoform.validation;



public interface ValidationResultProvider
{
    public void addValidationResultListener(ValidationResultListener listener);
    public void removeValidationResultListener(ValidationResultListener listener);
}
