package com.example.board.repository;

import com.example.board.entity.Board;
import com.example.board.entity.Reply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.*;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    /** Dummy Date Insert */
    @Test
    public void insertReply() {

        IntStream.rangeClosed(1, 300).forEach(i -> {
            // 1부터 100까지의 임의의 번호
            long bno = (long)(Math.random() * 100) + 1;

            Board board = Board.builder().bno(bno).build();

            Reply reply = Reply.builder()
                    .text("Reply..." + i)
                    .board(board)
                    .replyer("guest")
                    .build();

            replyRepository.save(reply);
        });
    }

    /** 조회 -> 자동으로 Join 처리됨 */
    @Test
    public void readReply1() {

        Optional<Reply> result = replyRepository.findById(1L);

        Reply reply = result.get();

        System.out.println(reply);
        System.out.println(reply.getBoard());
    }
}
