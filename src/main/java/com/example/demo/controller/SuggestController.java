package com.example.demo.controller;

import com.example.demo.service.ISuggestService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/suggests")
public class SuggestController {

    private final ISuggestService _suggestService;

    public SuggestController(ISuggestService suggestService) {
        _suggestService = suggestService;
    }

    @GetMapping("/{id}/examination")
    public void suggest(@PathVariable UUID id) throws Exception {
         _suggestService.suggest(id);
    }
}
