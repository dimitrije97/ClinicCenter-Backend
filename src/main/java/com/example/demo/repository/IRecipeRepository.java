package com.example.demo.repository;

import com.example.demo.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IRecipeRepository extends JpaRepository<Recipe, UUID> {

    Recipe findOneById(UUID id);

    List<Recipe> findAllByDeleted(boolean deleted);
}
