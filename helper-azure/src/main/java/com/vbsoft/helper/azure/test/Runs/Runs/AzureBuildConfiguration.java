package com.vbsoft.helper.azure.test.Runs.Runs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AzureBuildConfiguration {

    Long id;
    String number;
    String flavor;
    String platform;
    String buildDefinitionId;

}
