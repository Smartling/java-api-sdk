package com.smartling.api.sdk.v2.response.authentication.pto;

import com.smartling.api.sdk.v2.response.ResponseData;
import lombok.Data;

@Data
public class Authentication implements ResponseData
{
    private String accessToken;
    private String refreshToken;
    private int expiresIn;
    private int refreshExpiresIn;
    private String tokenType;
}
