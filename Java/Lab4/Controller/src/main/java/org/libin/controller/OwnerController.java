package org.libin.controller;

import lombok.AllArgsConstructor;
import org.libin.dto.DtoOwner;
import org.libin.service.OwnerService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.libin.service.EntityNotFoundException;


import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/owners")
@ComponentScan(basePackages = "org.libin.service")
public class OwnerController {

    private final OwnerService ownerService;


    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<DtoOwner>> getAllOwners(){
        return ResponseEntity.ok(ownerService.getAllOwners());
    }


    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<?> getOwner(@PathVariable("id") Long id){
        try{
            return ResponseEntity.ok(ownerService.getOwner(id));
        }catch (EntityNotFoundException ex){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }catch (AccessDeniedException ex){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", ex.getMessage()));
        }
    }


    @GetMapping("/getName/{name}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<DtoOwner>> findByName(@RequestParam("name") String name){
        return ResponseEntity.ok(ownerService.findByName(name));
    }


    @PostMapping("/new")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<DtoOwner> createOwner(@RequestBody DtoOwner dto){
        DtoOwner created = ownerService.createOwner(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteOwner(@PathVariable("id") Long id){
        ownerService.deleteOwner(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{ownerId}/addCat/{catId}")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<Void> addCat(
            @PathVariable("ownerId") Long ownerId,
            @PathVariable("catId") Long catId
    ) {
        ownerService.addCat(ownerId, catId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{ownerId}/deleteCat/{catId}")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<Void> removeCat(
            @PathVariable("ownerId") Long ownerId,
            @PathVariable("catId") Long catId
    ) {
        ownerService.deleteCat(ownerId, catId);
        return ResponseEntity.noContent().build();
    }
}
