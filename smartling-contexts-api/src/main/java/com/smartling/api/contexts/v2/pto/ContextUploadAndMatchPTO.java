package com.smartling.api.contexts.v2.pto;

import java.util.List;
import javax.ws.rs.FormParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ContextUploadAndMatchPTO extends ContextUploadPTO
{
    private static final Integer DO_NOT_OVERRIDE_CONTEXT = null;

    @FormParam("matchParams")
    @PartType("application/json")
    private MatchRequestPTO matchRequest;

    public ContextUploadAndMatchPTO(String name, byte[] content, List<String> stringHashcodes)
    {
        super(name, null, content);
        this.matchRequest = new MatchRequestPTO();
        this.matchRequest.setStringHashcodes(stringHashcodes);
        this.matchRequest.setOverrideContextOlderThanDays(DO_NOT_OVERRIDE_CONTEXT);
    }
}
