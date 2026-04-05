package com.hansung.logrove.tag.service;

import com.hansung.logrove.tag.dto.TagResponse;
import com.hansung.logrove.tag.entity.Tag;
import com.hansung.logrove.tag.entity.TagName;
import com.hansung.logrove.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    // 전체 태그 목록 조회 — 프론트 태그 필터 UI에서 사용
    @Transactional(readOnly = true)
    public List<TagResponse> getAllTags() {
        return tagRepository.findAll().stream()
                .map(TagResponse::from)
                .toList();
    }

    // 태그명으로 조회, 없으면 새로 생성 — 추후 Gemini AI 연동 시 사용
    @Transactional
    public Tag findOrCreate(TagName name) {
        return tagRepository.findByName(name)
                .orElseGet(() -> tagRepository.save(new Tag(name)));
    }
}