package com.example.board.entity;

import lombok.Getter;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

// BoardApplication에 @EnableJpaAuditing를 추가해야 AuditingEntityListener를 활성화 시킬 수 있다.
// 자동으로 처리되는 날짜/시간 설정
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
abstract class BaseEntity {

    @CreatedDate
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(name = "moddate")
    private LocalDateTime modDate;
}
