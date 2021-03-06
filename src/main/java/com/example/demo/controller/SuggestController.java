package com.example.demo.controller;

import com.example.demo.dto.request.SuggestRequest;
import com.example.demo.service.ISuggestService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/suggests")
public class SuggestController {

    private final ISuggestService _suggestService;

    public SuggestController(ISuggestService suggestService) {
        _suggestService = suggestService;
    }

    @PutMapping
    public void suggest(@RequestBody SuggestRequest request) throws Exception {
         _suggestService.suggest(request);
    }

    @PutMapping("/operation")
    public void suggestOp(@RequestBody SuggestRequest request) throws Exception {
        _suggestService.suggestOp(request);
    }
}
