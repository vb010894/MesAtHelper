package com.vbsoft.helper.azure.workitems.base.Error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vbsoft.helper.azure.workitems.base.AzureWorkItem;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AzureError extends AzureWorkItem {

    @JsonIgnore
    private static final String TYPE = "Ошибка";

    @JsonProperty("Microsoft.VSTS.TCM.ReproSteps")
    String replay;

}
