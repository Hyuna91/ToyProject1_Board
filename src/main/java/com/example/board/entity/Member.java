package com.example.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 생성
@NoArgsConstructor  // 파라미터가 없는 기본 생성자를 생성
@Getter
@ToString
public class Member extends BaseEntity {

    @Id
    private String email;

    private String password;

    private String name;
}
