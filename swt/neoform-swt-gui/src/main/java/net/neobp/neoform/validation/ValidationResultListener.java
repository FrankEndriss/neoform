package net.neobp.neoform.validation;

import java.util.Collection;

import net.neobp.neo.nms.core.validation.ValidationResult;


/** Interface for listeners of ValidationResults
 */
public interface ValidationResultListener
{
    /** Called after everey Validation performed by the provider src.
     * @param src the provider/executor of the validation
     * @param resultList list of results of the validation. One Result per Validator.
     */
    public void validationPerformed(ValidationResultProvider src, Collection<ValidationResult> resultList);
}
