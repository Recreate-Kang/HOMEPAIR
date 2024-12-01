package com.home.ssafyhome.board.util;

import com.home.ssafyhome.board.dto.BoardRequestDTO;
import com.home.ssafyhome.board.dto.BoardResponseDTO;
import com.home.ssafyhome.board.model.Board;

public class BoardUtil {
    public static Board toModel(BoardRequestDTO dto) {
        Board board = new Board();
        board.setArticleNo(dto.getArticleNo());
        board.setUserId(dto.getUserId());
        board.setSubject(dto.getSubject());
        board.setContent(dto.getContent());
        // hit과 registerTime은 엔티티의 기본값 및 @PrePersist로 처리
        return board;
    }

    public static BoardResponseDTO toResponseDto(Board board) {
        BoardResponseDTO dto = new BoardResponseDTO();
        dto.setArticleNo(board.getArticleNo());
        dto.setUserId(board.getUserId());
        dto.setSubject(board.getSubject());
        dto.setContent(board.getContent());
        dto.setHit(board.getHit());
        dto.setRegisterTime(board.getRegisterTime());
        return dto;
    }

}

