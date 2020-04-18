package com.example.demo.service.implementation;

import com.example.demo.dto.request.CreateRecipeRequest;
import com.example.demo.dto.response.RecipeResponse;
import com.example.demo.entity.Diagnosis;
import com.example.demo.entity.Medicine;
import com.example.demo.entity.Recipe;
import com.example.demo.repository.IDiagnosisRepository;
import com.example.demo.repository.IMedicineRepository;
import com.example.demo.repository.IRecipeRepository;
import com.example.demo.service.IRecipeService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecipeService implements IRecipeService {

    private final IRecipeRepository _recipeRepository;

    private final IMedicineRepository _medicineRepository;

    private final IDiagnosisRepository _diagnosisRepository;

    public RecipeService(IRecipeRepository recipeRepository, IMedicineRepository medicineRepository, IDiagnosisRepository diagnosisRepository) {
        _recipeRepository = recipeRepository;
        _medicineRepository = medicineRepository;
        _diagnosisRepository = diagnosisRepository;
    }

    @Override
    public RecipeResponse createRecipe(CreateRecipeRequest recipeRequest) throws Exception {
        Medicine medicine = _medicineRepository.findOneById(recipeRequest.getMedicineId());
        Diagnosis diagnosis = _diagnosisRepository.findOneById(recipeRequest.getDiagnosisId());
        Recipe recipe = new Recipe();
        recipe.setCertified(false);
        recipe.setDeleted(false);
        recipe.setDiagnosis(diagnosis);
        recipe.setMedicine(medicine);
        Recipe savedRecipe = _recipeRepository.save(recipe);
        return mapRecipeToRecipeResponse(savedRecipe);
    }

    @Override
    public void deleteRecipe(UUID id) {
        Recipe recipe = _recipeRepository.findOneById(id);
        recipe.setDeleted(true);
        _recipeRepository.save(recipe);
    }

    public RecipeResponse mapRecipeToRecipeResponse(Recipe recipe){
        RecipeResponse response = new RecipeResponse();
        response.setDiagnosisName(recipe.getDiagnosis().getName());
        response.setMedicineName(recipe.getMedicine().getName());
        response.setId(recipe.getId());
        return response;
    }
}
