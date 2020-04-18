package com.example.demo.controller;

import com.example.demo.dto.request.CreateRecipeRequest;
import com.example.demo.dto.response.RecipeResponse;
import com.example.demo.service.IRecipeService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final IRecipeService _recipeService;

    public RecipeController(IRecipeService recipeService) {
        _recipeService = recipeService;
    }

    @PostMapping
    public RecipeResponse createRecipe(@RequestBody CreateRecipeRequest recipeRequest) throws Exception {
        return _recipeService.createRecipe(recipeRequest);
    }

    @DeleteMapping("/{id}/recipe")
    public void deleteRecipe(@PathVariable UUID id){
        _recipeService.deleteRecipe(id);
    }
}
