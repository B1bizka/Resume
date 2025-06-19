package org.libin.service;

import org.libin.dao.repository.CatRepository;
import lombok.AllArgsConstructor;
import org.libin.dao.dto.DtoOwner;
import org.libin.dao.models.Owner;
import org.libin.dao.models.Cat;
import org.libin.dao.repository.OwnerRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@ComponentScan(basePackages = "org.libin.dao")
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final CatRepository catRepository;


    public DtoOwner getOwner(Long id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found with id=" + id));
        return toDto(owner);
    }

    public List<DtoOwner> getAllOwners() {
        return ownerRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<DtoOwner> findByName(String name) {
        return ownerRepository.findAllByName(name).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void addCat(Long ownerId, Long catId){
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found: " + ownerId));
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException("Cat not found: " + catId));

        owner.tameTheBeast(cat);
        ownerRepository.save(owner);
    }

    public void deleteCat(Long ownerId, Long catId){
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(()-> new EntityNotFoundException("Owner not found: " + ownerId));
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException("Cat not found: " + catId));
        owner.getRidOfTheBeast(cat);
        ownerRepository.save(owner);
    }

    public DtoOwner createOwner(DtoOwner dto) {
        Owner owner = toEntity(dto);
        Owner saved = ownerRepository.save(owner);
        return toDto(saved);
    }

    public void deleteOwner(Long id) {
        if (!ownerRepository.existsById(id)) {
            throw new EntityNotFoundException("Owner not found with id=" + id);
        }
        ownerRepository.deleteById(id);
    }


    private DtoOwner toDto(Owner owner) {
        DtoOwner dto = new DtoOwner();
        dto.setId(owner.getOwnerId());
        dto.setName(owner.getName());
        dto.setBirthday(owner.getOwnerBirthday());

        Set<Long> catIds = owner.getListOfCats().stream()
                .map(Cat::getCatId)
                .collect(Collectors.toSet());
        dto.setListOfCats(catIds);
        return dto;
    }

    private Owner toEntity(DtoOwner dto) {
        Owner owner = new Owner();
        owner.setName(dto.getName());
        owner.setOwnerBirthday(dto.getBirthday());
        return owner;
    }
}
