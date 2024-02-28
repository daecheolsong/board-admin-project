package com.example.boardadminproject.controller.advice;

import com.example.boardadminproject.service.VisitCounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * @author daecheol song
 * @since 1.0
 */
@RequiredArgsConstructor
@ControllerAdvice
public class VisitCounterControllerAdvice {

    private final VisitCounterService visitCounterService;

    @ModelAttribute("visitCount")
    public Long visitCount() {
        return visitCounterService.visitCount();
    }
}
