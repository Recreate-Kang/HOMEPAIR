package com.home.ssafyhome.board.service;

import com.home.ssafyhome.board.dto.BoardRequestDTO;
import com.home.ssafyhome.board.dto.BoardResponseDTO;
import com.home.ssafyhome.board.dto.SubjectResponseDTO;
import org.springframework.data.domain.Page;

public interface BoardService {
    Integer writeBoard(BoardRequestDTO boardDTO);

    BoardResponseDTO findArticle(Long num);

    Page<SubjectResponseDTO> getBoardLists(int page, int size);

    Integer updateBoard(BoardRequestDTO boardDTO);
}
