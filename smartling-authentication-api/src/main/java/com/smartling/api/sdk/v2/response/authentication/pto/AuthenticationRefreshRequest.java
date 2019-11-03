package com.smartling.api.sdk.v2.response.authentication.pto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticationRefreshRequest
{
    private String refreshToken;
}
