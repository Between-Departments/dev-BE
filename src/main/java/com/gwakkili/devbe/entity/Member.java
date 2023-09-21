package com.gwakkili.devbe.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Member extends BaseEntity{

    @Id
    String username;

    private String password;

    private String nickname;

    private String mail;

    private String name;

    @OneToOne
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @OneToOne
    @JoinColumn(name = "major_id", nullable = false)
    private major major;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roleSet = new HashSet<>();

    private boolean locked;

    private LocalDateTime deleteAt;
}
