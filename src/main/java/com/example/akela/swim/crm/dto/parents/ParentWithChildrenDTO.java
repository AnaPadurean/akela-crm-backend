package com.example.akela.swim.crm.dto.parents;

import lombok.Getter;

import java.util.List;

@Getter
public class ParentWithChildrenDTO {

    private Long parentId;
    private String parentFirstName;
    private String parentLastName;
    private String phone;
    private String email;
    private String address;
    private List<String> children;

    public ParentWithChildrenDTO(Long parentId, String parentFirstName, String parentLastName, String phone, String email, String address, List<String> children) {
        this.parentId = parentId;
        this.parentFirstName = parentFirstName;
        this.parentLastName = parentLastName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.children = children;
    }

}
