package org.libin.ownerms.service;


import org.libin.dao.dto.owner.DtoListOwner;
import org.libin.dao.repository.CatRepository;
import lombok.AllArgsConstructor;
import org.libin.dao.dto.owner.DtoOwner;
import org.libin.dao.models.Owner;
import org.libin.dao.models.Cat;
import org.libin.dao.errors.EntityNotFoundException;
import org.libin.dao.repository.OwnerRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.libin.dao.dto.util.DtoUtil;


import java.util.List;

@AllArgsConstructor
@Service
@ComponentScan(basePackages = "org.libin.dao.repository")
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final CatRepository catRepository;


    public DtoOwner getOwner(Long id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found with id= ",id));
        return DtoUtil.castToDtoOwner(owner);
    }

    public DtoListOwner getAllOwners() {
        List<Owner> owners = ownerRepository.findAll();
        return DtoUtil.castToDtoOwnerList(owners);
    }

    public DtoListOwner findByName(String name) {
        List<Owner> owners = ownerRepository.findAllByName(name);
        return DtoUtil.castToDtoOwnerList(owners);
    }

    public String addCat(Long ownerId, Long catId){
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found: ",ownerId));
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException("Cat not found: ",catId));

        owner.tameTheBeast(cat);
        ownerRepository.save(owner);
        return "Success";
    }

    public String deleteCat(Long ownerId, Long catId){
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(()-> new EntityNotFoundException("Owner not found: ", ownerId));
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException("Cat not found: ",catId));
        owner.getRidOfTheBeast(cat);

        ownerRepository.save(owner);
        return "Success";
    }

    public DtoOwner deleteOwner(Long id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Owner not found: ",id));
        ownerRepository.deleteById(id);
        return DtoUtil.castToDtoOwner(owner);
    }

    public Owner save(Owner owner){
        return ownerRepository.save(owner);
    }
}
