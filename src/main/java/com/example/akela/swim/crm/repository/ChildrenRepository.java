package com.example.akela.swim.crm.repository;

import com.example.akela.swim.crm.entity.ChildrenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildrenRepository  extends JpaRepository<ChildrenEntity, Long> {
    @Query(value = """
        SELECT 
            c.child_id AS childId,
            c.child_first_name AS childFirstName,
            c.child_last_name AS childLastName,
            c.birthday AS birthday,
            c.child_unique_identifier AS childUniqueIdentifier,
            c.status as status,
            COALESCE(
                string_agg(
                    DISTINCT CONCAT(p.parent_first_name, ' ', p.parent_last_name),
                ', '), 
            '') AS parents
        FROM akl_children c
        LEFT JOIN akl_assoc_parent_children a ON a.child_id = c.child_id
        LEFT JOIN akl_parents p ON p.parent_id = a.parent_id
        GROUP BY c.child_id, c.child_first_name, c.child_last_name, c.birthday, c.child_unique_identifier
        ORDER BY c.child_last_name
        """,
            nativeQuery = true)
    List<Object[]> findAllChildrenWithParentsNative();

    long countByStatusTrue();
}
