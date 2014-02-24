package net.neobp.neoform.value;

/** Interface for a two direction type converter.
 * The naming indicates that there is a model value named M, and a Screen value names S. If
 * used in this terms the code should be more or less readable.
 */
public interface ValueConverter<M, S>
{
    public S convertToScreen(M value);
    public M convertToModel(S value);

}
