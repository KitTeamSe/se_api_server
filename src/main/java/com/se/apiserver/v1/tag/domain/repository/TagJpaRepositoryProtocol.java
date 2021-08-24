package com.se.apiserver.v1.tag.domain.repository;

import com.se.apiserver.v1.tag.domain.entity.Tag;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TagJpaRepositoryProtocol {

  Optional<Tag> findByText(String text);
}
