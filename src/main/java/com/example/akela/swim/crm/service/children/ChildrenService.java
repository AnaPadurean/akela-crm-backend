package com.example.akela.swim.crm.service.children;

import com.example.akela.swim.crm.dto.children.ChildWithParentsDTO;
import com.example.akela.swim.crm.entity.ChildrenEntity;
import com.example.akela.swim.crm.repository.ChildrenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ChildrenService {

    private final ChildrenRepository childrenRepository;

    public ChildrenService(ChildrenRepository childrenRepository) {
        this.childrenRepository = childrenRepository;
    }

    public List<ChildrenEntity> findAll() {
        return childrenRepository.findAll();
    }

    public Optional<ChildrenEntity> findById(Long id) {
        return childrenRepository.findById(id);
    }

    public ChildrenEntity save(ChildrenEntity child) {
        return childrenRepository.save(child);
    }

    public void delete(Long id) {
        childrenRepository.deleteById(id);
    }

    public List<ChildWithParentsDTO> findAllAsDto() {
        List<Object[]> rows = childrenRepository.findAllChildrenWithParentsNative();
        return rows.stream().map(r -> {
            Long id = ((Number) r[0]).longValue();
            String firstName = (String) r[1];
            String lastName = (String) r[2];
            LocalDate birthday = null;
            if (r[3] != null) {
                if (r[3] instanceof java.sql.Timestamp ts) {
                    birthday = ts.toLocalDateTime().toLocalDate();
                } else if (r[3] instanceof java.sql.Date d) {
                    birthday = d.toLocalDate();
                }
            }

            String uniqueId = (String) r[4];
            Boolean status = (Boolean) r[5];
            String parentsRaw = (String) r[6];
            List<String> parents = parentsRaw != null && !parentsRaw.isBlank()
                    ? List.of(parentsRaw.split(",\\s*"))
                    : List.of();

            return new ChildWithParentsDTO(id, firstName, lastName, birthday, uniqueId, status,  parents);
        }).toList();
    }

}
