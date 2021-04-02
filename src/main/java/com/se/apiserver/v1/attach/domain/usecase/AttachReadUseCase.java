package com.se.apiserver.v1.attach.domain.usecase;

import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.domain.error.AttachErrorCode;
import com.se.apiserver.v1.attach.infra.dto.AttachReadDto;
import com.se.apiserver.v1.attach.infra.dto.AttachReadDto.Response;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.post.infra.repository.AttachJpaRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachReadUseCase {

  private final AttachJpaRepository attachJpaRepository;

  public AttachReadDto.Response read(Long id) {
    Attach attach = attachJpaRepository.findById(id)
        .orElseThrow(() -> new BusinessException(AttachErrorCode.NO_SUCH_ATTACH));
    return Response.fromEntity(attach);
  }


  public List<Response> readAllByPostId(Long postId) {
    List<Attach> attach = attachJpaRepository.findAllByPostId(postId);
    return attach.stream()
        .map(a -> Response.fromEntity(a))
        .collect(Collectors.toList());
  }

  public List<Response> readAllByReplyId(Long replyId) {
    List<Attach> attach = attachJpaRepository.findAllByPostId(replyId);
    return attach.stream()
        .map(a -> Response.fromEntity(a))
        .collect(Collectors.toList());
  }

  public PageImpl readAllByPage(Pageable pageable){
    Page<Attach> all = attachJpaRepository.findAll(pageable);
    List<AttachReadDto.Response> responseList = all.stream()
        .map(a -> AttachReadDto.Response.fromEntity(a))
        .collect(Collectors.toList());
    return new PageImpl(responseList, all.getPageable(), all.getTotalElements());
  }

}
