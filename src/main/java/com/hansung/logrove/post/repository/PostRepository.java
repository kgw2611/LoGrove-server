    package com.hansung.logrove.post.repository;

    import com.hansung.logrove.post.entity.BoardType;
    import com.hansung.logrove.post.entity.Post;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;

    import java.util.List;

    public interface PostRepository extends JpaRepository<Post, Long> {

        // 게시판 종류별 글 목록 조회 (페이징)
        Page<Post> findByBoardType(BoardType boardType, Pageable pageable);

        // 내가 작성한 글 목록
        List<Post> findByUserId(Long userId);

        // 제목 검색
        Page<Post> findByTitleContaining(String title, Pageable pageable);

        // 게시판 + 제목 검색
        Page<Post> findByBoardTypeAndTitleContaining(BoardType boardType, String title, Pageable pageable);

        // 게시판 + 태그 검색 (중복 제거)
        @Query("SELECT DISTINCT p FROM Post p JOIN PostTag pt ON pt.post = p WHERE p.boardType = :boardType AND pt.tag.id IN :tagIds")
        Page<Post> findByBoardTypeAndTagIds(@Param("boardType") BoardType boardType, @Param("tagIds") List<Long> tagIds, Pageable pageable);

        // 게시판 + 제목 + 태그 복합 검색
        @Query("SELECT DISTINCT p FROM Post p JOIN PostTag pt ON pt.post = p WHERE p.boardType = :boardType AND p.title LIKE %:title% AND pt.tag.id IN :tagIds")
        Page<Post> findByBoardTypeAndTitleContainingAndTagIds(@Param("boardType") BoardType boardType, @Param("title") String title, @Param("tagIds") List<Long> tagIds, Pageable pageable);
    }