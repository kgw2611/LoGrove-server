package com.hansung.logrove.gemini.service;

import com.hansung.logrove.gemini.client.GeminiClient;
import com.hansung.logrove.gemini.dto.GeminiTagResponse;
import com.hansung.logrove.gemini.parser.GeminiResponseParser;
import com.hansung.logrove.gemini.prompt.TagPromptFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageTaggingService {

    private final GeminiClient geminiClient;
    private final TagPromptFactory tagPromptFactory;
    private final GeminiResponseParser geminiResponseParser;

    public GeminiTagResponse recommendTags(MultipartFile file) {
        String prompt = tagPromptFactory.create();
        String rawResponse = geminiClient.requestTagging(file, prompt);
        return geminiResponseParser.parseTagResponse(rawResponse);
    }
}