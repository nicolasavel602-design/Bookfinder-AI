package com.bookfinder.backend;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController 
@CrossOrigin(origins = "http://localhost:5173")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final HelloController bookController;

    // Spring injects the existing service and controller objects,
    // which allows for their methods to be called.
    public RecommendationController(
            RecommendationService recommendationService,
            HelloController bookController) {

                this.recommendationService = recommendationService;
                this.bookController = bookController;

    }
    // Handles recommendation requests from react frontend
    // AI generates search terms, which are then used to
    // retrive matching books from the Open Library
    @GetMapping ("/api/recommendations")
    public List<Book> recommendBooks(@RequestParam String query) {

        String searchTerms = recommendationService.generateSearchQuery(query);


        return bookController.searchBooks(searchTerms);
    }
    
}



