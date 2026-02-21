package com.example.akela.swim.crm.dto;

import com.example.akela.swim.crm.dto.children.ChildDTO;
import com.example.akela.swim.crm.dto.parents.ParentAssocDTO;

import java.util.List;

public class SaveChildWithParentsDTO {
    private ChildDTO child;
    private List<ParentAssocDTO> parents;

    public ChildDTO getChild() {
        return child;
    }

    public void setChild(ChildDTO child) {
        this.child = child;
    }

    public List<ParentAssocDTO> getParents() {
        return parents;
    }

    public void setParents(List<ParentAssocDTO> parents) {
        this.parents = parents;
    }
}
