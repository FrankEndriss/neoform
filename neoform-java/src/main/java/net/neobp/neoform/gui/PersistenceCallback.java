package net.neobp.neoform.gui;

/** Interface for parent components of Neoform forms.
 * The methods are called usually after clicking the corresponding buttons.
 */
public interface PersistenceCallback<M>
{
    /** Indicates the save button was clicked, so the current model object
     * should be saved persistent.
     * If the save succeeds, the parent of the neoform should create a new copy
     * of the model object and set it on the form using setModel(M)
     * If the save does not succeeds, it is the parents responsibility to 
     * display info to the user. 
     * Since the parent is expected to register its constraints to the model object
     * via addModelValidator(M) with the Neoform this should happen not very often.
     * On the other hand, there are constrainst not checkable before doing the
     * real save() within the database or whereever.
     * However, the Neoform does not changes its state before or after calling
     * this method, it just goes on as nothing had happened.
     * 
     * @param m for convenience the last model set per setModel(M, boolean)
     */
    public void save(M model);

    /** Indicates the cancel button was clicked.
     * The parent should create a new copy of the model object and set it on the
     * Neoform via setModel(M)
     * The Neoform does not changes its state before or after calling this
     * method, it just goes on as nothing had happened.
     * 
     * @param m for convenience the last model set per setModel(M, boolean)
     */
    public void cancel(M model);
}
