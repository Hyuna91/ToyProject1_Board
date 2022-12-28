package com.example.board.service;

import com.example.board.dto.ReplyDTO;
import com.example.board.entity.Board;
import com.example.board.entity.Reply;
import com.example.board.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    /** 댓글 등록 */
    @Override
    public Long register(ReplyDTO replyDTO) {

        // 댓글로 등록하기 위해 DTO를 Entity로 변환
        Reply reply = dtoToEntity(replyDTO);

        replyRepository.save(reply);

        return reply.getRno();
    }

    /** 특정 게시물의 댓글 목록 */
    @Override
    public List<ReplyDTO> getList(Long bno) {

        List<Reply> result = replyRepository.getRepliesByBoardOrderByRno(Board.builder().bno(bno).build());

        return result.stream().map(reply -> entityToDTO(reply)).collect(Collectors.toList());

    }

    /** 댓글 수정 */
    @Override
    public void modify(ReplyDTO replyDTO) {

        Reply reply = dtoToEntity(replyDTO);

        replyRepository.save(reply);

    }

    /** 댓글 삭제 */
    @Override
    public void remove(Long rno) {

        replyRepository.deleteById(rno);

    }
}
