package com.example.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board")
public class Reply extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;
    
    private String text;
    
    private String replyer;
    
    // Reply과 Board와의 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
}
