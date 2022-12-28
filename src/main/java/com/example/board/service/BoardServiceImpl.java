package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.PageRequestDTO;
import com.example.board.dto.PageResultDTO;
import com.example.board.entity.Board;
import com.example.board.entity.Member;
import com.example.board.entity.Reply;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{

    private final BoardRepository repository;   // 자동 주입 final

    private final ReplyRepository replyRepository;
    
    /** 게시물 등록 */
    @Override
    public Long register(BoardDTO dto) {

        log.info(dto);

        // DTO를 Entity 형태로 바꿔서 Save
        Board board = dtoToEntity(dto);

        repository.save(board);

        return board.getBno();
    }

    /** 게시물 리스트 받아오기 */
    /** entityToDTO()를 이용해서 PageRequestDTO 객체를 구성 */
    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        log.info(pageRequestDTO);

        // Function<EN, DTO>를 받아서
        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board)en[0],(Member)en[1],(Long)en[2]));

//        Page<Object[]> result = repository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));
        Page<Object[]> result = repository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending())
        );

        return new PageResultDTO<>(result, fn);
    }

    /** 게시물 조회 */
    @Override
    public BoardDTO get(Long bno) {

        Object result = repository.getBoardByBno(bno);

        Object[] arr = (Object[])result;

        return entityToDTO((Board)arr[0], (Member)arr[1], (Long) arr[2] );

    }

    /** 게시물 삭제 */
    /** 해당 게시물의 모든 댓글의 삭제와 해당 게시물의 삭제를 한 transaction으로 처리하기 위해 @Transactional을 추가 */
    @Transactional
    @Override
    public void removeWithReplies(Long bno) {

        // 해당 게시물의 모든 댓글 삭제
        replyRepository.deleteByBno(bno);

        // 해당 게시물의 삭제
        repository.deleteById(bno);

    }

    /** 게시물 수정 */
    @Override
    public void modify(BoardDTO boardDTO) {

        // findById의 return 값이 Optional
        Optional<Board> board = repository.findById(boardDTO.getBno());

        if(board != null) {

            board.get().changeTitle(boardDTO.getTitle());
            board.get().changeContent(boardDTO.getContent());

            repository.save(board.get());

        }


    }
}
