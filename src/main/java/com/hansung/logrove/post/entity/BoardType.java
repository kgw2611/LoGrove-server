package com.hansung.logrove.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "board_type")
@Getter
@NoArgsConstructor
public class BoardType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(name = "board_name", nullable = false, unique = true, length = 20)
    private String board;

    public BoardType(String board) {
        this.board = board;
    }
}
