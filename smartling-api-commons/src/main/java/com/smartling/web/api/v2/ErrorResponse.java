package com.smartling.web.api.v2;

import java.util.Arrays;
import java.util.List;

/**
 * Represents an error response.
 */
public class ErrorResponse extends Response<EmptyData>
{
    private List<Error>  errors;

    /**
     * Creates a new, empty error response.
     */
    protected ErrorResponse()
    {
        super();
    }

    /**
     * Creates an error response with the given response code and errors.
     *
     * @param code the <code>ResponseCode</code> for this response
     * @param errors a <code>List</code> of <code>Error</code>s for this response
     */
    public ErrorResponse(ResponseCode code, List<Error> errors)
    {
        super(code);
        this.errors = errors;
    }

    /**
     * Creates an error response with the given response code and errors.
     *
     * @param code the <code>ResponseCode</code> for this response
     * @param errors one or more <code>Error</code>(s) for this response
     */
    public ErrorResponse(ResponseCode code, Error... errors)
    {
        this(code, Arrays.asList(errors));
    }

    public List<Error> getErrors()
    {
        return errors;
    }

    public void setErrors(List<Error> errors)
    {
        this.errors = errors;
    }
}
