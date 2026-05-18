package com.hansung.logrove.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NeighborPostsResponse {

    private List<PostListResponse> newer;
    private List<PostListResponse> older;
}
