package net.neobp.neoform.gui;

import java.util.Collection;
import java.util.List;

import net.neobp.neo.nms.core.validation.ValidationMessage;
import net.neobp.neoform.validation.ValidationResultListener;
import net.neobp.neoform.validation.ValidationResultProvider;
import net.neobp.neoform.validation.ValidationStateListener;
import net.neobp.neoform.validation.ValidationStateProvider;
import net.neobp.neoform.validation.ValidationTracker;
import net.neobp.neoform.value.DirtyListener;
import net.neobp.neoform.value.DirtyProvider;
import net.neobp.neoform.value.DirtyTracker;

public class AbstractNeoformController implements DirtyProvider, ValidationStateProvider
{
    protected DirtyTracker dirtyTracker = new DirtyTracker();
    protected ValidationTracker validationTracker = new ValidationTracker();
    
    public AbstractNeoformController() {
        dirtyTracker.addDirtyListener(new DirtyListener() {
            @Override
            public void isDirty(DirtyProvider src, boolean isDirty)
            {
                onDirtyChanged(isDirty);
            }

        });

        validationTracker.addValidationStateListener(new ValidationStateListener() {
            @Override
            public void validationStateChanged(boolean isValid)
            {
                onValidationStateChanged(isValid);
            }
            @Override
            public void validationMessagesChanged(List<Collection<ValidationMessage>> messages)
            {
                onValidationMessagesChanged(messages);
            }
        });
    }

    protected void onValidationMessagesChanged(List<Collection<ValidationMessage>> messages) {
        //  empty
    }

    protected void onValidationStateChanged(boolean isValid) {
        //  empty
    }

    protected void onDirtyChanged(boolean isDirty) {
        //  empty
    }

    @Override
    public void addDirtyListener(DirtyListener listener)
    {
        dirtyTracker.addDirtyListener(listener);
    }

    @Override
    public void removeDirtyListener(DirtyListener listener)
    {
        dirtyTracker.removeDirtyListener(listener);
    }

    @Override
    public void addValidationStateListener(ValidationStateListener listener)
    {
        validationTracker.addValidationStateListener(listener);
    }

    @Override
    public void removeValidationStateListener(ValidationStateListener listener)
    {
        validationTracker.removeValidationStateListener(listener);
    }
}
