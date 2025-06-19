package org.libin.dao.repository;

import org.libin.dao.models.Cat;
import org.libin.dao.models.Colour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatRepository extends JpaRepository<Cat,Long> {
    List<Cat> findAllByCatName(String catName);
    List<Cat> findAllByCatBreed(String catBreed);
    List<Cat> findAllByCatColour(Colour catColour);
}
