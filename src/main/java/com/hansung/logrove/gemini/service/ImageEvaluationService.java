package com.hansung.logrove.gemini.service;

import com.hansung.logrove.gemini.client.GeminiClient;
import com.hansung.logrove.gemini.dto.GeminiEvaluationResponse;
import com.hansung.logrove.gemini.parser.GeminiResponseParser;
import com.hansung.logrove.gemini.prompt.EvaluationPromptFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageEvaluationService {

    private final GeminiClient geminiClient;
    private final EvaluationPromptFactory evaluationPromptFactory;
    private final GeminiResponseParser geminiResponseParser;

    public GeminiEvaluationResponse evaluate(MultipartFile file, String theme, String content, String guide, int passScore) {
        String prompt = evaluationPromptFactory.create(theme, content, guide);
        String rawResponse = geminiClient.requestEvaluation(file, prompt);
        return geminiResponseParser.parseEvaluationResponse(rawResponse, passScore);
    }
}