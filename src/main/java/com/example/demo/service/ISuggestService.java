package com.example.demo.service;

import com.example.demo.dto.response.ExaminationResponse;

import java.util.UUID;

public interface ISuggestService {

    ExaminationResponse suggest(UUID id) throws Exception;
}
