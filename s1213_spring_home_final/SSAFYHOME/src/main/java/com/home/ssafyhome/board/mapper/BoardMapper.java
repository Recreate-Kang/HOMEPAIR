package com.home.ssafyhome.board.mapper;

import com.home.ssafyhome.board.model.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardMapper {

    Integer save(Board board);
    Integer updateBoard(Board board);
    Integer incrementHit(@Param("num") Long num);
    Optional<Board> findById(@Param("num") Long num);

    List<Board> findAllPaged(@Param("size") int size, @Param("offset") int offset);

    int countAll();
}
