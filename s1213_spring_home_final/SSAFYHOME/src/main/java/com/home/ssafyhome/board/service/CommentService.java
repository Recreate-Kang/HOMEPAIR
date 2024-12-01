package com.home.ssafyhome.board.service;

import com.home.ssafyhome.board.dto.CommentListResponseDTO;
import com.home.ssafyhome.board.dto.CommentRequestDTO;
import com.home.ssafyhome.board.model.Comment;

import java.time.LocalDateTime;

public interface CommentService {

    // 댓글 생성
    public void writeComment(int articleId, CommentRequestDTO comment);
    // 특정 댓글 조회
    public Comment getCommentById(Long commentId);

    // 특정 게시글의 댓글 조회
    // 댓글 수정
    void editComment(int articleId, CommentRequestDTO comment);

    // 댓글 삭제
    public void removeComment(Long commentId, String userId);

    CommentListResponseDTO getCommentsByArticleNo(int articleId, LocalDateTime registerTime);
}
