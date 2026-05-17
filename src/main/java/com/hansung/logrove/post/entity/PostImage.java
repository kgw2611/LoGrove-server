package com.hansung.logrove.post.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "images")
@Getter
@NoArgsConstructor
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "url", nullable = false, length = 500)
    private String url;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public PostImage(String url, Post post, Integer width, Integer height) {
        this.url = url;
        this.post = post;
        this.width = width;
        this.height = height;
    }
}
