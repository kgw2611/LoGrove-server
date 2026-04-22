package com.hansung.logrove.post.repository;

import com.hansung.logrove.post.entity.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardTypeRepository extends JpaRepository<BoardType, Long> {

    Optional<BoardType> findByBoard(String board);
}
