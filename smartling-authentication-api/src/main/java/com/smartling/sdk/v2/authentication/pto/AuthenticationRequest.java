package com.smartling.sdk.v2.authentication.pto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticationRequest
{
    private String userIdentifier;
    private String userSecret;
}
