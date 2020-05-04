package com.example.demo.controller;

import com.example.demo.dto.request.CertfieRecipeRequest;
import com.example.demo.dto.request.CreateRecipeRequest;
import com.example.demo.dto.request.SearchCertifiedRecipesRequest;
import com.example.demo.dto.response.RecipeResponse;
import com.example.demo.service.IRecipeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping
    public List<RecipeResponse> getAllRecipes() throws Exception {
        return _recipeService.getAllRecipes();
    }

    @PutMapping
    public RecipeResponse certifyRecipe(@RequestBody CertfieRecipeRequest request) throws Exception {
        return _recipeService.certifyRecipe(request);
    }

    @GetMapping("/certified/{id}/clinic")
    public List<RecipeResponse> getAllCertifiedRecipes(@PathVariable UUID id) throws Exception {
        return _recipeService.getAllCertifiedRecipes(id);
    }

    @GetMapping("/non-certified/{id}/clinic")
    public List<RecipeResponse> getAllNonCertifiedRecipes(@PathVariable UUID id) throws Exception {
        return _recipeService.getAllNonCertifiedRecipes(id);
    }

    @GetMapping("/waiting/{id}/clinic")
    public List<RecipeResponse> getAllWaitingRecipes(@PathVariable UUID id) throws Exception {
        return _recipeService.getAllWaitingRecipes(id);
    }

    @GetMapping("/certified/search/{id}/clinic")
    public List<RecipeResponse> getAllCertifiedRecipesByMedicineNameAndDiagnosisName(SearchCertifiedRecipesRequest request, @PathVariable UUID id) throws Exception {
        return _recipeService.getAllCertifiedRecipes(request, id);
    }
}
