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
public class major {

    @Id
    @Column(name = "major_id")
    long id;

    String category;

    String name;
}
