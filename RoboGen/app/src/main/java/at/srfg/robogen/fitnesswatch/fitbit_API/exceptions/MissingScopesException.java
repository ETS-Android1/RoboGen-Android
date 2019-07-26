package at.srfg.robogen.fitnesswatch.fitbit_API.exceptions;

import at.srfg.robogen.fitnesswatch.fitbit_Auth.Scope;
import java.util.Collection;

/*******************************************************************************
 * Class MissingScopesException
 ******************************************************************************/
public class MissingScopesException extends FitbitAPIException {

    private Collection<Scope> m_collectionScopes;

    public MissingScopesException(Collection<Scope> scopes) {
        this.m_collectionScopes = scopes;
    }

    public Collection<Scope> getScopes() {
        return m_collectionScopes;
    }
}
