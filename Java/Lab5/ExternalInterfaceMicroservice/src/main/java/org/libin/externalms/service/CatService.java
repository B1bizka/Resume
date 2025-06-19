package org.libin.externalms.service;

import lombok.AllArgsConstructor;
import org.libin.dao.dto.cat.DtoListCat;
import org.libin.dao.dto.cat.DtoSaveCat;
import org.libin.dao.models.Colour;
import org.libin.externalms.producer.CatProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@ComponentScan(basePackages = "org.libin.dao.repository")
@Service
public class CatService {
    @Value("${http.cat.get.all}")
    private String urlGetAllCats;

    @Value("${http.cat.get.by.id}")
    private String urlGetCatById;

    @Value("${http.cat.get.by.colour}")
    private String urlGetCatByColour;

    @Value("${http.cat.get.by.breed}")
    private String urlGetCatByBreed;

    @Value("${http.cat.get.by.name}")
    private String urlGetCatByName;

    @Value("${http.cat.delete}")
    private String urlDeleteCat;

    @Value("${http.cat.catId.friend.id}")
    private String urlFriendCat;

    @Value("${http.cat.catId.unfriend.id}")
    private String urlUnfriendCat;

    private  final CatProducer catProducer;

    public CatService(CatProducer producer){
        catProducer = producer;
    }

    public DtoListCat getAllCats(){
        return new RestTemplate().getForObject(urlGetAllCats, DtoListCat.class);
    }

    public ResponseEntity<?> getCatById(Long id){
        return  new RestTemplate().getForObject(urlGetCatById, ResponseEntity.class, id);
    }

    public DtoListCat getCatByColour(String colour){
        return  new RestTemplate().getForObject(urlGetCatByColour, DtoListCat.class, colour);
    }

    public DtoListCat getCatByBreed(String breed){
        return  new RestTemplate().getForObject(urlGetCatByBreed, DtoListCat.class, breed);
    }

    public DtoListCat getCatByName(String name){
        return  new RestTemplate().getForObject(urlGetCatByName, DtoListCat.class, name);
    }

    //ToDo save, kill, friend, unfriend (done)

    public void saveCat(DtoSaveCat saveCat){
        catProducer.sendCat(saveCat);
    }

    public ResponseEntity<?> killCat(Long id){
        return new RestTemplate().getForObject(urlDeleteCat,ResponseEntity.class,id);
    }

    public ResponseEntity<?> friendCat(Long id1,Long id2){
        return new RestTemplate().getForObject(urlFriendCat,ResponseEntity.class,id1,id2);
    }

    public ResponseEntity<?> unfriendCat(Long id1,Long id2){
        return new RestTemplate().getForObject(urlUnfriendCat,ResponseEntity.class,id1,id2);
    }
}
