package com.smartling.api.filetranslations.v2.pto.file;

public class ParseConfigItem
{
    private String instruction;
    private String value;

    public ParseConfigItem()
    {
    }

    public ParseConfigItem(String instruction, String value)
    {
        this.instruction = instruction;
        this.value = value;
    }

    public String getInstruction()
    {
        return instruction;
    }

    public void setInstruction(String instruction)
    {
        this.instruction = instruction;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return "ParseConfigItem{" +
            "instruction='" + instruction + '\'' +
            ", value='" + value + '\'' +
            '}';
    }
}
