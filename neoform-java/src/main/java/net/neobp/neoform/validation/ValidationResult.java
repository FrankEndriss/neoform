package net.neobp.neoform.validation;

import java.util.Collection;

public interface ValidationResult
{

    public boolean isEmpty();
    // TODO: like nms ValidationResult
    // its a Collection<ValidationMessage>

    public Collection<ValidationMessage> getMessages();
}
