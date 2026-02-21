package com.example.akela.swim.crm.dto.parents;

public class ParentListDTO {
    private Long parentId;
    private String parentFirstName;
    private String parentLastName;
    private String phone;
    private String email;
    private String address;

    public ParentListDTO(Long parentId, String parentFirstName, String parentLastName,
                         String phone, String email, String address) {
        this.parentId = parentId;
        this.parentFirstName = parentFirstName;
        this.parentLastName = parentLastName;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public Long getParentId() { return parentId; }
    public String getParentFirstName() { return parentFirstName; }
    public String getParentLastName() { return parentLastName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
}
