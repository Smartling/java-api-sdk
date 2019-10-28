package com.smartling.web.api.v2;

public enum ResponseCode
{
    SUCCESS(200),
    GENERAL_ERROR(500),
    VALIDATION_ERROR(400),
    AUTHENTICATION_ERROR(401),
    AUTHORIZATION_ERROR(403),
    ACCESS_DENIED_ERROR(401),
    NOT_FOUND_ERROR(404),
    MAINTENANCE_MODE_ERROR(503),
    INSUFFICIENT_FUNDS_ERROR(500),
    FILE_ALREADY_EXISTS_ERROR(500), // FIXME: shouldn't this be a 409 conflict?
    SERVICE_BUSY(500),  // FIXME: some better code
    RESOURCE_LOCKED(423), // Locked
    MAX_OPERATIONS_LIMIT_EXCEEDED(429), // Too Many Requests
    ACCEPTED(202),
    CREATED(201), // REST POST success
    NO_CONTENT(204), // REST PUT / DELETE success
    GONE(410), // Gone, Expired
    CONFLICT(409); // Conflict

    private int code;

    private ResponseCode(final int code)
    {
        this.code = code;
    }

    public int getValue()
    {
        return code;
    }
}
