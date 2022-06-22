package com.vbsoft.helper.azure.test.Runs.Attachment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AzureTestAttachment {

    Long id;
    String createdDate;
    String url;
    String fileName;
    String comment;
    Long size;

}
