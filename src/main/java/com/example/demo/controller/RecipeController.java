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

    @GetMapping("/certified")
    public List<RecipeResponse> getAllCertifiedRecipes() throws Exception {
        return _recipeService.getAllCertifiedRecipes();
    }

    @GetMapping("/non-certified")
    public List<RecipeResponse> getAllNonCertifiedRecipes() throws Exception {
        return _recipeService.getAllNonCertifiedRecipes();
    }

    @GetMapping("/waiting")
    public List<RecipeResponse> getAllWaitingRecipes() throws Exception {
        return _recipeService.getAllWaitingRecipes();
    }

    @GetMapping("/certified/search")
    public List<RecipeResponse> getAllCertifiedRecipesByMedicineNameAndDiagnosisName(SearchCertifiedRecipesRequest request) throws Exception {
        return _recipeService.getAllCertifiedRecipes(request);
    }
}
