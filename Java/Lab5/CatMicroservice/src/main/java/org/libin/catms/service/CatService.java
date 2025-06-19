package org.libin.catms.service;

import lombok.AllArgsConstructor;
import org.libin.dao.errors.EntityNotFoundException;
import org.libin.dao.dto.cat.DtoCat;
import org.libin.dao.dto.cat.DtoListCat;
import org.libin.dao.dto.util.DtoUtil;
import org.libin.dao.models.Cat;
import org.libin.dao.models.Colour;
import org.libin.dao.repository.CatRepository;
import org.libin.dao.repository.OwnerRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
@ComponentScan(basePackages = "org.libin.dao.repository")
public class CatService {
    private final CatRepository catRepository;
    private final OwnerRepository ownerRepository;


    public DtoCat getCat(Long id) {
        Cat cat = catRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cat not found with id= ",id));
        return DtoUtil.castToDtoCat(cat);
    }

    public DtoListCat getAllCats() {
        List<Cat> cats = catRepository.findAll();
        return DtoUtil.castToDtoCatList(cats);
    }


    public DtoListCat getCatsByColor(String color) {
        Colour c = Colour.valueOf(color.toUpperCase());
        List<Cat> cats = catRepository.findAllByCatColour(c);
        return DtoUtil.castToDtoCatList(cats);
    }

    public DtoListCat getCatsByBreed(String breed){
        List<Cat> cats = catRepository.findAllByCatBreed(breed);
        return DtoUtil.castToDtoCatList(cats);
    }


    public DtoListCat findByName(String name) {
        List<Cat> cats = catRepository.findAllByCatName(name);
        return DtoUtil.castToDtoCatList(cats);
    }


    public Cat save(Cat cat){
        return catRepository.save(cat);
    }

    public DtoCat befriend(Long catId, Long otherId) {
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException("Cat not found: ",catId));
        Cat other = catRepository.findById(otherId)
                .orElseThrow(() -> new EntityNotFoundException("Cat not found: ",otherId));


        cat.befriend(other);
        catRepository.save(cat);
        return  DtoUtil.castToDtoCat(cat);
    }

    public DtoCat unfriend(Long catId, Long otherId) {
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException("Cat not found: ",catId));
        Cat other = catRepository.findById(otherId)
                .orElseThrow(() -> new EntityNotFoundException("Cat not found: ",otherId));


        cat.unfriend(other);
        catRepository.save(cat);
        return  DtoUtil.castToDtoCat(cat);
    }


    public DtoCat deleteCat(Long id) {
        Cat cat = catRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cat not found: ",id));

        catRepository.deleteById(id);
        return  DtoUtil.castToDtoCat(cat);
    }
}