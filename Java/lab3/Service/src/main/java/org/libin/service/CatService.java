package org.libin.service;


import lombok.AllArgsConstructor;
import org.libin.dao.dto.DtoCat;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import org.libin.dao.repository.CatRepository;
import org.libin.dao.models.Owner;
import org.libin.dao.models.Colour;
import org.libin.dao.models.Cat;
import org.libin.dao.repository.OwnerRepository;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@ComponentScan(basePackages = "org.libin.dao")
public class CatService {
    private final CatRepository   catRepository;
    private final OwnerRepository ownerRepository;


    public DtoCat getCat(Long id) {
        Cat cat = catRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cat not found with id=" + id));
        return toDto(cat);
    }

    public List<DtoCat> getAllCats() {
        return catRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    public List<DtoCat> getCatsByColor(String color) {
        Colour c = Colour.valueOf(color.toUpperCase());
        return catRepository.findAllByCatColour(c).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<DtoCat> getCatsByBreed(String breed){
        return catRepository.findAllByCatBreed(breed).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    public List<DtoCat> findByName(String name) {
        return catRepository.findAllByCatName(name).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public DtoCat createCat(DtoCat dto) {
        Cat cat = toEntity(dto);
        if (dto.getOwnerId() != null) {
            Owner owner = ownerRepository.findById(dto.getOwnerId())
                    .orElseThrow(() -> new EntityNotFoundException("Owner not found with id=" + dto.getOwnerId()));
            cat.setOwner(owner);
        }
        if (dto.getFriends() != null) {
            Set<Cat> friends = dto.getFriends().stream()
                    .map(fid -> catRepository.findById(fid)
                            .orElseThrow(() -> new EntityNotFoundException("Cat not found with id=" + fid)))
                    .collect(Collectors.toSet());
            cat.setListOfFriends(friends);
        }
        Cat saved = catRepository.save(cat);
        return toDto(saved);
    }

    public void befriend(Long catId, Long otherId) {
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException("Cat not found: " + catId));
        Cat other = catRepository.findById(otherId)
                .orElseThrow(() -> new EntityNotFoundException("Cat not found: " + otherId));

        cat.befriend(other);
        catRepository.save(cat);
    }

    public void unfriend(Long catId, Long otherId) {
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException("Cat not found: " + catId));
        Cat other = catRepository.findById(otherId)
                .orElseThrow(() -> new EntityNotFoundException("Cat not found: " + otherId));

        cat.unfriend(other);
        catRepository.save(cat);
    }


    public void deleteCat(Long id) {
        if (!catRepository.existsById(id)) {
            throw new EntityNotFoundException("Cat not found with id=" + id);
        }
        catRepository.deleteById(id);
    }


    private DtoCat toDto(Cat cat) {
        DtoCat dto = new DtoCat();
        dto.setId(cat.getCatId());
        dto.setName(cat.getCatName());
        dto.setBirthday(cat.getCatBirthday());
        dto.setBreed(cat.getCatBreed());
        dto.setColor(cat.getCatColour());
        dto.setOwnerId(cat.getOwner() != null ? cat.getOwner().getOwnerId() : null);
        dto.setFriends(Optional.ofNullable(cat.getListOfFriends())
                .orElse(Collections.emptySet())
                .stream().map(Cat::getCatId)
                .collect(Collectors.toSet()));
        return dto;
    }

    private Cat toEntity(DtoCat dto) {
        Cat cat = new Cat();
        cat.setCatName(dto.getName());
        cat.setCatBirthday(dto.getBirthday());
        cat.setCatBreed(dto.getBreed());
        cat.setCatColour(dto.getColor());
        return cat;
    }
}