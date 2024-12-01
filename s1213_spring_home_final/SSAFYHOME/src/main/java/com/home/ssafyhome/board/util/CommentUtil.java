package com.home.ssafyhome.board.util;


import com.home.ssafyhome.board.dto.CommentListResponseDTO;
import com.home.ssafyhome.board.dto.CommentRequestDTO;
import com.home.ssafyhome.board.dto.CommentResponseDTO;
import com.home.ssafyhome.board.model.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentUtil {

    /**
     * Converts a CommentRequestDTO to a Comment entity.
     *
     * @param dto the CommentRequestDTO
     * @return the Comment entity
     */
    public static Comment toModel(Integer articleId, CommentRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Comment comment = new Comment();
        comment.setArticleNo(articleId);
        comment.setCommentId(dto.getCommentId());
        comment.setUserId(dto.getUserId());
        comment.setContent(dto.getContent());
        comment.setCreatedTime(dto.getCreatedTime());
        comment.setParentCommentId(dto.getParentCommentId());
        // Set additional properties as needed
        return comment;
    }
    public static CommentListResponseDTO buildCommentTree(List<Comment> comments) {
        // Step 1: Map to hold commentId -> CommentResponseDTO
        Map<Integer, CommentResponseDTO> dtoMap = new HashMap<>();
        for (Comment comment : comments) {
            dtoMap.put(comment.getCommentId(), toResponseDTO(comment));
        }

        // Step 2: Create the tree structure
        List<CommentResponseDTO> rootComments = new ArrayList<>();
        for (Comment comment : comments) {
            CommentResponseDTO currentDTO = dtoMap.get(comment.getCommentId());

            if (comment.getParentCommentId() == null) {
                // Root comment (no parent)
                rootComments.add(currentDTO);
            } else {
                // Child comment, attach to its parent
                CommentResponseDTO parentDTO = dtoMap.get(comment.getParentCommentId());
                if (parentDTO != null) {
                    // Initialize subComment list if null
                    if (parentDTO.getSubComment() == null) {
                        parentDTO.setSubComment(new ArrayList<>());
                    }
                    parentDTO.getSubComment().add(currentDTO);
                }
            }
        }
        CommentListResponseDTO responseDTO = new CommentListResponseDTO();
        responseDTO.setCommentList(rootComments);
        return responseDTO;
    }
    public static CommentRequestDTO toRequestDTO(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentRequestDTO dto = new CommentRequestDTO();
        dto.setCommentId(comment.getCommentId());
        dto.setUserId(comment.getUserId());
        dto.setContent(comment.getContent());
        dto.setCreatedTime(comment.getCreatedTime());
        dto.setParentCommentId(comment.getParentCommentId());
        return dto;
    }
    private static CommentResponseDTO toResponseDTO(Comment comment) {
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setCommentId(comment.getCommentId());
        dto.setUserId(comment.getUserId());
        dto.setContent(comment.getContent());
        dto.setCreatedTime(comment.getCreatedTime());
        dto.setSubComment(new ArrayList<>()); // Initialize as empty list
        return dto;
    }
}
