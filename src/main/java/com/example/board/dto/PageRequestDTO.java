package com.example.board.dto;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {
    // 화면에서 전달되는 page라는 파라미터와 size라는 파라미터를 수집하는 역할
    // 페이지 번호 등은 기본값을 가지는 것이 좋기 때문에 1과 10을 사용

    private int page;
    private int size;

    /** 검색 조건 */
    private String type;
    /** 검색 키워드 */
    private String keyword;

    public PageRequestDTO() {
        this.page = 1;
        this.size = 10;
    }
    
    public Pageable getPageable(Sort sort) {
    
        // JPA에서 page 번호가 0부터 시작하기 때문에 -1
        // sort는 별도의 파라미터로 받도록 설계
        return PageRequest.of(page -1, size, sort);
    }

}
