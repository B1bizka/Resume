package org.libin.ownerms.controller;

import lombok.AllArgsConstructor;
import org.libin.dao.dto.owner.DtoListOwner;
import org.libin.dao.errors.EntityNotFoundException;
import org.libin.ownerms.service.OwnerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/owners")
public class OwnerController {
    private final OwnerService ownerService;

    @GetMapping
    public ResponseEntity<DtoListOwner> getAllOwners(){
        return ResponseEntity.ok(ownerService.getAllOwners());
    }


    @GetMapping("/get/{id}")
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
    public ResponseEntity<DtoListOwner> findByName(@RequestParam("name") String name){
        return ResponseEntity.ok(ownerService.findByName(name));
    }




    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOwner(@PathVariable("id") Long id){
        try{
            return ResponseEntity.ok(ownerService.deleteOwner(id));

        }catch (EntityNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
        }

    }

    @PostMapping("/{ownerId}/addCat/{catId}")
    public ResponseEntity<?> addCat(
            @PathVariable("ownerId") Long ownerId,
            @PathVariable("catId") Long catId
    ) {
        try{
            String string = ownerService.addCat(ownerId, catId);
            return ResponseEntity.ok(string);
        }catch (EntityNotFoundException ex){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @DeleteMapping("/{ownerId}/deleteCat/{catId}")
    public ResponseEntity<?> removeCat(
            @PathVariable("ownerId") Long ownerId,
            @PathVariable("catId") Long catId
    ) {
        try{
            String string = ownerService.deleteCat(ownerId, catId);
            return ResponseEntity.ok(string);
        }catch (EntityNotFoundException ex){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

}
