package com.home.ssafyhome.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubjectResponseListDTO {
    private List<SubjectResponseDTO> articles; // 게시글 리스트
    private String lastPage;
}
