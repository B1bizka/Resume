package org.libin.dao.models;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cats")
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@id")

public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long catId;
    @Column(name = "name")
    private String catName;
    @Column(name ="birthday")
    private LocalDate catBirthday;
    @Column(name = "breed")
    private String catBreed;
    @Enumerated(EnumType.STRING)
    @Column(name = "color", columnDefinition = "cat_color")
    private Colour catColour;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "cat_friends",
            joinColumns=  @JoinColumn(name="cat_id"),
            inverseJoinColumns =  @JoinColumn(name="friend_id")
    )
    private Set<Cat> listOfFriends = new HashSet<>();


    public Cat(String name, String breed, Colour color, LocalDate birth, Owner owner){
        catName = name;
        catBirthday = birth;
        catBreed = breed;
        catColour = color;
        this.owner = owner;
    }
    public Cat(String name, String breed, Colour color, LocalDate birth){
        catName = name;
        catBirthday = birth;
        catBreed = breed;
        catColour = color;
    }

    public void befriend(Cat cat){
        if (this.listOfFriends.add(cat)) {
            cat.listOfFriends.add(this);
        }
    }
    public void unfriend(Cat cat){
        if (this.listOfFriends.remove(cat)) {
            cat.listOfFriends.remove(this);
        }
    }

}