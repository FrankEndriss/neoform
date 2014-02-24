package net.neobp.neoform.validation;


public interface ValueValidationProvider
{
    public void addValueValidationListener(ValueValidationListener listener);
    public void removeValueValidationListener(ValueValidationListener listener);
}
