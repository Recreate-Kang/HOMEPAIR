package com.home.ssafyhome.board.service;

import com.home.ssafyhome.board.dto.BoardRequestDTO;
import com.home.ssafyhome.board.dto.BoardResponseDTO;
import com.home.ssafyhome.board.dto.SubjectResponseDTO;
import com.home.ssafyhome.board.mapper.BoardMapper;
import com.home.ssafyhome.board.model.Board;
import com.home.ssafyhome.board.util.BoardUtil;
import com.home.ssafyhome.board.util.SubjectResponseMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService {

    BoardMapper boardMapper;

    BoardServiceImpl(BoardMapper boardmapper) {
        this.boardMapper = boardmapper;
    }

    @Override
    @Transactional
    public Integer writeBoard(BoardRequestDTO boardDTO) {
        Integer isDone = boardMapper.save(BoardUtil.toModel(boardDTO));
        return isDone;
    }

    @Override
    public Integer updateBoard(BoardRequestDTO boardDTO) {
        Integer isDone = boardMapper.updateBoard(BoardUtil.toModel(boardDTO));
        return isDone;
    }

    @Override
    public BoardResponseDTO findArticle(Long num) {
        boardMapper.incrementHit(num);
        Optional<Board> board = boardMapper.findById(num);
        return board.map(BoardUtil::toResponseDto).orElse(null);
    }

    @Override
    public Page<SubjectResponseDTO> getBoardLists(int page, int size) {
        // 전체 데이터 개수
        int totalElements = boardMapper.countAll();

        // 페이징 계산
        int offset = page * size;

        // 페이징된 데이터 조회
        List<Board> boards = boardMapper.findAllPaged(size, offset);

        // 데이터를 SubjectResponseDTO로 변환
        List<SubjectResponseDTO> subjectDtos = boards.stream().map(SubjectResponseMapper::toSubjectDto).toList();

        // PageImpl 객체 생성
        return new PageImpl<>(subjectDtos, PageRequest.of(page, size), totalElements);
    }

}
