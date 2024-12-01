package com.home.ssafyhome.board.controller;

import com.home.ssafyhome.board.dto.BoardRequestDTO;
import com.home.ssafyhome.board.dto.BoardResponseDTO;
import com.home.ssafyhome.board.dto.SubjectResponseDTO;
import com.home.ssafyhome.board.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/write")
    public ResponseEntity<Void> writeBoard(@RequestBody BoardRequestDTO boardDTO) {
        boardService.writeBoard(boardDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateBoard(@RequestBody BoardRequestDTO boardDTO) {
        boardService.updateBoard(boardDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/read/{num}")
    public ResponseEntity<BoardResponseDTO> readBoard(@PathVariable("num") Long num) {
        BoardResponseDTO response = boardService.findArticle(num);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<SubjectResponseDTO>> getBoardlists(@RequestParam int page, @RequestParam int size) {
        System.out.println("getBoardlists");
        Page<SubjectResponseDTO> response = boardService.getBoardLists(page, size);
        return ResponseEntity.ok().body(response);
    }
}
