package com.vbsoft.helper.azure.test.Runs.Results;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vbsoft.helper.azure.test.Runs.Attachment.AzureTestAttachment;
import com.vbsoft.helper.azure.test.Runs.Runs.AzureTestRuns;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AzureTestResult {

    Long id;
    String outcome;
    String state;
    String computerName;
    String testCaseTitle;
    String errorMessage;
    String stackTrace;

    @JsonIgnore
    AzureTestRuns parent;

    @JsonIgnore
    List<AzureTestAttachment> attachments;

}
