package net.neobp.neoform.value;


/** Interface for most likely editable components which track a dirty state.
 */
public interface DirtyProvider
{
    public void addDirtyListener(DirtyListener listener);
    public void removeDirtyListener(DirtyListener listener);
}
