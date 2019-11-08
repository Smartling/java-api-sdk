package com.smartling.api.v2.issues.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@AllArgsConstructor
@Data
@EqualsAndHashCode
public class IssueCommentPTO implements ResponseData
{
    private String issueCommentUid;
    private String commentText;
    private Date createdDate;
    private Date commentTextModifiedDate;
    private String createdByUserUid;
    private String createdByWatcherUid;
}
