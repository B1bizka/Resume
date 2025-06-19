package org.libin.controller;

import lombok.AllArgsConstructor;
import org.libin.service.EntityNotFoundException;
import org.libin.service.OwnerService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.libin.dao.dto.*;


import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/owners")
@ComponentScan(basePackages = "org.libin.service")
public class OwnerController {

    private final OwnerService ownerService;


    @GetMapping
    public ResponseEntity<List<DtoOwner>> getAllOwners(){
        return ResponseEntity.ok(ownerService.getAllOwners());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getOwner(@PathVariable("id") Long id){
        try{
            return ResponseEntity.ok(ownerService.getOwner(id));
        }catch (EntityNotFoundException ex){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }

    }


    @GetMapping("/get_name/{name}")
    public ResponseEntity<List<DtoOwner>> findByName(@RequestParam("name") String name){
        return ResponseEntity.ok(ownerService.findByName(name));
    }


    @PostMapping("/new")
    public ResponseEntity<?> createOwner(@RequestBody DtoOwner dto){
        DtoOwner created = ownerService.createOwner(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOwner(@PathVariable("id") Long id){
        try{
            ownerService.deleteOwner(id);
        }catch (EntityNotFoundException ex){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{ownerId}/addCat/{catId}")
    public ResponseEntity<?> addCat(
            @PathVariable("ownerId") Long ownerId,
            @PathVariable("catId") Long catId
    ) {
        try{
            ownerService.addCat(ownerId, catId);
        }catch (EntityNotFoundException ex){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{ownerId}/deleteCat/{catId}")
    public ResponseEntity<?> removeCat(
            @PathVariable("ownerId") Long ownerId,
            @PathVariable("catId") Long catId
    ) {
        try{
            ownerService.deleteCat(ownerId, catId);
        }catch (EntityNotFoundException ex){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
        return ResponseEntity.noContent().build();
    }
}
