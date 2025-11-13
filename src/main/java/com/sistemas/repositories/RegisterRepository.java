package com.sistemas.repositories;
import com.sistemas.entities.RegisterEntity;
import com.sistemas.entities.RoleEntity;
import com.sistemas.entities.StablishmentEntity;
import com.sistemas.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegisterRepository extends JpaRepository<RegisterEntity, Integer> {
    @Query(
            value = "SELECT * FROM registers c WHERE c.deleted_at IS NOT NULL",
            nativeQuery = true
    )
    List<RegisterEntity> findAllDeleted();

    @Query("SELECT ur FROM RegisterEntity ur WHERE ur.deletedAt IS NULL")
    List<RegisterEntity> findAllActive();

    @Query(value = "SELECT * FROM registers c WHERE c.id = :id", nativeQuery = true)
    Optional<RegisterEntity> findAnyById(@Param("id") Integer id);

    @Query(value = "SELECT * FROM registers", nativeQuery = true)
    List<RegisterEntity> findAllIncludingDeleted();

    @Query("SELECT c FROM RegisterEntity c")
    Page<RegisterEntity> findAllPaginated(Pageable pageable);

    @Query("SELECT r FROM RegisterEntity r " +
            "WHERE (:term IS NULL OR LOWER(r.description_property) LIKE LOWER(CONCAT('%', :term, '%')))")
    Page<RegisterEntity> findAllPaginatedFiltered(@Param("term") String term, Pageable pageable);


    @Query("SELECT r.stablishment.name AS name, COUNT(r) AS total FROM RegisterEntity r GROUP BY r.stablishment.name")
    List<Object[]> countRegistersByStablishment();

    @Query(value = "SELECT * FROM registers c WHERE c.deleted_at IS NOT NULL AND " +
            "(:term IS NULL OR LOWER(c.description_property) LIKE LOWER(CONCAT('%', :term, '%')))",
            nativeQuery = true)
    Page<RegisterEntity> findAllDeletedPaginatedFiltered(@Param("term") String term, Pageable pageable);


}
