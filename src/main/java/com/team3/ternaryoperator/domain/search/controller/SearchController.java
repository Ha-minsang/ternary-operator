package com.team3.ternaryoperator.domain.search.controller;

import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.domain.search.model.response.SearchResponse;
import com.team3.ternaryoperator.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<CommonResponse<SearchResponse>> getSearch(
            @RequestParam("query") String query
    ) {
        SearchResponse result = searchService.search(query);

        return ResponseEntity
                .ok(CommonResponse.success(result, "검색 성공"));
    }
}
