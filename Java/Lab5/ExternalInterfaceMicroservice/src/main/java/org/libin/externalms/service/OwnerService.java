package org.libin.externalms.service;

import lombok.AllArgsConstructor;
import org.libin.dao.dto.owner.DtoListOwner;
import org.libin.dao.dto.owner.DtoSaveOwner;
import org.libin.externalms.producer.OwnerProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@ComponentScan(basePackages = "org.libin.dao.repository")
@Service
public class OwnerService {
    @Value("${http.owner.get.all}")
    private String urlGetAllOwners;

    @Value("${http.owner.get.by.id}")
    private String urlGetOwnerById;

    @Value("${http.owner.get.by.name}")
    private String urlGetOwnerByName;


    @Value("${http.owner.delete}")
    private String urlDeleteOwner;

    @Value("${http.owner.add.cat}")
    private String urlOwnerAddCat;

    @Value("${http.owner.delete.cat}")
    private String urlOwnerDeleteCat;

    private final OwnerProducer ownerProducer;

    public OwnerService(OwnerProducer producer){
        this.ownerProducer = producer;
    }

    public DtoListOwner getAllOwners(){
        return  new RestTemplate().getForObject(urlGetAllOwners, DtoListOwner.class);
    }

    public ResponseEntity<?> getOwnerById(Long id){
        return  new RestTemplate().getForObject(urlGetOwnerById, ResponseEntity.class, id);
    }

    public  DtoListOwner getOwnerByName(String name){
        return  new RestTemplate().getForObject(urlGetOwnerByName, DtoListOwner.class, name);
    }

    public void  saveOwner(DtoSaveOwner saveOwner){
        ownerProducer.sendOwner(saveOwner);
    }
    public ResponseEntity<?> killOwner(Long id){
        return new RestTemplate().getForObject(urlDeleteOwner,ResponseEntity.class,id);
    }
    public ResponseEntity<?> deleteCat(Long ownerId, Long catId){
        return new RestTemplate().getForObject(urlOwnerDeleteCat,ResponseEntity.class,ownerId,catId);


    }
    public ResponseEntity<?> addCat(Long ownerId, Long catId){
        return new RestTemplate().getForObject(urlOwnerAddCat,ResponseEntity.class,ownerId,catId);
    }

}
