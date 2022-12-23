package com.example.board.repository;

import com.example.board.entity.Guestbook;
import com.example.board.entity.QGuestbook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    /** Dummy Data Insert */
    @Test
    public void insertDummies() {

        // IntStream는 특정 범위의 숫자를 차례대로 생성/rangeClosed는 끝 값(300)까지 생성
        IntStream.rangeClosed(1,300).forEach(i -> {

            Guestbook guestbook = Guestbook.builder()
                    .title("Title....." + i)
                    .content("Content....." + i)
                    .writer("user" + (i % 10))
                    .build();
            System.out.println(guestbookRepository.save(guestbook));
        });
    }

    /** Id가 300인 엔티티 수정 */
    @Test
    public void updateTest() {

        Optional<Guestbook> result = guestbookRepository.findById(300L);

        if(result.isPresent()) {

            Guestbook guestbook = result.get();

            guestbook.changeTitle("Changed Title...");
            guestbook.changeContent("Change Content...");

            guestbookRepository.save(guestbook);
        }
    }
    
    /** 단일 항목 검색 테스트 */
    @Test
    public void testQuery() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        // 동적으로 처리하기 위해서 Q도메인 클래스를 얻어온다.
        // Q도메인 클래스를 이용하면 엔티티 클래스에 선언된 title, content같은 필드를 변수로 사용할 수 있다.
        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        // BooleanBuilder는 조건을 넣어주는 컨테이너
        BooleanBuilder builder = new BooleanBuilder();

        // title에 1이라는 글자가 들어 있는 엔티티 검색
        BooleanExpression expression = qGuestbook.title.contains(keyword);

        // 만들어진 조건은 where문에 and나 or 같은 키워드와 조합
        builder.and(expression);


        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }
    
    /** 다중 항목 검색 테스트 */
    @Test
    public void testQuery2() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        // 조건을 담는 컨테이너
        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);

        BooleanExpression exContent = qGuestbook.content.contains(keyword);

        // exTitle과 exContent라는 BooleanExpression를 결합
        BooleanExpression exAll = exTitle.or(exContent);

        // 결합한 조건을 BooleanBuilder에 추가
        builder.and(exAll);

        // 이후에 gno가 0보다 크다는 조건을 추가
        builder.and(qGuestbook.gno.gt(0L));

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }

}
