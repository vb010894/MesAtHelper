package com.vbsoft.helper.azure.test.Runs;

import com.vbsoft.helper.azure.AzureConnection;
import com.vbsoft.helper.azure.annotations.AzureHeader;
import com.vbsoft.helper.azure.base.AzureProcessor;
import com.vbsoft.helper.azure.exceptions.AzureException;
import com.vbsoft.helper.azure.test.Runs.Attachment.AzureTestAttachmentWrapper;
import com.vbsoft.helper.azure.test.Runs.Results.AzureTestResult;
import com.vbsoft.helper.azure.test.Runs.Results.AzureTestResultWrapper;
import com.vbsoft.helper.azure.test.Runs.Runs.AzureTestRuns;
import com.vbsoft.helper.azure.test.Runs.Runs.AzureTestRunsWrapper;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Component
public class AzureTestRunsProcessor extends AzureProcessor {

    private AzureConnection connection;

    private static final String RUNS_LIST = "_apis/test/runs";
    private static final String API_VERSION = "6.0";

    @Autowired
    public AzureTestRunsProcessor(AzureConnection connection) {
        this.connection = connection;
    }

    public List<AzureTestRuns> getList() throws AzureException {
        String URL = String.format(
                "%s/%s/%s/%s?api-version=%s",
                this.connection.getUrl(),
                this.connection.getCollection(),
                this.connection.getProject(),
                RUNS_LIST,
                API_VERSION);

        Request request = this.connection.getRequest().url(URL).get().build();
        Response resp = this.connection.getResponse(request);

        AzureTestRunsWrapper wrapper = this.convertAnswer(resp, AzureTestRunsWrapper.class);
        return wrapper.getValue();
    }

    public List<AzureTestRuns> getByQuery(Query query) throws AzureException {
        String URL = String.format(
                "%s/%s/%s/%s?api-version=%s&%s",
                this.connection.getUrl(),
                this.connection.getCollection(),
                this.connection.getProject(),
                RUNS_LIST,
                API_VERSION,
                query.convertToHeader());

        Request request = this.connection.getRequest().url(URL).get().build();
        Response resp = this.connection.getResponse(request);

        AzureTestRunsWrapper wrapper = this.convertAnswer(resp, AzureTestRunsWrapper.class);
        List<AzureTestRuns> runs = wrapper.getValue();
        runs = this.appendResults(runs);
        return runs;
    }

    private List<AzureTestRuns>  appendResults(List<AzureTestRuns> runs) {
        runs.forEach(run -> {
            String URL = String.format(
                    "%s/%s/%s/_apis/test/Runs/%s/results?api-version=%s",
                    this.connection.getUrl(),
                    this.connection.getCollection(),
                    this.connection.getProject(),
                    run.getId(),
                    API_VERSION);

            Request request = this.connection.getRequest().url(URL).get().build();
            Response resp = this.connection.getResponse(request);

            try {
                AzureTestResultWrapper wrapper = this.convertAnswer(resp, AzureTestResultWrapper.class);
                List<AzureTestResult> results = wrapper.getValue();
                results.parallelStream().forEach(result -> {
                    result.setParent(run);
                });
                this.appendAttachments(results);
                run.setResults(results);
            } catch (AzureException e) {
                log.error(e.getMessage());
            }
        });

        return runs;
    }

    private void appendAttachments(List<AzureTestResult> results) {
        results.forEach(result -> {
            String URL = String.format(
                    "%s/%s/%s/_apis/test/Runs/%s/Results/%s/attachments?api-version=%s",
                    this.connection.getUrl(),
                    this.connection.getCollection(),
                    this.connection.getProject(),
                    result.getParent().getId(),
                    result.getId(),
                    API_VERSION + "-preview.1");

            Request request = this.connection.getRequest().url(URL).get().build();
            Response resp = this.connection.getResponse(request);

            try {
                AzureTestAttachmentWrapper wrapper = this.convertAnswer(resp, AzureTestAttachmentWrapper.class);
                result.setAttachments(wrapper.getValue());
            } catch (AzureException e) {
                log.error(e.getMessage());
            }
        });

    }

    @Builder
    public static class Query {
        Date maxLastUpdatedDate;
        Date minLastUpdatedDate;
        @AzureHeader("$top")
        Long top;
        String branchName;
        String buildDefIds;
        String buildIds;
        String continuationToken;
        Boolean isAutomated;
        String planIds;
        String publishContext;
        String releaseDefIds;
        String releaseEnvIds;
        String releaseIds;
        String runTitle;
        TestRunState state;

        public String convertToHeader() {
           return Arrays.stream(this.getClass()
                    .getDeclaredFields())
                    .parallel()
                    .map(this::convertField)
                    .filter(el -> !el.equals(""))
                    .collect(Collectors.joining("&"));
        }

        private String convertField(Field field) {
            field.setAccessible(true);
            String name = (field.isAnnotationPresent(AzureHeader.class)) ? field.getAnnotation(AzureHeader.class).value() : field.getName();
            String result;
            try {
                Object value = field.get(this);
                if(value instanceof Date) {
                    value = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format((Date) value);
                }

                if(value == null)
                    result = "";
                else
                    result = name + "=" + value;
            } catch (IllegalAccessException e) {
               result = "";

            }
            field.setAccessible(false);
            return result;
        }

    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    public enum TestRunState {
        ABORTED("aborted"),
        COMPLETED("completed"),
        IN_PROGRESS("inProgress"),
        NEEDS_INVESTIGATION("needsInvestigation"),
        NOT_STARTED("notStarted"),
        UNSPECIFIED("unspecified"),
        WAITING("waiting");
        @Getter
        final String value;

        TestRunState(String state) {
            this.value = state;
        }
    }


}
