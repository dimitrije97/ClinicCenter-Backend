package com.example.demo.controller;

import com.example.demo.dto.response.ExaminationResponse;
import com.example.demo.service.ISuggestService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/suggests")
public class SuggestController {

    private final ISuggestService _suggestService;

    public SuggestController(ISuggestService suggestService) {
        _suggestService = suggestService;
    }

    @PutMapping("/{id}/examination")
    public ExaminationResponse suggest(@PathVariable UUID id) throws Exception {
        return _suggestService.suggest(id);
    }
}
