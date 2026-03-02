package com.example.akela.swim.crm.dto;

import com.example.akela.swim.crm.dto.children.ChildDTO;
import com.example.akela.swim.crm.dto.parents.ParentAssocDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SaveChildWithParentsDTO {
    private ChildDTO child;
    private List<ParentAssocDTO> parents;

}
