package com.vbsoft.helper.starter.helperstarter.services;

import com.vbsoft.helper.azure.exceptions.AzureException;
import com.vbsoft.helper.azure.test.Runs.AzureTestRunsProcessor;
import com.vbsoft.helper.azure.test.Runs.Runs.AzureTestRuns;
import com.vbsoft.helper.azure.workitems.base.AzureWorkItemProcessor;
import com.vbsoft.helper.azure.workitems.base.Error.AzureError;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class AzureErrorsService {

    private AzureTestRunsProcessor runs;
    private AzureWorkItemProcessor wp;

    @Autowired
    public AzureErrorsService(AzureTestRunsProcessor runs, AzureWorkItemProcessor processor) {
        this.runs = runs;
        this.wp = processor;
    }

    public void createErrors() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            calendar.set(Calendar.HOUR_OF_DAY, 14);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);


            AzureTestRunsProcessor.Query query = AzureTestRunsProcessor.Query
                    .builder()
                    .minLastUpdatedDate(calendar.getTime())
                    .maxLastUpdatedDate(new Date())
                    .build();

            List<AzureTestRuns> found = this.runs.getByQuery(query);
            found.parallelStream().forEach(r -> {
                r.getResults().parallelStream().filter(result -> result.getOutcome().equalsIgnoreCase("failed")).forEach(res -> {
                    AzureError error = new AzureError();
                    if(r.getBuildConfiguration().getNumber().contains("UP"))
                        error.setTag("UPSTREAM");
                    else
                        error.setTag("DOWNSTREAM");

                    error.setState("Новый");
                    error.setAreaPath("QMET");
                    error.setIterationPath("QMET");

                    String replay = "<b>Сообщение</b><br>" + res.getErrorMessage()
                            + "<br> <b>Attachments<b><br>" + res.getAttachments().stream().map(at -> "<a href = " + at.getUrl() + ">" + at.getFileName() + "</a>").collect(Collectors.joining("<br>"));
                    error.setReplay(replay);
                    error.setTitle(res.getComputerName() + " " + res.getTestCaseTitle());
                    try {
                        wp.createError(error);
                    } catch (AzureException e) {
                        log.error(e);
                    }
                });
            });
        } catch (AzureException e) {
            log.error(e);
        }

    }
}
