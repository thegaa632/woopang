package com.standout.sopang.springex.controller;

import com.standout.sopang.springex.dto.PageRequestDTO;
import com.standout.sopang.springex.dto.PageResponseDTO;
import com.standout.sopang.springex.dto.TodoDTO;
import com.standout.sopang.springex.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;


@Controller("TodoController")
@RequestMapping("/todo")
@Log4j2
@RequiredArgsConstructor
public class TodoController {

    @Autowired
    private final TodoService todoService;

    String goodsIdValue;

    @GetMapping("/register")
    public void register() {
        log.info("GET todo register.......");
//        log.info("goods_id : " + goods_id);
//        goods_id1 = goods_id.toString();
//
//        String[] parts = goods_id1.split("=");
//        String goods_id1 = parts[1];
//        String[] parts1 = goods_id1.split("}");
//        goods_id1 = parts1[0];
//        log.info("goods_id1 :" + goods_id1);
//
//        model.addAttribute("goods_id", goods_id1);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@Valid TodoDTO todoDTO,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           @RequestParam Map<String, Object> goods_id,
                           Model model
    ) {

        log.info("POST todo register.......");
        log.info("goods_id : " + goods_id);

        model.addAttribute("totalModel", goods_id);
        if (bindingResult.hasErrors()) {
            log.info("has errors.......");
            goodsIdValue = (String) goods_id.get("goods_id");
            log.info("goodsIdValue : " + goodsIdValue);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/todo/register";
        }
        todoService.register(todoDTO);
        return "redirect:/goods/goodsDetail?goods_id="+goodsIdValue;
    }

    @GetMapping({"/read", "/modify"})
    public void read(Long tno, Model model) {

        TodoDTO todoDTO = todoService.getOne(tno);
        log.info("todoDTO : "+ todoDTO);

        model.addAttribute("dto", todoDTO);
    }


    @PostMapping("/remove")
    public String remove(Long tno, PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes) {

        log.info("-------------remove------------------");
        log.info("tno: " + tno);

        todoService.remove(tno);

        return "redirect:/todo/list?" + pageRequestDTO.getLink();
    }

    @PostMapping("/modify")
    public String modify(
            PageRequestDTO pageRequestDTO,
            @Valid TodoDTO todoDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("tno", todoDTO.getTno());
            return "redirect:/todo/modify";
        }

        log.info("todoDTO :"+todoDTO);

        todoService.modify(todoDTO);

        redirectAttributes.addAttribute("tno", todoDTO.getTno());

        return "redirect:";
    }

    //    @ResponseBody
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public void list() {
        log.info("list get 호출");
    }

    @ResponseBody
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public PageResponseDTO<TodoDTO> list(@Valid PageRequestDTO pageRequestDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        log.info("list post 호출");

        TodoDTO dto;

        if (bindingResult.hasErrors()) {
            pageRequestDTO = PageRequestDTO.builder().build();
        }
        model.addAttribute("responseDTO", todoService.getList(pageRequestDTO));

        PageResponseDTO<TodoDTO> pageResponseDTO = todoService.getList(pageRequestDTO);
        log.info("pageResponseDTO 입니다. : " + pageResponseDTO);
        return pageResponseDTO;
    }
}
