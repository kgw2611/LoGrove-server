package com.hansung.logrove.gemini.prompt;

import org.springframework.stereotype.Component;

@Component
public class EvaluationPromptFactory {

    public String create(String theme, String content, String guide, int passScore) {
        return """
                You are a strict photography judge.
                
                Your task is to evaluate how well the given image satisfies the following photography concept:
                
                Concept: "%s"
                Description: "%s"
                Guide: "%s"
                
                Evaluation rules:
                
                - Focus ONLY on how well the image matches the given concept
                - Ignore unrelated qualities unless they affect the concept
                - Be strict and do not give high scores unless clearly justified
                
                Scoring:
                
                - Give a score from 0 to 100 based on how well the concept is applied
                
                Decision rule:
                
                - If score >= %d → PASS
                - Else → FAIL
                
                Also:
                
                - Provide a short explanation (1~2 sentences) in the "reason" field
                - Response as fast as you can (in 5 seconds)
                - Response in Korean
                
                Output JSON only:
                
                {
                "concept": "%s",
                "score": 0,
                "reason": "...",
                "result": "PASS or FAIL"
                }
                """.formatted(theme, content, guide, passScore, theme);
    }
}