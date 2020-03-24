package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ExaminationType extends BaseEntity {

    private String name;

    private String price;

    @OneToMany(mappedBy = "examinationType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Doctor> doctors = new ArrayList<>();

    private boolean deleted;
}
