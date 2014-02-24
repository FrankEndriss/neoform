package net.neobp.neoform.validation;

import net.neobp.neo.nms.core.validation.ValidationResult;

/** Validator for (more or less) complex Model classes.
 * @see ValueValidator, its mostly the same interface.
 */
public interface ModelValidator<M>
{
    /** Performs a validation on the model object m, and returns the result. Note that
     * you can return null in case of "all valid, no messages"
     * @param m the model object
     * @return the result of the validation, or null to indicate "no constraints violated".
     */
    public ValidationResult validateModel(M m);
}
