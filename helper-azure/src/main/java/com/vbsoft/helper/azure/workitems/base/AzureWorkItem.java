package com.vbsoft.helper.azure.workitems.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AzureWorkItem {

    @JsonProperty("System.AreaPath")
    String areaPath;

    @JsonProperty("System.IterationPath")
    String iterationPath;

    @JsonProperty("System.State")
    String state;

    @JsonProperty("System.Title")
    String title;

    @JsonProperty("System.Tags")
    String tag;



}
