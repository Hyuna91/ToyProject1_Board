package com.example.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString( exclude = "writer" )
public class Board extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    
    private String title;
    
    private String content;
    
    // Board(writer) To Member(email)
    // fetch : 데이터를 어떻게 가져올 것인가
    // Lazy loading <-> Eager loading(연관관계가 있는 데이터를 모두 로딩)
    @ManyToOne (fetch = FetchType.LAZY)
    private Member writer;  // 연관관계 지정
    
}
