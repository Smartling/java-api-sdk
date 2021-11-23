package com.smartling.api.contexts.v2.pto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteContextsAsyncRequestPTO
{
    private List<String> contextUids;

}
