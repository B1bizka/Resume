package org.libin.controller;


import lombok.AllArgsConstructor;
import org.libin.dto.DtoCat;
import org.libin.service.CatService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/cats")
@ComponentScan(basePackages = "org.libin.service")
public class CatController {

    private final CatService catService;


    @GetMapping
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<List<DtoCat>> getAllCats(){
        return ResponseEntity.ok(catService.getAllCats());
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<DtoCat> getCat(@PathVariable("id") Long id){
        return ResponseEntity.ok(catService.getCat(id));
    }


    @GetMapping("/get_name/{name}")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<List<DtoCat>> findByName(@RequestParam("name") String name){
        return ResponseEntity.ok(catService.findByName(name));
    }

    @GetMapping("/get_breed{breed}")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<List<DtoCat>> findByBreed(@RequestParam("breed") String breed){
        return ResponseEntity.ok(catService.getCatsByBreed(breed));
    }

    @GetMapping("/get_colour{colour}")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<List<DtoCat>> findByColour(@RequestParam("colour") String colour){
        return ResponseEntity.ok(catService.getCatsByColor(colour));
    }


    @PostMapping("/newCat")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<DtoCat> createCat(@RequestBody DtoCat dto) {
        DtoCat created = catService.createCat(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteCat(@PathVariable("id") Long id) {
        catService.deleteCat(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{catId}/addFriend/{otherId}")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<Void> befriend(
            @PathVariable("catId") Long catId,
            @PathVariable("otherId") Long otherId
    ) {
        catService.befriend(catId, otherId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{catId}/unfriend/{otherId}")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<Void> unfriend(
            @PathVariable("catId") Long catId,
            @PathVariable("otherId") Long otherId
    ) {
        catService.unfriend(catId, otherId);
        return ResponseEntity.noContent().build();
    }

}
