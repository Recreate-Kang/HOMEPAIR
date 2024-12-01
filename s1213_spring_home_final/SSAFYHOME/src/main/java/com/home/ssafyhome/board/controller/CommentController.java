package com.home.ssafyhome.board.controller;

import com.home.ssafyhome.board.dto.CommentListResponseDTO;
import com.home.ssafyhome.board.dto.CommentRequestDTO;
import com.home.ssafyhome.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/write/{articleId}")
    ResponseEntity<Void> writeComment(@PathVariable("articleId") int articleId, @RequestBody CommentRequestDTO comment) {
        commentService.writeComment(articleId, comment);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/edit/{articleId}")
    ResponseEntity<Void> editComment(@PathVariable("articleId") int articleId, @RequestBody CommentRequestDTO comment) {
        commentService.editComment(articleId, comment);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/list/{articleId}")
    ResponseEntity<CommentListResponseDTO> getAllComment(@PathVariable("articleId") int articleId, @RequestBody LocalDateTime registerTime) {

        return ResponseEntity.ok().body(commentService.getCommentsByArticleNo(articleId,registerTime));
    }
}
