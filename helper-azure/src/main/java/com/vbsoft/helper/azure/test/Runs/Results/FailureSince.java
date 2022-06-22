package com.vbsoft.helper.azure.test.Runs.Results;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FailureSince {

    String date;
    String build;
}
