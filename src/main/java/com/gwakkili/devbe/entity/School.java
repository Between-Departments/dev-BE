package com.gwakkili.devbe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class School {

    @Id
    @Column(name = "school_id")
    long id;

    String name;

    String mail;

}
