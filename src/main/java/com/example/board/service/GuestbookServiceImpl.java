package com.example.board.service;

import com.example.board.dto.GuestbookDTO;
import com.example.board.dto.PageRequestDTO;
import com.example.board.dto.PageResultDTO;
import com.example.board.entity.Guestbook;
import com.example.board.entity.QGuestbook;
import com.example.board.repository.GuestbookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor    // 의존성 자동 주입
public class GuestbookServiceImpl implements GuestbookService{

    //JPA처리를 위해 GuestbookRepository를 주입
    private final GuestbookRepository repository;

    // GuestbookDTO를 Guestbook 엔티티로 변환
    @Override
    public Long register(GuestbookDTO dto) {

        log.info("DTO------------------");
        log.info(dto);

        Guestbook entity = dtoToEntity(dto);

        log.info(entity);

        repository.save(entity);

        return entity.getGno();
    }

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        // 검색 조건 처리
        BooleanBuilder booleanBuilder = getSearch(requestDTO);

        // Querydsl 사용
        Page<Guestbook> result = repository.findAll(booleanBuilder, pageable);

        // entityToDTO를 이용하여 Function을 구성 (Method와 Function의 차이는)
        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));

        /** TODO : 여기 다시 보기 */
//        Guestbook ge =  result.getContent().get(0);
//        GuestbookDTO dto = new GuestbookDTO();
//        dto.setGno(ge.getGno());

        // Page<Entity>와 Function의 결과 (result, fn)을 전달하여 엔티티 객체들을 DTO의 리스트로 변환
        return new PageResultDTO<>(result, fn);
    }

    /** 방명록 조회 처리 */
    @Override
    public GuestbookDTO read(Long gno) {

        Optional<Guestbook> result = repository.findById(gno);

        return result.isPresent()? entityToDto(result.get()): null;

    }

    /** 삭제 */
    @Override
    public void remove(Long gno) {

        repository.deleteById(gno);

    }

    /** 수정 */
    @Override
    public void modify(GuestbookDTO dto) {

        // 업데이트 하는 항목은 '제목', '내용'
        Optional<Guestbook> result = repository.findById(dto.getGno());

        if(result.isPresent()) {

            Guestbook entity = result.get();

            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());

            repository.save(entity);

        }

    }
    
    /** 검색 */
    private BooleanBuilder getSearch(PageRequestDTO requestDTO) {
        String type = requestDTO.getType();

        // Querydsl을 사용하기 위해 BooleanBuilder를 생성
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        // 동적으로 처리하기 위해서 Q도메인 클래스를 얻어온다.(Entity를 바로 사용하기 위해)
        QGuestbook qGuestbook = QGuestbook.guestbook;
        
        String keyword = requestDTO.getKeyword();

        // gno > 0 조건 생성
        BooleanExpression expression = qGuestbook.gno.gt(0L);
        
        booleanBuilder.and(expression);
        
        // 검색 조건이 없는 경우
        if(type == null || type.trim().length() == 0) {
            return booleanBuilder;
        }

        // 검색 조건 작성
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t")) {
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        }

        if(type.contains("c")) {
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        }

        if(type.contains("w")) {
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        }

        // 모든 조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
        
        // -> getList()에 검색 코드 추가
        
    }
}
