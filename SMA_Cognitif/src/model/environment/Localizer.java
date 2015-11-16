package model.environment;

/**
 * Created by benoitvuillemin on 16/11/2015.
 */
public interface Localizer
{
    public <T> T setCurrentCase(Case c);
    public Case getCurrentCase();
}
