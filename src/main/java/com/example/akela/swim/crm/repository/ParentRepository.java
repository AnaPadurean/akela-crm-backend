package com.example.akela.swim.crm.repository;

import com.example.akela.swim.crm.dto.parents.ParentListDTO;
import com.example.akela.swim.crm.entity.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParentRepository extends JpaRepository<ParentEntity, Long> {
    @Query(value = """
        SELECT 
            p.parent_id AS parentId,
            p.parent_first_name AS parentFirstName,
            p.parent_last_name AS parentLastName,
            p.phone AS phone,
            p.email AS email,
            p.address AS address,
            COALESCE(
                string_agg(
                    DISTINCT CONCAT(c.child_first_name, ' ', c.child_last_name),
                ', '), 
            '') AS children
        FROM akl_parents p
        LEFT JOIN akl_assoc_parent_children a ON a.parent_id = p.parent_id
        LEFT JOIN akl_children c ON c.child_id = a.child_id
        GROUP BY p.parent_id, p.parent_first_name, p.parent_last_name, p.phone, p.email, p.address
        ORDER BY p.parent_last_name
        """,
            nativeQuery = true)
    List<Object[]> findAllParentsWithChildrenNative();

    @Query("""
    SELECT new com.example.akela.swim.crm.dto.parents.ParentListDTO(
        p.parentId,
        p.parentFirstName,
        p.parentLastName,
        p.phone,
        p.email,
        p.address
    )
    FROM ParentEntity p
    ORDER BY p.parentLastName
    """)
    List<ParentListDTO> findAllForList();

}
