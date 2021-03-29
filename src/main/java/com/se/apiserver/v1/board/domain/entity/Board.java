package com.se.apiserver.v1.board.domain.entity;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = "BOARD")
public class Board extends AccountGenerateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(length = 20, nullable = false)
    @Size(min = 2, max = 20)
    private String nameEng;

    @Column(length = 20, nullable = false)
    @Size(min = 2, max = 20)
    private String nameKor;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Builder
    public Board(Long boardId, @Size(min = 2, max = 20) String nameEng, @Size(min = 2, max = 20) String nameKor, Menu menu) {
        this.boardId = boardId;
        this.nameEng = nameEng;
        this.nameKor = nameKor;
        this.menu = menu;
    }

    public void updateNameEng(String nameEng) {
        this.nameEng = nameEng;
    }

    public void updateNameKor(String nameKor) {
        this.nameKor = nameKor;
    }
}
