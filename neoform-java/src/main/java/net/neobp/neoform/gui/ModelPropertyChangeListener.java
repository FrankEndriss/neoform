package net.neobp.neoform.gui;

/** Interface for listeners of models properties changes.
 * TODO is this class needed? It seems there is no one to implement it :/
 */
public interface ModelPropertyChangeListener<M>
{
    /** Called whenever the ModelHolder wants to tell that the model object changed to a new model object
     * @param m the changed model object, might be null
     */
    public void modelPropertyChanged(M m);
}
