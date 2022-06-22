package com.vbsoft.helper.azure.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.vbsoft.helper.azure.exceptions.AzureException;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public abstract class AzureProcessor {

    public <T> T convertAnswer(Response body, Class<T> target) throws AzureException {
        try {
            JsonMapper mapper = new JsonMapper();
            return mapper.readValue(body.body().string(), target);
        } catch (IOException ex) {
            throw new AzureException(ex);
        }
    }

    public String convertToJSONArrayString(List<?> object) throws AzureException {
        try {
            JsonMapper mapper = new JsonMapper();
            return mapper.writeValueAsString(object);
        } catch (IOException ex) {
            throw new AzureException(ex);
        }
    }

}
