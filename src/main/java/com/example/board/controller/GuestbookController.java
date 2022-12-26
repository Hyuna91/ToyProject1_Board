package com.example.board.controller;

import com.example.board.dto.GuestbookDTO;
import com.example.board.dto.PageRequestDTO;
import com.example.board.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestbookController {

    private final GuestbookService service;
    @GetMapping("/")
    public String index() {

        return "redirect:/guestbook/list";

    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {

        log.info("list............." + pageRequestDTO);

        model.addAttribute("result", service.getList(pageRequestDTO));

    }

    @GetMapping("/register")
    public void register() {
        log.info("register get...");
    }

    @PostMapping("/register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes) {

        log.info("dto....", dto);

        // 새로 추가된 엔티티의 번호
        Long gno = service.register(dto);

        // redirectAttributes를 이용하여 한 번만 화면에서 msg라는 이름의 변수를 사용할 수 있도록 처리
        // addFlashAttribute() 단 한번만 데이터를 전달하는 용도
        redirectAttributes.addFlashAttribute("msg", gno);

        return "redirect:/guestbook/list";

    }

    /** 방명록 조회 처리 */
    /** @ModelAttribute로 처리된 requestDTO로 페이지 관련된 처리를 한다. */
//    @GetMapping("/read")
//    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
//
//        log.info("gno: " + gno);
//
//        GuestbookDTO dto = service.read(gno);
//
//        model.addAttribute("dto", dto);
//
//    }
    
    /** 조회 & 수정 */
    @GetMapping({"/read", "/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {

        log.info("gno: " + gno);

        GuestbookDTO dto = service.read(gno);

        model.addAttribute("dto", dto);

    }

    /** 삭제 */
    @PostMapping("/remove")
    public String remove(long gno, RedirectAttributes redirectAttributes) {

        log.info("gno: " + gno);

        service.remove(gno);

        redirectAttributes.addFlashAttribute("msg", gno);

        return "redirect:/guestbook/list";

    }

    /** 수정 */
    @PostMapping("/modify")
    public String modify(GuestbookDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes) {
        // GuestbookDTO : 수정해야 할 정보
        // PageRequestDTO : 기존의 페이지 정보를 유지
        // RedirectAttributes : 리다이렉트로 이동

        log.info("post modify.........................");
        log.info("dto: " + dto);

        service.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("gno", dto.getGno());

        return "redirect:/guestbook/read";

    }



}
