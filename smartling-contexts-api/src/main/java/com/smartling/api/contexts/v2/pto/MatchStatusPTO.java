package com.smartling.api.contexts.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchStatusPTO implements ResponseData
{
    private String matchId;
    private String createdDate;
    private String modifiedDate;
    private String status;
    private String contextUid;
    private Long checkedStringsCount;
    private List<BindingPTO> bindings;
}
