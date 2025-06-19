package org.libin.dao.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "owners")
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class)

public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long ownerId;
    @Column(name = "name")
    private String name;
    @Column(name ="birthday")
    private LocalDate ownerBirthday;

    @OneToMany(mappedBy = "owner", cascade = { CascadeType.PERSIST, CascadeType.MERGE },
            orphanRemoval = false)
    private Set<Cat> listOfCats = new HashSet<>();

    public Owner(String name, LocalDate dateOfBirth ){
        this.name = name;
        this.ownerBirthday = dateOfBirth;


    }
    public Owner(){}

    public void tameTheBeast(Cat cat){
        if (listOfCats.add(cat)) {
            cat.setOwner(this);
        }
    }
    public void getRidOfTheBeast(Cat cat){
        if (listOfCats.remove(cat)) {
            cat.setOwner(null);
        }
    }

}

