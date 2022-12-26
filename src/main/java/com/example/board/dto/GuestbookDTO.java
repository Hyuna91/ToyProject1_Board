package com.example.board.dto;

import lombok.*;

import java.time.LocalDateTime;

// GuestbookDTO는 Guestbook과 거의 동일한 필드들을 가지고 있고
// getter, setter를 통해 자유롭게 값을 변경할 수 있게 구성한다.
// 서비스계층에서 사용

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GuestbookDTO {

    private Long gno;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime regDate, modDate;

}


