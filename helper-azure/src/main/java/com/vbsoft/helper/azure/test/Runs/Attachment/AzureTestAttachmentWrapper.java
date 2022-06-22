package com.vbsoft.helper.azure.test.Runs.Attachment;

import com.vbsoft.helper.azure.base.AzureWrapperModel;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AzureTestAttachmentWrapper extends AzureWrapperModel {

    List<AzureTestAttachment> value;

}
