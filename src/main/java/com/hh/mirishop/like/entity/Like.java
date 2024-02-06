package com.hh.mirishop.like.entity;

import com.hh.mirishop.like.domain.LikeType;
import com.hh.mirishop.like.domain.LikeTypeConverter;
import com.hh.mirishop.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "Likes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "member_number", referencedColumnName = "number")
    private Member member;

    @Column(name = "item_id")
    private Long itemId; // 게시글 또는 댓글의 ID

    @Convert(converter = LikeTypeConverter.class)
    @Column(name = "like_type")
    private LikeType likeType;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Like(Member member, Long itemId, LikeType likeType) {
        this.member = member;
        this.itemId = itemId;
        this.likeType = likeType;
    }
}
