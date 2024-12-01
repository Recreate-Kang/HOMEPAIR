package com.home.ssafyhome.board.util;

import com.home.ssafyhome.board.dto.SubjectResponseDTO;
import com.home.ssafyhome.board.model.Board;

public class SubjectResponseMapper {

    // 단일 엔티티를 SubjectResponseDTO로 변환
    public static SubjectResponseDTO toSubjectDto(Board board) {
        SubjectResponseDTO dto = new SubjectResponseDTO();
        dto.setArticleNo(board.getArticleNo());
        dto.setSubject(board.getSubject());
        dto.setUserId(board.getUserId());
        dto.setRegisterTime(board.getRegisterTime());
        dto.setHit((board.getHit()));
        return dto;
    }
}
