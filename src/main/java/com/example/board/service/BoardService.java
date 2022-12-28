package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.PageRequestDTO;
import com.example.board.dto.PageResultDTO;
import com.example.board.entity.Board;
import com.example.board.entity.Member;
import com.example.board.repository.BoardRepository;

public interface BoardService {

    /** 게시물 등록 */
    Long register(BoardDTO dto);

    /** 게시물 리스트 받아오기 */
    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    /** 게시물 조회 */
    BoardDTO get(Long bno);

    /** 게시물 등록 시 사용할 메소드 */
    default Board dtoToEntity(BoardDTO dto) {

        Member member = Member.builder().email(dto.getWriterEmail()).build();

        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();
        return board;
    }

    /** Object[]를 DTO로 변환하기 */
    default BoardDTO entityToDTO(Board board, Member member, Long replyCount) {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue()) // Long으로 나오므로 int로 처리하도록
                .build();

        return boardDTO;
    }

    /** 게시물 삭제 */
    void removeWithReplies(Long bno);

    /** 게시물 수정 : BoardDTO를 이용하여 수정 */
    void modify(BoardDTO boardDTO);
}
