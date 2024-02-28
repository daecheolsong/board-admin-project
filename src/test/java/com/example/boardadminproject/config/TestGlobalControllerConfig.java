package com.example.boardadminproject.config;

import com.example.boardadminproject.service.VisitCounterService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

/**
 * @author daecheol song
 * @since 1.0
 */
@TestConfiguration
public class TestGlobalControllerConfig {

    @MockBean
    private VisitCounterService visitCounterService;

    @BeforeTestMethod
    public void visitSetUp() {
        given(visitCounterService.visitCount()).willReturn(0L);
    }

}
