package org.libin.externalms.restController;

import lombok.AllArgsConstructor;
import org.libin.dao.dto.owner.DtoOwner;
import org.libin.dao.dto.owner.DtoSaveOwner;
import org.libin.dao.errors.EntityNotFoundException;
import org.libin.externalms.service.OwnerService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@ComponentScan(basePackages = "org.libin.externalms.service")
@RequestMapping("/api/owners")
public class OwnerController {

    private final OwnerService ownerService;

    //public OwnerController(OwnerService ownerService){
       // this.ownerService = ownerService;
    //}

    @GetMapping
    public ResponseEntity<List<DtoOwner>> findAll(){
        return new ResponseEntity<>(ownerService.getAllOwners().getOwners(), HttpStatus.OK);
    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> newOwner(@RequestBody DtoSaveOwner ownerSaveModel){
        ownerService.saveOwner(ownerSaveModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> findById(@PathVariable("id") Long id){
        try {
            ResponseEntity<?> resp = ownerService.getOwnerById(id);
            DtoOwner dtoOwner = (DtoOwner) resp.getBody();
            return new ResponseEntity<>(dtoOwner,HttpStatus.OK);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/get_name/{name}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<DtoOwner>> findByName(@PathVariable("name") String name){
        return new ResponseEntity<>(ownerService.getOwnerByName(name).getOwners(), HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteOwner(@PathVariable("id") Long id){
        try {
            ResponseEntity<?> resp = ownerService.killOwner(id);
            DtoOwner dtoOwner = (DtoOwner) resp.getBody();
            return new ResponseEntity<>(dtoOwner,HttpStatus.OK);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }

    }

    @PostMapping("/{ownerId}/addCat/{catId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> addCat(
            @PathVariable("ownerId") Long ownerId,
            @PathVariable("catId") Long catId
    ) {
        try {
            ResponseEntity<?> resp = ownerService.addCat(ownerId, catId);
            DtoOwner dtoOwner = (DtoOwner) resp.getBody();
            return new ResponseEntity<>(dtoOwner,HttpStatus.OK);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @DeleteMapping("/{ownerId}/deleteCat/{catId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> removeCat(
            @PathVariable("ownerId") Long ownerId,
            @PathVariable("catId") Long catId
    ) {
        try {
            ResponseEntity<?> resp = ownerService.deleteCat(ownerId, catId);
            DtoOwner dtoOwner = (DtoOwner) resp.getBody();
            return new ResponseEntity<>(dtoOwner,HttpStatus.OK);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
