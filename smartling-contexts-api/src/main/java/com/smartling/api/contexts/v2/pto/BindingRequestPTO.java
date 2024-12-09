package com.smartling.api.contexts.v2.pto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BindingRequestPTO
{
    protected String contextUid;
    protected String stringHashcode;
    protected List<String> anchors;
    protected TimecodePTO timecode;
    protected CoordinatesPTO coordinates;
    protected Integer page;

    public BindingRequestPTO(String contextUid, String stringHashcode, CoordinatesPTO coordinates)
    {
        this.contextUid = contextUid;
        this.stringHashcode = stringHashcode;
        this.coordinates = coordinates;
    }

    public BindingRequestPTO(String contextUid, String stringHashcode, CoordinatesPTO coordinates, Integer page)
    {
        this.contextUid = contextUid;
        this.stringHashcode = stringHashcode;
        this.coordinates = coordinates;
        this.page = page;
    }

    public BindingRequestPTO(String contextUid, String stringHashcode, TimecodePTO timecode, CoordinatesPTO coordinates)
    {
        this.contextUid = contextUid;
        this.stringHashcode = stringHashcode;
        this.timecode = timecode;
        this.coordinates = coordinates;
    }

    public BindingRequestPTO(String contextUid, String stringHashcode, List<String> anchors)
    {
        this.contextUid = contextUid;
        this.stringHashcode = stringHashcode;
        this.anchors = anchors;
    }
}
