package org.libin.externalms.restController;

import lombok.AllArgsConstructor;
import org.libin.dao.dto.cat.DtoCat;
import org.libin.dao.dto.cat.DtoListCat;
import org.libin.dao.dto.cat.DtoSaveCat;
import org.libin.dao.models.Colour;
import org.libin.externalms.authentication.IAuthenticationFacade;
import org.libin.externalms.security.UserDetailsConfig;
import org.libin.externalms.service.CatService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.libin.dao.errors.EntityNotFoundException;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@ComponentScan(basePackages = "org.libin.externalms.service")
@ComponentScan(basePackages = "org.libin.externalms.authentication")
@RequestMapping("/api/cats")
public class CatController {
    private final IAuthenticationFacade authenticationFacade;
    private final CatService catService;



    private Authentication getCurrentAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private List<DtoCat> filterCats(DtoListCat dtoListCat){
        dtoListCat.setCats(checkUserAuthorities(dtoListCat.getCats()));
        return dtoListCat.getCats();

    }
    private List<DtoCat> checkUserAuthorities(List<DtoCat> cats){
        String userRole = getUserRole();
        Authentication authentication = getCurrentAuthentication();
        UserDetailsConfig user = (UserDetailsConfig) authentication.getPrincipal();
        Long OwnerId = user.getOwnerId();
        if(userRole.equals("ADMIN")){
            return cats;
        }
        return cats.stream().filter(cat -> cat.getOwnerId().equals(OwnerId)).toList();
    }

    private DtoCat checkUserAuthority(DtoCat cat){
        String userRole = getUserRole();
        Authentication authentication = getCurrentAuthentication();
        UserDetailsConfig user = (UserDetailsConfig) authentication.getPrincipal();
        Long OwnerId = user.getOwnerId();
        if(userRole.equals("ADMIN") || cat.getOwnerId().equals(OwnerId)){
            return cat;
        }
        throw new AccessDeniedException("You are not allowed to view this cat");

    }

    private String getUserRole(){
        Authentication authentication = authenticationFacade.getAuthentication();
        return authentication.getAuthorities().stream().toList().get(0).getAuthority();
    }


    @GetMapping
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<List<DtoCat>> findAll(){
        DtoListCat catListResponse = catService.getAllCats();
        return  new ResponseEntity<>(filterCats(catListResponse), HttpStatus.OK);
    }

    @GetMapping("/get/{cat_id}")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<?> findById(@PathVariable("cat_id") Long id){
        try {
            ResponseEntity<?> resp = catService.getCatById(id);
            DtoCat cat = (DtoCat) resp.getBody();
            DtoCat authorizedCat = checkUserAuthority(cat);
            return new ResponseEntity<>(authorizedCat,HttpStatus.OK);

        }catch (EntityNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/get_color/{color}")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<List<DtoCat>> findByColour(@PathVariable("color") String colour){
        DtoListCat catListResponse = catService.getCatByColour(colour);
        return  new ResponseEntity<>(filterCats(catListResponse), HttpStatus.OK);
    }

    @GetMapping("/get_name/{name}")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<List<DtoCat>> findByName(@PathVariable("name") String name){
        DtoListCat catListResponse = catService.getCatByName(name);
        return  new ResponseEntity<>(filterCats(catListResponse), HttpStatus.OK);
    }

    @GetMapping("/get_breed/{breed}")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<List<DtoCat>> findByBreed(@PathVariable("breed") String breed){
        DtoListCat catListResponse = catService.getCatByBreed(breed);
        return  new ResponseEntity<>(filterCats(catListResponse), HttpStatus.OK);
    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> registerCat(@RequestBody DtoSaveCat catSaveModel){
        catService.saveCat(catSaveModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteCat(@PathVariable("id") Long id){
        try {
            ResponseEntity<?> resp = catService.killCat(id);
            DtoCat cat = (DtoCat) resp.getBody();
            DtoCat authorizedCat = checkUserAuthority(cat);
            return new ResponseEntity<>(authorizedCat,HttpStatus.OK);

        }catch (EntityNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
    //Todo add friend, unfriend cat (done)

    @PostMapping("/{catId}/addFriend/{otherId}")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<?> friendCat(
            @PathVariable("catId") Long catId,
            @PathVariable("otherId") Long otherId
    ){
        try {
            ResponseEntity<?> resp = catService.friendCat(catId,otherId);
            DtoCat cat = (DtoCat) resp.getBody();
            DtoCat authorizedCat = checkUserAuthority(cat);
            return new ResponseEntity<>(authorizedCat,HttpStatus.OK);

        }catch (EntityNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @DeleteMapping("/{catId}/unfriend/{otherId}")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public ResponseEntity<?> unfriendCat(
            @PathVariable("catId") Long catId,
            @PathVariable("otherId") Long otherId
    ){
        try {
            ResponseEntity<?> resp = catService.unfriendCat(catId,otherId);
            DtoCat cat = (DtoCat) resp.getBody();
            DtoCat authorizedCat = checkUserAuthority(cat);
            return new ResponseEntity<>(authorizedCat,HttpStatus.OK);

        }catch (EntityNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

}
