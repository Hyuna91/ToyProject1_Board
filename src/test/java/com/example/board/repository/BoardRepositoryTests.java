package com.example.board.repository;

import com.example.board.dto.PageRequestDTO;
import com.example.board.entity.Board;
import com.example.board.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    /** Dummy Date Insert */
    @Test
    public void insertBoard() {

        IntStream.rangeClosed(1, 100).forEach(i -> {

            Member member = Member.builder().email("user" + i + "@aaa.com").build();

            Board board = Board.builder()
                    .title("Title..." + i)
                    .content("content..." + i)
                    .writer(member)
                    .build();

            boardRepository.save(board);
        });
    }


    /** 조회 -> 자동으로 Join 처리됨 */
    /**  FetchType.LAZY로 변경하여 @Transactional을 추가함 */
    /**  @Transactional : 해당 메서드를 하나의 트랜잭션으로 처리(필요할 때 데이터 베이스와 다시 연결) */
    @Transactional
    @Test
    public void testRead1() {

        // 데이터 베이스에 bno-100을 찾는다.
        Optional<Board> result = boardRepository.findById(100L);

        // .get()을 통해서 Optional의 값을 꺼내온다.
        Board board = result.get();

        System.out.println(board);
        System.out.println(board.getWriter());
    }

    /** BoardRepository.java - getBoardWithWriter() 테스트 */
    @Test
    public void testReadWithWriter() {

        Object result = boardRepository.getBoardWithWriter(100L);

        Object[] arr = (Object[])result;

        System.out.println("---------------------------------------");
        System.out.println(Arrays.toString(arr));
    }

    /** BoardRepository.java - getBoardWithReply() 테스트 */
    @Test
    public void testGetBoardWithReply() {

        List<Object[]> result = boardRepository.getBoardWithReply(100L);

        for (Object[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }
    }

    /** BoardRepository.java - getBoardWithReplyCount() 테스트 */
    @Test
    public void testWithReplyCount() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);

        result.get().forEach(row -> {

            Object[] arr = (Object[])row;

            System.out.println(Arrays.toString(arr));
        });
    }

    /** BoardRepository.java - getBoardByBno() 테스트 */
    @Test
    public void testRead3() {

        Object result = boardRepository.getBoardByBno(100L);

        Object[] arr = (Object[])result;

        System.out.println(Arrays.toString(arr));

    }
}
