package com.sistemas.repositories;

import com.sistemas.entities.RegisterHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegisterHistoryRepository extends JpaRepository<RegisterHistoryEntity, Integer> {

    // === LISTAR TODO INCLUYENDO SOFT-DELETED (ignora @Where)
    @Query(value = "SELECT * FROM registers_histories", nativeQuery = true)
    List<RegisterHistoryEntity> findAllIncludingDeleted();

    // === SOLO ACTIVOS
    @Query("SELECT r FROM RegisterHistoryEntity r WHERE r.deletedAt IS NULL")
    List<RegisterHistoryEntity> findAllActive();

    // === SOLO ELIMINADOS
    @Query("SELECT r FROM RegisterHistoryEntity r WHERE r.deletedAt IS NOT NULL")
    List<RegisterHistoryEntity> findAllDeleted();

    // === GET ANY BY ID (IGNORA @Where)
    @Query(value = "SELECT * FROM registers_histories WHERE id = :id", nativeQuery = true)
    Optional<RegisterHistoryEntity> findAnyById(@Param("id") Integer id);

    // === PAGINACIÓN GENERAL
    @Query("SELECT r FROM RegisterHistoryEntity r")
    Page<RegisterHistoryEntity> findAllPaginated(Pageable pageable);

    // === PAGINACIÓN + FILTRO
    @Query("""
            SELECT r FROM RegisterHistoryEntity r
            WHERE (:term IS NULL
                OR LOWER(r.action) LIKE LOWER(CONCAT('%', :term, '%'))
                OR LOWER(r.jsonAfter) LIKE LOWER(CONCAT('%', :term, '%'))
                OR LOWER(r.jsonBefore) LIKE LOWER(CONCAT('%', :term, '%'))
                OR LOWER(r.jsonDiff) LIKE LOWER(CONCAT('%', :term, '%'))
                OR LOWER(r.user.firstName) LIKE LOWER(CONCAT('%', :term, '%'))
                OR LOWER(r.user.firstLastName) LIKE LOWER(CONCAT('%', :term, '%'))
            )
            """)
    Page<RegisterHistoryEntity> findAllPaginatedFiltered(
            @Param("term") String term,
            Pageable pageable
    );

    // === PAGINACIÓN SOLO ELIMINADOS
    @Query("""
            SELECT r FROM RegisterHistoryEntity r
            WHERE r.deletedAt IS NOT NULL AND
                (:term IS NULL
                    OR LOWER(r.action) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(r.jsonAfter) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(r.jsonBefore) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(r.jsonDiff) LIKE LOWER(CONCAT('%', :term, '%'))
                )
            """)
    Page<RegisterHistoryEntity> findAllDeletedPaginatedFiltered(
            @Param("term") String term,
            Pageable pageable
    );

    // === HISTORIAL AGRUPADO POR ESTABLECIMIENTO
    @Query("""
            SELECT r.register.stablishment.name,
                   COUNT(r)
            FROM RegisterHistoryEntity r
            GROUP BY r.register.stablishment.name
            """)
    List<Object[]> countHistoryByStablishment();

    // === HISTORIAL POR REGISTER (ACTIVOS)
    @Query("""
            SELECT r FROM RegisterHistoryEntity r
            WHERE r.register.id = :id
            ORDER BY r.createdAt DESC
            """)
    List<RegisterHistoryEntity> findByRegisterId(@Param("id") Integer id);

    // === HISTORIAL POR REGISTER (INCLUYE SOFT-DELETED)
    @Query(value = "SELECT * FROM registers_histories WHERE register_id = :id ORDER BY created_at DESC",
            nativeQuery = true)
    List<RegisterHistoryEntity> findAllByRegisterIdIncludingDeleted(@Param("id") Integer id);

}
