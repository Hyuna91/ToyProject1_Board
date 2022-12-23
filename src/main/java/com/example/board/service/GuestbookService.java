package com.example.board.service;

import com.example.board.dto.GuestbookDTO;
import com.example.board.entity.Guestbook;

public interface GuestbookService {

    Long register(GuestbookDTO dto);

    // 기존에 만든 DTO를 최대한 보존하기 위해 default 메서드를 이용하여 처리
    // 인터페이스의 실제 내용을 가지는 코드를 default라는 키워드로 생성할 수 있다.
    default Guestbook dtoToEntity(GuestbookDTO dto) {
        Guestbook entity = Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }

}