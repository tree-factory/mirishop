package com.hh.mirishop.follow.entity;

import com.hh.mirishop.follow.domain.FollowId;
import com.hh.mirishop.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "Follows")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Follow {

    @EmbeddedId
    private FollowId followId;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "followerNumber", insertable = false, updatable = false)
    private Member follower;

    @ManyToOne
    @JoinColumn(name = "followingNumber", insertable = false, updatable = false)
    private Member following;

    public Follow(Member follower, Member following) {
        this.followId = new FollowId(follower.getNumber(), following.getNumber());
        this.follower = follower;
        this.following = following;
    }
}
