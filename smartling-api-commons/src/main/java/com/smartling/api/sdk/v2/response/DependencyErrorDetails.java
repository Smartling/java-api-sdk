package com.smartling.api.sdk.v2.response;

import java.util.List;

public class DependencyErrorDetails extends Details
{
    private DependencyErrorDetailsReport dependencies;

    public DependencyErrorDetails()
    {
        super();
        dependencies = new DependencyErrorDetailsReport();
    }

    public DependencyErrorDetails(List<DependencyErrorField> dependencyFields)
    {
        super();
        dependencies = new DependencyErrorDetailsReport(dependencyFields);
    }

    public DependencyErrorDetailsReport getDependencies()
    {
        return dependencies;
    }

    public void setDependencies(DependencyErrorDetailsReport dependencies)
    {
        this.dependencies = dependencies;
    }
}
