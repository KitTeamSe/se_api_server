package com.se.apiserver.v1.post.infra.repository;

import com.querydsl.jpa.JPQLQuery;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.post.application.dto.PostReadDto.SearchRequest;
import com.se.apiserver.v1.post.application.error.PostSearchErrorCode;
import com.se.apiserver.v1.post.domain.entity.Post;
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
  public Page<Post> search(SearchRequest searchRequest) {
    if(searchRequest.getKeyword() == null || searchRequest.getKeyword().length() < 1)
      throw new BusinessException(PostSearchErrorCode.INVALID_SEARCH_KEYWORD);

    QPost post = QPost.post;

    JPQLQuery query = from(post);
    query.where(post.board.boardId.eq(searchRequest.getBoardId()));

    String keyword = searchRequest.getKeyword();

    switch (searchRequest.getPostSearchType()){
      case TITLE_TEXT:
        query.where(post.postContent.title.contains(keyword).or(post.postContent.text.contains(keyword)));
        break;
      case TITLE:
        query.where(post.postContent.title.contains(keyword));
        break;
      case TEXT:
        query.where(post.postContent.text.contains(keyword));
        break;
      case REPLY:
        query.where(post.replies.any().text.contains(keyword));
        break;
      case NICKNAME:
        query.where(post.account.nickname.contains(keyword).or(post.anonymous.anonymousNickname.contains(keyword)));
        break;
      case USERID:
        query.where(post.anonymous.anonymousNickname.contains(keyword).or(post.account.idString.contains(keyword)));
      case TAG:
        query.where(post.tags.any().tag.text.contains(keyword));
        break;
      default:
        throw new BusinessException(PostSearchErrorCode.NO_SUCH_SEARCH_TYPE);
    }


    Pageable pageable = searchRequest.getPageRequest().of();
    List<Post> posts = getQuerydsl().applyPagination(pageable, query).fetch();
    long totalCount = query.fetchCount();
    return new PageImpl<>(posts, pageable, totalCount);
  }
}
