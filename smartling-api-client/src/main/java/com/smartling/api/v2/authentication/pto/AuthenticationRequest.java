package com.smartling.api.v2.authentication.pto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticationRequest
{
    private String userIdentifier;
    private String userSecret;
}
