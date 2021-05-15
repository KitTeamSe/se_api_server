package com.se.apiserver.v1.board.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import com.se.apiserver.v1.menu.domain.entity.MenuType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

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

    public Board(@Size(min = 2, max = 20) String nameEng, @Size(min = 2, max = 20) String nameKor) {
        this.nameEng = nameEng;
        this.nameKor = nameKor;
        this.menu = new Menu(nameEng, nameEng, nameKor, 1, nameKor, MenuType.BOARD);
    }

    public void updateNameEng(String nameEng) {
        this.nameEng = nameEng;
        this.menu.updateNameEng(nameEng);
    }

    public void updateNameKor(String nameKor) {
        this.nameKor = nameKor;
        this.menu.updateNameKor(nameKor);
    }

    public void validateAccessAuthority(Set<String> authorities) {
        menu.validateAccessAuthority(authorities);
    }
}
