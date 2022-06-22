package com.vbsoft.helper.starter.helperstarter.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AzureErrorsServiceTest {

    @Autowired
    AzureErrorsService service;

    @Test
    public void createErrors() {
        service.createErrors();
    }
}