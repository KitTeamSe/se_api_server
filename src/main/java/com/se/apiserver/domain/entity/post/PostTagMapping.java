package com.se.apiserver.domain.entity.post;

import com.se.apiserver.domain.entity.BaseEntity;
import com.se.apiserver.domain.entity.tag.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dom4j.tree.BaseElement;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class PostTagMapping extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postTagMappingId;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "postId", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "tagId", nullable = false)
    private Tag tag;
}
