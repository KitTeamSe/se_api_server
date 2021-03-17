package com.se.apiserver.domain.entity.post;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class PostTagMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postTagMappingId;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "postId", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "tagId", nullable = false)
    private Post tag;
}
