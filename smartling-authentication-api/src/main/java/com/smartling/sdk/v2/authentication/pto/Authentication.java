package com.smartling.sdk.v2.authentication.pto;

import com.smartling.sdk.v2.ResponseData;
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
