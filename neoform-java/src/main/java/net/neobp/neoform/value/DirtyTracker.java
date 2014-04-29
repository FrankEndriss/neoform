package net.neobp.neoform.value;

import java.util.HashSet;
import java.util.Set;

/** This class collects the dirty states of a group of DirtyProviders. (usually some widgets)
 * The states are combined (by "or" operation), and a dirtyProvider (usually a form) is informed 
 * about the cumulated state changes.
 * This is, if all widgets are not dirty, then the tracker is not dirty.
 * If at least one widget is dirty, the tracker is dirty.
 * I.e. one can control the enabled state of a save button with this cumulated state.
 * 
 * The cumulated dirty state of the tracker is sent to the registered DirtyListeners
 * whenever it changes.
 */
public class DirtyTracker implements DirtyListener, DirtyProvider {
    private final Set<DirtyProvider> dirtyWidgets = new HashSet<DirtyProvider>();
    private final Set<DirtyListener> dirtyListeners=new HashSet<DirtyListener>();
    
    /** Lock to synchronize the HashSet dirtyWidgets. */
    private final Object lock=new Object();
    
    /** Clears the dirty state to notDirty, and sents a event to all listeners denoting this probably new state. */
    public void clear() {
        synchronized(lock) {
            dirtyWidgets.clear();
        }
        fireDirty(false);
    }

    /** Called by the cumulated widgets
     * @see net.neobp.neoform.value.DirtyListener#isDirty(net.neobp.neoform.value.DirtyProvider, boolean)
     */
    public void isDirty(final DirtyProvider src, final boolean isDirty)
    {
        if(isDirty) { 
            boolean firstEntry=false;
            synchronized(lock) {
                dirtyWidgets.add(src);
                    if(dirtyWidgets.size()==1)
                        firstEntry=true;
            }
            if(firstEntry)
                fireDirty(true);
        } else {
            boolean lastEntry=false;
            synchronized(lock) {
                dirtyWidgets.remove(src);
                if(dirtyWidgets.size()==0)
                    lastEntry=true;
            }
            if(lastEntry)
                fireDirty(false);
        }
    }

    public void addDirtyListener(DirtyListener listener)
    {
        dirtyListeners.add(listener);
    }

    public void removeDirtyListener(DirtyListener listener)
    {
        dirtyListeners.remove(listener);
    }
    
    protected void fireDirty(final boolean isDirty) {
        for(DirtyListener dl : dirtyListeners)
            dl.isDirty(this, isDirty);
    }
}