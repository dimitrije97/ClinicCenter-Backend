package com.example.demo.service;

import com.example.demo.dto.request.CertfieRecipeRequest;
import com.example.demo.dto.request.CreateRecipeRequest;
import com.example.demo.dto.request.SearchCertifiedRecipesRequest;
import com.example.demo.dto.response.RecipeResponse;

import java.util.List;
import java.util.UUID;

public interface IRecipeService {

    RecipeResponse createRecipe(CreateRecipeRequest recipeRequest) throws Exception;

    void deleteRecipe(UUID id);

    List<RecipeResponse> getAllRecipes() throws Exception;

    RecipeResponse certifyRecipe(CertfieRecipeRequest request) throws Exception;

    List<RecipeResponse> getAllCertifiedRecipes(UUID clinicId) throws Exception;

    List<RecipeResponse> getAllNonCertifiedRecipes(UUID clinicId) throws Exception;

    List<RecipeResponse> getAllWaitingRecipes(UUID clinicId) throws Exception;

    List<RecipeResponse> getAllCertifiedRecipes(SearchCertifiedRecipesRequest request, UUID clinicId) throws Exception;
}
