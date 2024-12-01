package com.home.ssafyhome.board.service;

import com.home.ssafyhome.board.dto.CommentListResponseDTO;
import com.home.ssafyhome.board.dto.CommentRequestDTO;
import com.home.ssafyhome.board.mapper.CommentMapper;
import com.home.ssafyhome.board.model.Comment;
import com.home.ssafyhome.board.util.CommentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    @Override
    public void writeComment(int articleId, CommentRequestDTO comment) {

        commentMapper.insertComment(CommentUtil.toModel(articleId, comment));
    }

    @Override
    public Comment getCommentById(Long commentId) {
        return null;
    }

    @Override
    public void editComment(int articleId, CommentRequestDTO comment) {

    }

    @Override
    public void removeComment(Long commentId, String userId) {

    }

    @Override
    public CommentListResponseDTO getCommentsByArticleNo(int articleId, LocalDateTime registerTime) {

        return CommentUtil.buildCommentTree(commentMapper.findCommentsByArticleNo(articleId));
    }
}
