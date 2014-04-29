package net.neobp.neoform.adapter;

import org.eclipse.ui.forms.widgets.FormToolkit;


/** Class which is used in Neoforms to represent the external Context of a Neoform-Object.
 * It is used while initialization of the Neoform, and while runtime within the Controller.
 * Any references to "external" objects like Services of any kind should be declared in
 * the Context-Class of the Neoform, which is a Subclass of this class.
 * Included in this class are external references to services/factories needed for creation of
 * Neoform gui objects.
 */
public class NeoformContext
{
    private FormToolkit formToolkit;

    /**
     * @return the FormToolkit to create widgets
     */
    public FormToolkit getFormToolkit()
    {
        return formToolkit;
    }

    public void setFormToolkit(FormToolkit formToolkit)
    {
        this.formToolkit = formToolkit;
    }

}
