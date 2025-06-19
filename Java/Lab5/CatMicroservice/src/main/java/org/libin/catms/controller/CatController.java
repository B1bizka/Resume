package org.libin.catms.controller;

import lombok.AllArgsConstructor;
import org.libin.catms.service.CatService;
import org.libin.dao.errors.EntityNotFoundException;
import org.libin.dao.dto.cat.DtoListCat;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/cats")
public class CatController {

    private final CatService catService;


    @GetMapping
    public ResponseEntity<DtoListCat> getAllCats(){
        return ResponseEntity.ok(catService.getAllCats());
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<?> getCat(@PathVariable("id") Long id){
        try{
            return ResponseEntity.ok(catService.getCat(id));
        }catch (EntityNotFoundException ex){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }


    @GetMapping("/get_name/{name}")
    public ResponseEntity<DtoListCat> findByName(@PathVariable("name") String name){
        return ResponseEntity.ok(catService.findByName(name));
    }

    @GetMapping("/get_breed/{breed}")
    public ResponseEntity<DtoListCat> findByBreed(@PathVariable("breed") String breed){
        return ResponseEntity.ok(catService.getCatsByBreed(breed));
    }

    @GetMapping("/get_color/{colour}")
    public ResponseEntity<DtoListCat> findByColour(@PathVariable("colour") String colour){
        return ResponseEntity.ok(catService.getCatsByColor(colour));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCat(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(catService.deleteCat(id));
        }catch (EntityNotFoundException ex) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/{catId}/addFriend/{otherId}")
    public ResponseEntity<?> befriend(
            @PathVariable("catId") Long catId,
            @PathVariable("otherId") Long otherId
    ) {
        try {
            return ResponseEntity.ok(catService.befriend(catId, otherId));
        }catch (EntityNotFoundException ex) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @DeleteMapping("/{catId}/unfriend/{otherId}")
    public ResponseEntity<?> unfriend(
            @PathVariable("catId") Long catId,
            @PathVariable("otherId") Long otherId
    ) {
        try {
            return ResponseEntity.ok(catService.unfriend(catId, otherId));
        }catch (EntityNotFoundException ex) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

}
