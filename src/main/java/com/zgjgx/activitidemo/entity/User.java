package com.zgjgx.activitidemo.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "sys_user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_user_id;

    @Column(name = "name", nullable = false)
    private String name;
}
