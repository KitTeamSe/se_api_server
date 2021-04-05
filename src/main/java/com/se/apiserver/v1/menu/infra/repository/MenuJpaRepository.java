package com.se.apiserver.v1.menu.infra.repository;

import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MenuJpaRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByNameEng(String nameEng);
    Optional<Menu> findByNameKor(String nameKor);
    Optional<Menu> findByUrl(String url);

    @Query("select m from Menu m where m.parent is null")
    List<Menu> findAllRootMenu();
}
