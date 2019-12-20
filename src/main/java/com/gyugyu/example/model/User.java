package com.gyugyu.example.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class User {

    @Id @GeneratedValue // default: AUTO
    @Column(name = "USER_ID")
    private Long id;

    private String name;

    private String city;
    private String street;
    private String zipcode;

}
