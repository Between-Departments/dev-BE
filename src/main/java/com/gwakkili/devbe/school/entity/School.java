package com.gwakkili.devbe.school.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long schoolId;

    @Column(unique = true)
    String name;

    String mail;

    @Builder
    public School(String name, String mail) {
        this.name = name;
        this.mail = mail;
    }
}
