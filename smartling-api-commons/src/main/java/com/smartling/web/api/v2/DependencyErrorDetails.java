package com.smartling.web.api.v2;

import java.util.ArrayList;
import java.util.List;

import com.smartling.web.api.v2.Details;

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
