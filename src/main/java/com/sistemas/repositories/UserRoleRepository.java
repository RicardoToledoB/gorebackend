package com.sistemas.repositories;
import com.sistemas.entities.RoleEntity;
import com.sistemas.entities.UserRoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Integer> {

    @Query(
            value = "SELECT * FROM users_roles c WHERE c.deleted_at IS NOT NULL",
            nativeQuery = true
    )
    List<UserRoleEntity> findAllDeleted();

    @Query("SELECT ur FROM UserRoleEntity ur WHERE ur.deletedAt IS NULL")
    List<UserRoleEntity> findAllActive();

    @Query(value = "SELECT * FROM users_roles c WHERE c.id = :id", nativeQuery = true)
    Optional<UserRoleEntity> findAnyById(@Param("id") Integer id);

    @Query(value = "SELECT * FROM users_roles", nativeQuery = true)
    List<UserRoleEntity> findAllIncludingDeleted();

    @Query("SELECT c FROM UserRoleEntity c")
    Page<UserRoleEntity> findAllPaginated(Pageable pageable);

    @Query("""
       SELECT c FROM UserRoleEntity c
       WHERE (:id IS NULL OR c.id = :id)
    """)
    Page<UserRoleEntity> search(@Param("id") Integer id, Pageable pageable);

    @Query("""
           select ur.role.name
           from UserRoleEntity ur
           where ur.user.id = :userId
           """)
    List<String> findRoleNamesByUserId(Integer userId);



    @Query("""
       select ur.role
       from UserRoleEntity ur
       where ur.user.id = :userId
         and ur.deletedAt is null
       """)
    List<RoleEntity> findRolesByUserId(@Param("userId") Integer userId);

}
