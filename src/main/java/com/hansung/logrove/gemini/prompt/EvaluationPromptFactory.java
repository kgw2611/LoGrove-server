package com.hansung.logrove.gemini.prompt;

import org.springframework.stereotype.Component;

@Component
public class EvaluationPromptFactory {

    public String create(String theme, String content, String guide) {
        return """
                You are a strict photography judge.
                
                Your task is to evaluate how well the given image satisfies the following photography concept:
                
                Concept: "{주제 키워드}"
                
                Evaluation rules:
                
                - Focus ONLY on how well the image matches the given concept
                - Ignore unrelated qualities unless they affect the concept
                - Be strict and do not give high scores unless clearly justified
                
                Scoring:
                
                - Give a score from 0 to 100 based on how well the concept is applied
                
                Decision rule:
                
                - If score >= {threshold} → PASS
                - Else → FAIL
                
                Also:
                
                - Provide a short explanation (1~2 sentences)
                - Response as fast as you can (in 5 seconds)
                - Response in Korean
                
                Output JSON only:
                
                {
                "concept": "{주제 키워드}",
                "score": 0,
                "reason": "...",
                "result": "PASS or FAIL"
                }
                """.formatted(theme, content, guide);
    }
}