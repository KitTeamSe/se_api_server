package com.se.apiserver.v1.board.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.board.domain.entity.Board;
import com.se.apiserver.v1.board.application.error.BoardErrorCode;
import com.se.apiserver.v1.board.infra.repository.BoardJpaRepository;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardDeleteService {

    @Value("${spring.deletedBoard}")
    private String DELETEDBOARD;

    private final BoardJpaRepository boardJpaRepository;
    private final AccountContextService accountContextService;
    private final PostJpaRepository postJpaRepository;

    @Transactional
    public boolean delete(Long id){
        Set<String> authoritySet = accountContextService.getContextAuthorities();

        Board board = boardJpaRepository.findById(id).orElseThrow(() -> new BusinessException(BoardErrorCode.NO_SUCH_BOARD));
        Optional<Board> deletedBoard = boardJpaRepository.findByNameEng("deletedBoard");

        List<Post> postList = postJpaRepository.findAllByBoard(board);
        for(Post post: postList){
            post.delete(authoritySet, deletedBoard.get());
        }
        postJpaRepository.saveAll(postList);
        boardJpaRepository.delete(board);

        return true;
    }
}
