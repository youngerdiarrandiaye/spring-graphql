package com.youdev.SpringGrapQl.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Etudiant {
    @Id
    private int id;
    private String name;
    private String mobile;
    private String email;
    private String address;
}
