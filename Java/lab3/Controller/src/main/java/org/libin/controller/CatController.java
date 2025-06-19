package org.libin.controller;


import lombok.AllArgsConstructor;
import org.libin.service.CatService;
import org.libin.service.EntityNotFoundException;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.libin.dao.dto.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/cats")
@ComponentScan(basePackages = "org.libin.service")
public class CatController {

    private final CatService catService;


    @GetMapping
    public ResponseEntity<List<DtoCat>> getAllCats(){
        return ResponseEntity.ok(catService.getAllCats());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getCat(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(catService.getCat(id));
        }catch (EntityNotFoundException ex){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }

    }


    @GetMapping("/get_name/{name}")
    public ResponseEntity<List<DtoCat>> findByName(@RequestParam("name") String name){
        return ResponseEntity.ok(catService.findByName(name));
    }

    @GetMapping("/get_breed/{breed}")
    public ResponseEntity<List<DtoCat>> findByBreed(@RequestParam("breed") String breed){
        return ResponseEntity.ok(catService.getCatsByBreed(breed));
    }

    @GetMapping("/get_color/{colour}")
    public ResponseEntity<List<DtoCat>> findByColour(@RequestParam("colour") String colour){
        return ResponseEntity.ok(catService.getCatsByColor(colour));
    }


    @PostMapping("/newCat")
    public ResponseEntity<DtoCat> createCat(@RequestBody DtoCat dto) {
        DtoCat created = catService.createCat(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCat(@PathVariable("id") Long id) {
        try {
            catService.deleteCat(id);
        }catch (EntityNotFoundException ex){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{catId}/addFriend/{otherId}")
    public ResponseEntity<?> befriend(
            @PathVariable("catId") Long catId,
            @PathVariable("otherId") Long otherId
    ) {
        try {
            catService.befriend(catId, otherId);
        }catch (EntityNotFoundException ex){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{catId}/unfriend/{otherId}")
    public ResponseEntity<?> unfriend(
            @PathVariable("catId") Long catId,
            @PathVariable("otherId") Long otherId
    ) {
        try {
            catService.unfriend(catId, otherId);
        }catch (EntityNotFoundException ex){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
        return ResponseEntity.noContent().build();
    }




}
