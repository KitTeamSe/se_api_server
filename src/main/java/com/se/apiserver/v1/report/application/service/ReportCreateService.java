package com.se.apiserver.v1.report.application.service;

import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.application.error.PostErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.infra.repository.PostJpaRepository;
import com.se.apiserver.v1.reply.application.error.ReplyErrorCode;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.infra.repository.ReplyJpaRepository;
import com.se.apiserver.v1.report.application.dto.ReportCreateDto;
import com.se.apiserver.v1.report.application.error.ReportErrorCode;
import com.se.apiserver.v1.report.domain.entity.PostReport;
import com.se.apiserver.v1.report.domain.entity.ReplyReport;
import com.se.apiserver.v1.report.domain.entity.Report;
import com.se.apiserver.v1.report.domain.entity.ReportType;
import com.se.apiserver.v1.report.infra.repository.ReportJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportCreateService {

  private final AccountContextService accountContextService;
  private final ReportJpaRepository reportJpaRepository;
  private final PostJpaRepository postJpaRepository;
  private final ReplyJpaRepository replyJpaRepository;

  @Transactional
  public Long create(ReportCreateDto.Request request){
    if(request.getReportType() == null)
      throw new BusinessException(ReportErrorCode.INVALID_INPUT);

    if(!accountContextService.isSignIn())
      throw new BusinessException(ReportErrorCode.ANONYMOUS_CANT_REPORT);

    Account reporter = accountContextService.getContextAccount();

    Report report;
    switch(request.getReportType()){
      case POST:
        Post post = postJpaRepository.findById(request.getTargetId())
            .orElseThrow(() -> new BusinessException(PostErrorCode.NO_SUCH_POST));
        report = new PostReport(post, request.getDescription(), reporter);
        break;
      case REPLY:
        Reply reply = replyJpaRepository.findById(request.getTargetId())
            .orElseThrow(() -> new BusinessException(ReplyErrorCode.NO_SUCH_REPLY));
        report = new ReplyReport(reply, request.getDescription(), reporter);
        break;
      default:
        throw new BusinessException(ReportErrorCode.NO_SUCH_REPORT_TYPE);
    }

    return reportJpaRepository.save(report).getReportId();
  }
}
