package com.example.demo.service;

import com.example.demo.dto.request.CreateRecipeRequest;
import com.example.demo.dto.response.RecipeResponse;

import java.util.UUID;

public interface IRecipeService {

    RecipeResponse createRecipe(CreateRecipeRequest recipeRequest) throws Exception;

    void deleteRecipe(UUID id);
}
