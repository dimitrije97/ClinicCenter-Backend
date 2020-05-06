package com.example.demo.service;


import com.example.demo.dto.request.SuggestRequest;


public interface ISuggestService {

    void suggest(SuggestRequest request) throws Exception;

    void suggestOp(SuggestRequest request) throws Exception;
}
