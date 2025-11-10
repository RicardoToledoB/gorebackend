package com.sistemas.repositories;
import com.sistemas.entities.StablishmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StablishmentRepository extends JpaRepository<StablishmentEntity, Integer> {
}
