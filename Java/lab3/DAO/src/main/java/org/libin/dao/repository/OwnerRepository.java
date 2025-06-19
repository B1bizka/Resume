package org.libin.dao.repository;

import org.libin.dao.models.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OwnerRepository extends JpaRepository<Owner,Long> {
    List<Owner> findAllByName(String name);
}
