package com.example.demo.controller;

import com.example.demo.dto.response.ExaminationResponse;
import com.example.demo.service.ISuggestService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.UUID;

@RestController
@RequestMapping("/suggests")
public class SuggestController {

    private final ISuggestService _suggestService;

    public SuggestController(ISuggestService suggestService) {
        _suggestService = suggestService;
    }

    @GetMapping("/{id}/examination")
    public ExaminationResponse suggest(@PathVariable UUID id) throws Exception {
        return _suggestService.suggest(id);
    }
}
