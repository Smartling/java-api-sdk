package com.smartling.api.issues.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
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
