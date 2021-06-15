package com.smartling.api.contexts.v2.pto;

import com.smartling.api.v2.response.ResponseData;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BindingPTO extends BindingRequestPTO implements ResponseData
{
    private String bindingUid;
    private Integer contextPosition;

    public BindingPTO(String bindingUid, String contextUid, String stringHashcode, CoordinatesPTO coordinates)
    {
        super(contextUid, stringHashcode, coordinates);
        this.bindingUid = bindingUid;
    }

    public BindingPTO(String bindingUid, String contextUid, String stringHashcode, TimecodePTO timecode, CoordinatesPTO coordinates)
    {
        super(contextUid, stringHashcode, timecode, coordinates);
        this.bindingUid = bindingUid;
    }

    public BindingPTO(String bindingUid, String contextUid, String stringHashcode, List<String> anchors)
    {
        super(contextUid, stringHashcode, anchors);
        this.bindingUid = bindingUid;
    }
}
