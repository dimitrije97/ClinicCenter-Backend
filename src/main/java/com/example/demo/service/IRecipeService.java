package com.example.demo.service;

import com.example.demo.dto.request.CertfieRecipeRequest;
import com.example.demo.dto.request.CreateRecipeRequest;
import com.example.demo.dto.response.RecipeResponse;

import java.util.List;
import java.util.UUID;

public interface IRecipeService {

    RecipeResponse createRecipe(CreateRecipeRequest recipeRequest) throws Exception;

    void deleteRecipe(UUID id);

    List<RecipeResponse> getAllRecipes() throws Exception;

    RecipeResponse certifieRecipe(CertfieRecipeRequest request) throws Exception;
}
