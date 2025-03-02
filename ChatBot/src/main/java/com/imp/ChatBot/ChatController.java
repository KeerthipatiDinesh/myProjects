package com.imp.ChatBot;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@CrossOrigin
public class ChatController {

	@GetMapping
	public String index() {
		return "index1";
	}
	
	@Value("${gemini.api.key}") // Read API key from application.properties
    private String apiKey;
	
	@Value("${gemini.api.base-url}")
	private String baseurl;

    private final RestTemplate restTemplate;

    public ChatController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateContent(@RequestBody Map<String, String> requestPayload) {
        String inputText = requestPayload.get("text");

        // URL for the API
        String url = baseurl + apiKey;

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // JSON payload
        String requestBody = """
        {
            "contents": [
                {
                    "role": "user",
                    "parts": [
                        {
                            "text": "%s"
                        }
                    ]
                }
            ],
            "generationConfig": {
                "temperature": 1,
                "topK": 40,
                "topP": 0.95,
                "maxOutputTokens": 8192,
                "responseMimeType": "text/plain"
            }
        }
        """.formatted(inputText);

        // Create HTTP Entity
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Call the API
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        // Extract the text field from the response
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                // Parse the response JSON
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(response.getBody());
                String text = root.path("candidates")
                                  .get(0)
                                  .path("content")
                                  .path("parts")
                                  .get(0)
                                  .path("text")
                                  .asText();
                return ResponseEntity.ok(text);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error processing response: " + e.getMessage());
            }
        }

        return ResponseEntity.status(response.getStatusCode()).body("Failed to generate content");
    }

}

