package at.srfg.robogen.fitnesswatch.fitbit_API.common.loaders;

/**
 * Created by jboggess on 10/17/16.
 */

public class ResourceLoaderResult<T> {
    private final T m_tResult;
    private final boolean m_bSuccessful;
    private final Exception m_cException;
    private final String m_sErrorMessage;
    private final ResultType m_cResultType;

    public enum ResultType {
        SUCCESS, ERROR, EXCEPTION, LOGGED_OUT
    }

    private ResourceLoaderResult(T result, ResultType resultType, String errorMessage, Exception exception) {
        this.m_tResult = result;
        this.m_bSuccessful = resultType == ResultType.SUCCESS;
        this.m_sErrorMessage = errorMessage;
        this.m_cException = exception;
        this.m_cResultType = resultType;
    }

    public static <G> ResourceLoaderResult<G> onSuccess(G result) {
        return new ResourceLoaderResult<G>(result, ResultType.SUCCESS, null, null);
    }

    public static <G> ResourceLoaderResult<G> onError(String errorMessage) {
        return new ResourceLoaderResult<G>(null, ResultType.ERROR, errorMessage, null);
    }

    public static <G> ResourceLoaderResult<G> onException(Exception exception) {
        String message = exception.getMessage();
        if (message == null) {
            message = exception.getCause().getMessage();
        }
        return new ResourceLoaderResult<G>(null, ResultType.EXCEPTION, message, exception);
    }

    public static <G> ResourceLoaderResult<G> onLoggedOut() {
        return new ResourceLoaderResult<G>(null, ResultType.LOGGED_OUT, null, null);
    }

    public T getResult() {
        return m_tResult;
    }

    public boolean isSuccessful() {
        return m_bSuccessful;
    }

    public String getErrorMessage() {
        return m_sErrorMessage;
    }

    public Exception getException() {
        return m_cException;
    }

    public ResultType getResultType() {
        return m_cResultType;
    }
}
