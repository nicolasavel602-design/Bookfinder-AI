package com.bookfinder.backend;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service 
public class RecommendationService {

    private final RestClient restClient = RestClient.create();

    // Uses an AI model to convert the query into keywords that
    // Open library can use to search for books more effectively.
    public String generateSearchQuery(String preference){

        // Load the API key from an environmental variable to
        // avoid exposing the key in the source code.
        String apiKey = System.getenv("GROQ_API_KEY");

        if (apiKey== null || apiKey.isBlank()){
            throw new IllegalStateException("GROQ_API_KEY is not configured");

        }

        // Build the request sent to Groq.
        // The system message tells the AI what to do, while the
        // user message contains the reader's preferences.
       Map<String, Object> requestBody = Map.of(
            "model", "openai/gpt-oss-20b",
            "messages", List.of(
                Map.of(
                    "role", "system",
                    "content",
                    "Convert a reader's preferences into 3 to 6 concise English book-search keywords. " +
                    "Return only the keywords, separated by spaces. " +
                    "Do not include explanations or invented book titles."
                ),
                Map.of(
                    "role", "user",
                    "content", preference
                )
            ),
            "max_completion_tokens", 512
        );

        
        // Sends the request to Groq and deseralizes the JSON response
        // into a Java Map so its individual fields can be accessed.
        Map<?, ?> response = restClient.post()
            .uri("https://api.groq.com/openai/v1/chat/completions")
            .header("Authorization", "Bearer " + apiKey)
            .body(requestBody)
            .retrieve()
            .body(Map.class);

        if (response == null) {
            throw new IllegalStateException("Empty AI response");
        }

        // Groq returns generated outputs inside a "choices" array.
        // This checks that the array exists before accessing its first element
        Object choicesData = response.get("choices");

        if (!(choicesData instanceof List<?> choices) || choices.isEmpty()) {
            throw new IllegalStateException("No AI response choices");
        }

        // Extract Groq's generated text from the first choice.
        Map<?, ?> firstChoice = (Map<?, ?>) choices.get(0);
        Map<?, ?> message = (Map<?, ?>) firstChoice.get("message");

        String searchQuery = String.valueOf(message.get("content")).trim();

        // Prevent empty AI responses from being passed to Open library.
        if (searchQuery.isBlank() || searchQuery.equals("null")) {
            throw new IllegalStateException("AI returned no search terms");
        }

        return searchQuery;
    }
}

    

    

