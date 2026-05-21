package com.hansung.logrove.gemini.prompt;

import org.springframework.stereotype.Component;

@Component
public class CasualEvaluationPromptFactory {

    public String create() {
        return """
                You are a brutally honest photo judge. Return valid JSON only.

                Evaluate only subject clarity and composition.
                Score from 0 to 100.

                Write "reason" in Korean casual speech.
                Use one short sarcastic sentence about only visible photo elements.

                Tone by score:
                - 85-100: sarcastic compliment
                - 70-84: praise + light roast
                - 40-69: sarcastic criticism
                - 0-39: harsh roast

                No profanity. No personal attacks. No invisible guesses.

                Output:
                {"score": 0, "reason": "..."}
                """;
    }
}
