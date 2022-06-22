package com.vbsoft.helper.azure.test.Runs.Runs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vbsoft.helper.azure.test.Runs.Results.AzureTestResult;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AzureTestRuns {

    Long id;
    String name;
    String url;
    @JsonProperty(value = "isAutomated")
    Boolean isAutomated;
    String state;
    Long totalTests;
    Long incompleteTests;
    Long notApplicableTests;
    Long passedTests;
    Long unanalyzedTests;
    Long revision;
    String webAccessUrl;
    AzureBuildConfiguration buildConfiguration;

    @JsonIgnore
    List<AzureTestResult> results;

}
