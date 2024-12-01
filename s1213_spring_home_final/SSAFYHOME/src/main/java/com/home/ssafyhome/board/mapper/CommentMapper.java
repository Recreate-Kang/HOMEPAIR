package com.home.ssafyhome.board.mapper;

import com.home.ssafyhome.board.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    // 댓글 삽입
    void insertComment(Comment comment);

    // 특정 댓글 조회
    Comment findCommentById(Long commentId);

    // 특정 게시글의 댓글 목록 조회
    List<Comment> findCommentsByArticleNo(Integer articleNo);

    // 댓글 수정
    int updateComment(Comment comment); // 수정 성공 시 1 반환

    // 댓글 삭제
    int deleteCommentById(@Param("commentId") Long commentId, @Param("userId") String userId); // 삭제 성공 시 1 반환
}
