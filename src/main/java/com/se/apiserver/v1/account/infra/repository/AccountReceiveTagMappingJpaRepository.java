package com.se.apiserver.v1.account.infra.repository;

import com.se.apiserver.v1.account.domain.entity.AccountReceiveTagMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AccountReceiveTagMappingJpaRepository extends JpaRepository<AccountReceiveTagMapping, Long> {

    List<AccountReceiveTagMapping> findAccountReceiveTagMappingsByTag_TagIdIn(List<Long> tag_id);

}
