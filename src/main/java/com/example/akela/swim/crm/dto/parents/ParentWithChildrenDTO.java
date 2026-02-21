package com.example.akela.swim.crm.dto.parents;

import java.util.List;

public class ParentWithChildrenDTO {

    private Long parentId;
    private String parentFirstName;
    private String parentLastName;
    private String phone;
    private String email;
    private String address;
    private List<String> children; // ex: ["Andrei Popescu", "Ioana Popescu"]

    public ParentWithChildrenDTO(Long parentId, String parentFirstName, String parentLastName,
                                 String phone, String email, String address, List<String> children) {
        this.parentId = parentId;
        this.parentFirstName = parentFirstName;
        this.parentLastName = parentLastName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.children = children;
    }

    public Long getParentId() { return parentId; }
    public String getParentFirstName() { return parentFirstName; }
    public String getParentLastName() { return parentLastName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public List<String> getChildren() { return children; }
}
