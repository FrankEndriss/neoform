package net.neobp.neoform.gui;

import java.util.Collection;
import java.util.List;

import net.neobp.neoform.validation.ValidationMessage;
import net.neobp.neoform.validation.ValidationStateListener;
import net.neobp.neoform.validation.ValidationStateProvider;
import net.neobp.neoform.validation.ValidationTracker;
import net.neobp.neoform.value.DirtyListener;
import net.neobp.neoform.value.DirtyProvider;
import net.neobp.neoform.value.DirtyTracker;

public abstract class AbstractNeoformController implements DirtyProvider, ValidationStateProvider
{
    protected DirtyTracker dirtyTracker = new DirtyTracker();
    protected ValidationTracker validationTracker = new ValidationTracker();
    
    public AbstractNeoformController() {
        dirtyTracker.addDirtyListener(new DirtyListener() {
            public void isDirty(DirtyProvider src, boolean isDirty)
            {
                onDirtyChanged(isDirty);
            }

        });

        validationTracker.addValidationStateListener(new ValidationStateListener() {
            public void validationStateChanged(boolean isValid)
            {
                onValidationStateChanged(isValid);
            }
            public void validationMessagesChanged(List<Collection<ValidationMessage>> messages)
            {
                onValidationMessagesChanged(messages);
            }
        });
    }

    protected abstract void onValidationMessagesChanged(List<Collection<ValidationMessage>> messages);

    protected abstract void onValidationStateChanged(boolean isValid);

    protected abstract void onDirtyChanged(boolean isDirty);

    public void addDirtyListener(DirtyListener listener)
    {
        dirtyTracker.addDirtyListener(listener);
    }

    public void removeDirtyListener(DirtyListener listener)
    {
        dirtyTracker.removeDirtyListener(listener);
    }

    public void addValidationStateListener(ValidationStateListener listener)
    {
        validationTracker.addValidationStateListener(listener);
    }

    public void removeValidationStateListener(ValidationStateListener listener)
    {
        validationTracker.removeValidationStateListener(listener);
    }
}
