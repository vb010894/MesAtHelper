package com.vbsoft.helper.azure.workitems.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vbsoft.helper.azure.AzureConnection;
import com.vbsoft.helper.azure.base.AzureProcessor;
import com.vbsoft.helper.azure.exceptions.AzureException;
import com.vbsoft.helper.azure.workitems.base.Error.AzureError;
import lombok.extern.log4j.Log4j2;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
@Log4j2
public class AzureWorkItemProcessor extends AzureProcessor {

    private static final String ERROR_CREATION_URL = "_apis/wit/workitems?type=Ошибка&";
    private static final String API_VERSION = "6.0";

    private final AzureConnection connection;

    @Autowired
    public AzureWorkItemProcessor(AzureConnection connection) {
        this.connection = connection;
    }

    public void createError(AzureError error) throws AzureException {
        List<AzureWorkItemCreator> creators = new LinkedList<>();
        List<Field> fields = new LinkedList<>();
        fields.addAll(Arrays.asList(error.getClass().getDeclaredFields()));
        fields.addAll(Arrays.asList(error.getClass().getSuperclass().getDeclaredFields()));
        fields.stream().parallel().forEach(field -> {

            if(!field.isAnnotationPresent(JsonIgnore.class)) {
                try {
                    field.setAccessible(true);
                    AzureWorkItemCreator creator = new AzureWorkItemCreator();
                    creator.setOp("add");
                    creator.setFrom(null);
                    String path = field.isAnnotationPresent(JsonProperty.class) ? field.getAnnotation(JsonProperty.class).value() : field.getName();
                    String value = (String) field.get(error);
                    creator.setPath("/fields/" + path);
                    creator.setValue(value);
                    creators.add(creator);
                } catch (IllegalAccessException e) {
                    log.error(e);
                } finally {
                    field.setAccessible(false);
                }
            }

        });

        String URL = String.format(
                "%s/%s/%s/%sapi-version=%s",
                this.connection.getUrl(),
                this.connection.getCollection(),
                this.connection.getProject(),
                ERROR_CREATION_URL,
                API_VERSION);

        String creator = this.convertToJSONArrayString(creators);
        RequestBody body = RequestBody.create(MediaType.parse("application/json-patch+json") , creator);
        Request request = this.connection.getRequest().url(URL).post(body).build();
        Response resp = this.connection.getResponse(request);
        try {
            System.out.println(resp.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
