package net.neobp.neoform.value;

/** Interface for a two directional type/value converter.
 * The naming indicates that there is a model value of type M, and a screen value of type S. 
 * If used in this terms the code should be more or less readable.
 */
public interface ValueConverter<M, S>
{
    public S model2Screen(M value);
    public M screen2Model(S value);
}
