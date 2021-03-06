package com.example.demo.service.implementation;

import com.example.demo.dto.request.CertfieRecipeRequest;
import com.example.demo.dto.request.CreateRecipeRequest;
import com.example.demo.dto.request.SearchCertifiedRecipesRequest;
import com.example.demo.dto.response.RecipeResponse;
import com.example.demo.entity.Diagnosis;
import com.example.demo.entity.Medicine;
import com.example.demo.entity.Nurse;
import com.example.demo.entity.Recipe;
import com.example.demo.repository.IDiagnosisRepository;
import com.example.demo.repository.IMedicineRepository;
import com.example.demo.repository.INurseRepository;
import com.example.demo.repository.IRecipeRepository;
import com.example.demo.service.IRecipeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecipeService implements IRecipeService {

    private final IRecipeRepository _recipeRepository;

    private final IMedicineRepository _medicineRepository;

    private final IDiagnosisRepository _diagnosisRepository;

    private final INurseRepository _nurseRepository;

    public RecipeService(IRecipeRepository recipeRepository, IMedicineRepository medicineRepository, IDiagnosisRepository diagnosisRepository, INurseRepository nurseRepository) {
        _recipeRepository = recipeRepository;
        _medicineRepository = medicineRepository;
        _diagnosisRepository = diagnosisRepository;
        _nurseRepository = nurseRepository;
    }

    @Override
    public RecipeResponse createRecipe(CreateRecipeRequest recipeRequest) throws Exception {
        Medicine medicine = _medicineRepository.findOneById(recipeRequest.getMedicineId());
        Diagnosis diagnosis = _diagnosisRepository.findOneById(recipeRequest.getDiagnosisId());
        Recipe recipe = new Recipe();
        recipe.setCertified(false);
        recipe.setDeleted(false);
        recipe.setWaiting(false);
        recipe.setDiagnosis(diagnosis);
        recipe.setMedicine(medicine);
        recipe.setClinicId(recipeRequest.getClinicId());
        Recipe savedRecipe = _recipeRepository.save(recipe);
        return mapRecipeToRecipeResponse(savedRecipe);
    }

    @Override
    public void deleteRecipe(UUID id) {
        Recipe recipe = _recipeRepository.findOneById(id);
        recipe.setDeleted(true);
        _recipeRepository.save(recipe);
    }

    @Override
    public List<RecipeResponse> getAllRecipes() throws Exception {
        List<Recipe> recipes = _recipeRepository.findAllByDeleted(false);
        if(recipes.isEmpty()){
            throw new Exception("Ne postoji nijedan recept.");
        }
        return recipes.stream()
                .map(recipe -> mapRecipeToRecipeResponse(recipe))
                .collect(Collectors.toList());
    }

    @Override
    public RecipeResponse certifyRecipe(CertfieRecipeRequest request) throws Exception {
        Recipe recipe = _recipeRepository.findOneById(request.getRecipeId());
        if(recipe.isCertified()){
            throw new Exception("Recept je vec overen.");
        }
        recipe.setNurseId(request.getNurseId());
        recipe.setCertified(true);
        recipe.setWaiting(false);
        _recipeRepository.save(recipe);
        return mapRecipeToRecipeResponse(recipe);
    }

    @Override
    public List<RecipeResponse> getAllCertifiedRecipes(UUID clinicId) throws Exception {
        List<Recipe> recipes = _recipeRepository.findAllByDeletedAndCertifiedAndWaiting(false, true, false);
        List<Recipe> recipesByClinic = new ArrayList<>();
        for(Recipe r: recipes){
            if(r.getClinicId().equals(clinicId)){
                recipesByClinic.add(r);
            }
        }
        if(recipesByClinic.isEmpty()){
            throw new Exception("Ne postoji nijedan overen recept u Vašoj klinici.");
        }
        return recipesByClinic.stream()
                .map(recipe -> mapRecipeToRecipeResponse(recipe))
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeResponse> getAllNonCertifiedRecipes(UUID clinicId) throws Exception {
        List<Recipe> recipes = _recipeRepository.findAllByDeletedAndCertifiedAndWaiting(false, false, false);
        List<Recipe> recipesByClinic = new ArrayList<>();
        for(Recipe r: recipes){
            if(r.getClinicId().equals(clinicId)){
                recipesByClinic.add(r);
            }
        }
        if(recipesByClinic.isEmpty()){
            throw new Exception("Ne postoji nijedan neoveren recept u Vašoj klinici.");
        }
        return recipesByClinic.stream()
                .map(recipe -> mapRecipeToRecipeResponse(recipe))
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeResponse> getAllWaitingRecipes(UUID clinicId) throws Exception {
        List<Recipe> recipes = _recipeRepository.findAllByDeletedAndCertifiedAndWaiting(false, false, true);
        List<Recipe> recipesByClinic = new ArrayList<>();
        for(Recipe r: recipes){
            if(r.getClinicId().equals(clinicId)){
                recipesByClinic.add(r);
            }
        }
        if(recipesByClinic.isEmpty()){
            throw new Exception("Ne postoji nijedan recept koji treba da se overi u Vašoj klinici.");
        }
        return recipesByClinic.stream()
                .map(recipe -> mapRecipeToRecipeResponse(recipe))
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeResponse> getAllCertifiedRecipes(SearchCertifiedRecipesRequest request, UUID clinicId) throws Exception {
        List<Recipe> recipes = _recipeRepository.findAllByDeletedAndCertifiedAndWaiting(false, true, false);
        List<Recipe> recipesByClinic = new ArrayList<>();
        for(Recipe r: recipes){
            if(r.getClinicId().equals(clinicId)){
                recipesByClinic.add(r);
            }
        }

        return recipesByClinic
                .stream()
                .filter(recipe -> {
                    if(request.getMedicineName() != null) {
                        return recipe.getMedicine().getName().toLowerCase().contains(request.getMedicineName().toLowerCase());
                    } else {
                        return true;
                    }
                })
                .filter(recipe -> {
                    if(request.getDiagnosisName() != null) {
                        return recipe.getDiagnosis().getName().toLowerCase().contains(request.getDiagnosisName().toLowerCase());
                    } else {
                        return true;
                    }
                })
                .map(r -> mapRecipeToRecipeResponse(r))
                .collect(Collectors.toList());
    }

    public RecipeResponse mapRecipeToRecipeResponse(Recipe recipe){
        RecipeResponse response = new RecipeResponse();
        response.setDiagnosisName(recipe.getDiagnosis().getName());
        response.setMedicineName(recipe.getMedicine().getName());
        response.setId(recipe.getId());
        Nurse nurse = _nurseRepository.findOneById(recipe.getNurseId());
        if(nurse != null){
            response.setNurseName(nurse.getUser().getFirstName());
            response.setNurseSurname(nurse.getUser().getLastName());
        }
        return response;
    }
}
