package com.home.ssafyhome.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommentListResponseDTO {
    List<CommentResponseDTO> commentList;
}
