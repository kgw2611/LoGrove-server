package com.hansung.logrove.tag.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tag_name")
@Getter
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tag", nullable = false, unique = true)
    private TagName name;

    public Tag(TagName name) {
        this.name = name;
    }
}
