package com.vbsoft.helper.azure.workitems.base;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AzureWorkItemCreator {

    String op;
    String path;
    String from;
    String value;

}
