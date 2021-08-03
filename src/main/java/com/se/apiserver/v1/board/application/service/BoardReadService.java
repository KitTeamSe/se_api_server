package com.se.apiserver.v1.board.application.service;

import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.application.error.BoardErrorCode;
import com.se.apiserver.v1.board.application.dto.BoardReadDto;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardReadService {

    private final BoardJpaRepository boardJpaRepository;

    public BoardReadDto.ReadResponse read(Long id) {
        Board board = boardJpaRepository.findById(id).orElseThrow(() -> new BusinessException(BoardErrorCode.NO_SUCH_BOARD));
        return BoardReadDto.ReadResponse.fromEntity(board);
    }

    public BoardReadDto.ReadResponse readByNameEng(String nameEng) {
        Board board = boardJpaRepository.findByNameEng(nameEng).orElseThrow(() -> new BusinessException(BoardErrorCode.NO_SUCH_BOARD));
        return BoardReadDto.ReadResponse.fromEntity(board);
    }

    public List<BoardReadDto.ReadResponse> readAll() {
        List<Board> all = boardJpaRepository.findAll();
        List<BoardReadDto.ReadResponse> responses = all.stream()
                .map(b -> BoardReadDto.ReadResponse.fromEntity(b))
                .collect(Collectors.toList());
        return responses;
    }
}
