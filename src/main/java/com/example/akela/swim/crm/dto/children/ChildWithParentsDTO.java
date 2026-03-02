package com.example.akela.swim.crm.dto.children;

import com.example.akela.swim.crm.dto.parents.ParentDetailsDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data

public class ChildWithParentsDTO {

    private Long childId;
    private String childFirstName;
    private String childLastName;
    private LocalDate birthday;
    private String childUniqueIdentifier;
    private Boolean status;
    private List<String> parents;
    private List<ParentDetailsDTO> parentsDetails;

    public ChildWithParentsDTO(Long childId, String childFirstName, String childLastName, LocalDate birthday, String childUniqueIdentifier, Boolean status, List<String> parents) {
        this.childId = childId;
        this.childFirstName = childFirstName;
        this.childLastName = childLastName;
        this.birthday = birthday;
        this.childUniqueIdentifier = childUniqueIdentifier;
        this.status = status;
        this.parents = parents;
    }


    public ChildWithParentsDTO(Long childId, String childFirstName, String childLastName, LocalDate birthday, String childUniqueIdentifier, Boolean status, List<String> parents, List<ParentDetailsDTO> parentsDetails) {
        this.childId = childId;
        this.childFirstName = childFirstName;
        this.childLastName = childLastName;
        this.birthday = birthday;
        this.childUniqueIdentifier = childUniqueIdentifier;
        this.status = status;
        this.parents = parents;
        this.parentsDetails = parentsDetails;
    }

    public ChildWithParentsDTO() {}
}
