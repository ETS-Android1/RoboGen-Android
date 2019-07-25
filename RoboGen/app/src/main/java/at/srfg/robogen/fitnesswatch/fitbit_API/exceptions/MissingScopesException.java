package at.srfg.robogen.fitnesswatch.fitbit_API.exceptions;

import at.srfg.robogen.fitnesswatch.fitbit_Auth.Scope;
import java.util.Collection;

/*******************************************************************************
 * Class MissingScopesException
 ******************************************************************************/
public class MissingScopesException extends FitbitAPIException {

    private Collection<Scope> scopes;

    public MissingScopesException(Collection<Scope> scopes) {
        this.scopes = scopes;
    }

    public Collection<Scope> getScopes() {
        return scopes;
    }
}
