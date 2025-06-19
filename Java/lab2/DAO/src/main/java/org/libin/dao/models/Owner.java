package org.libin.dao.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "owners")

public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long ownerId;
    @Column(name = "name")
    private String ownerName;
    @Column(name ="birthday")
    private LocalDate ownerBirthday;

    @OneToMany(mappedBy = "owner", cascade = { CascadeType.PERSIST, CascadeType.MERGE },
            orphanRemoval = false)
    private Set<Cat> listOfCats;

    public Owner(String name, LocalDate dateOfBirth ){
        this.ownerName = name;
        this.ownerBirthday = dateOfBirth;
        this.listOfCats = new HashSet<>();

    }

    public void tameTheBeast(Cat cat){
        listOfCats.add(cat);
        cat.setOwner(this);
    }
    public void getRidOfTheBeast(Cat cat){
        listOfCats.remove(cat);
        cat.setOwner(null);
    }

}
