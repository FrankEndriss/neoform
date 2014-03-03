package net.neobp.dev.codeGenerator;

import net.neobp.dev.codeGenerator.neoform.model.NeoformModel;

public interface NeoformModelLoader
{
    /** Finds (and probably loads) a NeoformModel by name
     * @param neoformName the name of the neoform as given in the forms definition.
     * @return the model of the form
     */
    public NeoformModel getNeoformModel(String neoformName);

}
