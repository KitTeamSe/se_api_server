package com.se.apiserver.v1.post.infra.repository;

import com.querydsl.jpa.JPQLQuery;
import com.se.apiserver.v1.account.domain.entity.QAccount;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.application.dto.PostReadDto.PostSearchRequest;
import com.se.apiserver.v1.post.application.error.PostSearchErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.post.domain.entity.PostIsSecret;
import com.se.apiserver.v1.post.domain.entity.QPost;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class PostQueryRepositoryImpl extends QuerydslRepositorySupport implements PostQueryRepository {

  public PostQueryRepositoryImpl(){
    super(Post.class);
  }

  @Override
  public Page<Post> search(PostSearchRequest postSearchRequest) {
    if(postSearchRequest.getKeyword() == null || postSearchRequest.getKeyword().length() < 1)
      throw new BusinessException(PostSearchErrorCode.INVALID_SEARCH_KEYWORD);

    QPost post = QPost.post;
    QAccount account = QAccount.account;

    JPQLQuery query = from(post);
    query.where(post.board.nameEng.eq(postSearchRequest.getBoardNameEng()));

    String keyword = postSearchRequest.getKeyword();

    switch (postSearchRequest.getPostSearchType()){
      case TITLE_TEXT:
        query.where(
            post.postContent.title.contains(keyword).or(post.isSecret.eq(PostIsSecret.NORMAL).and(post.postContent.text.contains(keyword))));
        break;
      case TITLE:
        query.where(post.postContent.title.contains(keyword));
        break;
      case TEXT:
        query.where(post.isSecret.eq(PostIsSecret.NORMAL).and(post.postContent.text.contains(keyword)));
        break;
      case REPLY:
        query.where(post.isSecret.eq(PostIsSecret.NORMAL).and(post.replies.any().text.contains(keyword)));
        break;
      case NICKNAME:
        query.leftJoin(post.account, account);
        query.where(post.account.nickname.contains(keyword).or(post.anonymous.anonymousNickname.contains(keyword)));
        break;
      case USERID:
        query.leftJoin(post.account, account);
        query.where(post.account.idString.contains(keyword));
        break;
      case TAG:
        query.where(post.tags.any().text.contains(keyword));
        break;
      default:
        throw new BusinessException(PostSearchErrorCode.NO_SUCH_SEARCH_TYPE);
    }


    Pageable pageable = postSearchRequest.getPageRequest().of();
    List<Post> posts = getQuerydsl().applyPagination(pageable, query).fetch();
    long totalCount = query.fetchCount();
    return new PageImpl<>(posts, pageable, totalCount);
  }
}
