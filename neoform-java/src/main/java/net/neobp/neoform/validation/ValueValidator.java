package net.neobp.neoform.validation;


/** Most simple Validator. It validates a single value of type V.
 * @param V the type of the validate value
 */
public interface ValueValidator<V>
{
    /** Performs a validation on the value value
     * @param value value to validate
     * @return a validation result, or null to indicate "no constraint violated".
     */
    public ValidationResult validateValue(V value);
}
