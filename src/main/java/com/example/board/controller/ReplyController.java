package com.example.board.controller;

import com.example.board.dto.ReplyDTO;
import com.example.board.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Data를 반환하기 위해 @RestController를 사용(JSON 형식으로 반환)
@RestController
@RequestMapping("/replies/")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;    // 자동 주입을 위해 final

    // produces = MediaType.APPLICATION_JSON_VALUE : APPLICATION_JSON_VALUE 형태로 응답한다.
    @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno") Long bno) {

        log.info("bno : " + bno);

        // ResponseEntity라는 객체를 이용하면 HTTP 상태코드 등을 같이 전달할 수 있다.
        return new ResponseEntity<>(replyService.getList(bno), HttpStatus.OK);

    }

    /** REGISTER */
    // @RequestBody는 JSON으로 들어오는 데이터를 자동으로 해당 타입(ReplyDTO)의 객체로 매핑해준다.
    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO) {

        log.info(replyDTO);

        Long rno = replyService.register(replyDTO);

        return new ResponseEntity<>(rno, HttpStatus.OK);

    }

    /** DELETE*/
    @DeleteMapping("/{rno}")
    public ResponseEntity<String> remove(@PathVariable("rno") Long rno) {

        log.info("RNO : " + rno);

        replyService.remove(rno);

        return new ResponseEntity<>("success", HttpStatus.OK);

    }

    /** MODIFY */
    // @RequestBody는 JSON으로 들어오는 데이터를 자동으로 해당 타입(ReplyDTO)의 객체로 매핑해준다.

    @PutMapping("/{rno}")
    public ResponseEntity<String> modify(@RequestBody ReplyDTO replyDTO) {

        log.info(replyDTO);

        replyService.modify(replyDTO);

        return new ResponseEntity<>("success", HttpStatus.OK);

    }
}
