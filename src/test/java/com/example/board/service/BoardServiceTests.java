package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.PageRequestDTO;
import com.example.board.dto.PageResultDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    /** REGISTER */
    @Test
    public void testRegister() {

        BoardDTO dto = BoardDTO.builder()
                .title("Test.")
                .content("Test...")
                .writerEmail("user10@aaa.com")
                .build();
        Long bno = boardService.register(dto);
    }

    /** getList() */
    @Test
    public void testList() {

        // PageRequestDTO를 받아오고
        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        // getList()를 통해서 pageRequestDTO를 PageResultDTO를 받아오고
        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);

        // 배열(result.getDtoList())를 대입받을 변수정의(BoardDTO boardDTO)
        for (BoardDTO boardDTO : result.getDtoList()) {
            System.out.println(boardDTO);
        }
    }

    /** 게시물 조회 boardService.get() 확인 */
    @Test
    public void testGet() {

        Long bno = 100L;

        BoardDTO boardDTO = boardService.get(bno);

        System.out.println(boardDTO);
    }

    /** 게시물 삭제 */
    @Test
    public void testRemove() {

        Long bno = 1L;

        boardService.removeWithReplies(bno);

    }

    /** 게시물 수정 */
    @Test
    public void testModify() {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(2L)
                .title("제목 변경합니다.")
                .content("내용 변경합니다.")
                .build();

        boardService.modify(boardDTO);
    }
}
