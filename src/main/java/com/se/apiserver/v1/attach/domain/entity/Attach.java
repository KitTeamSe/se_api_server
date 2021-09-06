package com.se.apiserver.v1.attach.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.reply.domain.entity.Reply;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Attach extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "postId")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id", referencedColumnName = "replyId")
    private Reply reply;

    @Column(length = 255)
    @Size(min = 2, max = 255)
    private String downloadUrl;

    @Column(length = 255)
    @Size(min = 2, max = 255)
    private String fileName;

    @Column(nullable = false)
    private Long fileSize;

    public Attach(@Size(min = 2, max = 255) String downloadUrl, @Size(min = 2, max = 255) String fileName, @NotNull Long fileSize) {
        this.downloadUrl = downloadUrl;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public Attach(@Size(min = 2, max = 255) String downloadUrl, @Size(min = 2, max = 255) String fileName, @NotNull Long fileSize, Post post) {
        this(downloadUrl, fileName, fileSize);
        updatePost(post);
    }

    public Attach(@Size(min = 2, max = 255) String downloadUrl, @Size(min = 2, max = 255) String fileName, @NotNull Long fileSize, Reply reply) {
        this(downloadUrl, fileName, fileSize);
        updateReply(reply);
    }

    public void updatePost(Post post) {
        this.post = post;
        if(!post.getAttaches().contains(this))
            post.addAttache(this);
    }

    public void updateReply(Reply reply) {
        this.reply = reply;
        if(!reply.getAttaches().contains(this))
            reply.addAttach(this);
    }

    public void remove() {
        if(post != null)
            deleteFromPost();
        if(reply != null)
            deleteFromReply();
    }

    private void deleteFromReply() {
        if(this.reply.getAttaches().contains(this))
            this.reply.getAttaches().remove(this);
    }

    private void deleteFromPost() {
        if(this.post.getAttaches().contains(this))
            this.post.getAttaches().remove(this);
    }

    public String getSaveName(){
        return downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1);
    }

    public void setReply(Reply reply) {
        this.reply = reply;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
