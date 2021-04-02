package com.se.apiserver.v1.post.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostContent {
    @Column(length = 50, nullable = false)
    @Size(min = 3, max = 50)
    private String title;

    @Column(length = 2000, nullable = false)
    @Size(min = 5, max = 2000)
    private String text;

    public PostContent(@Size(min = 3, max = 50) String title, @Size(min = 5, max = 2000) String text) {
        this.title = title;
        this.text = text;
    }

    public void updateTitle(@Size(min = 3, max = 50) String title){
        this.title = title;
    }

    public void updateText(@Size(min = 5, max = 2000) String text){
        this.text = text;
    }
}
