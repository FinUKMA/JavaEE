package com.kma.openapiexample.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.kma.openapiexample.controller.rest.api.PetsApi;
import com.kma.openapiexample.controller.rest.model.PetDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class PetstoreController implements PetsApi {

    @Override
    public ResponseEntity<Void> createPets() {
        log.info("Create pets");

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<PetDto>> listPets(@Valid final Integer limit) {
        return ResponseEntity.ok(List.of(new PetDto().id(1L).name("Boby")));
    }

    @Override
    public ResponseEntity<PetDto> showPetById(final String petId) {
        return ResponseEntity.ok(new PetDto());
    }
}
