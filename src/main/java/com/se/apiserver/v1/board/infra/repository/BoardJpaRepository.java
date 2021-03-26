package com.se.apiserver.v1.board.infra.repository;

import com.se.apiserver.v1.board.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardJpaRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByNameEng(String nameEng);
    Optional<Board> findByNameKor(String nameKor);
}
