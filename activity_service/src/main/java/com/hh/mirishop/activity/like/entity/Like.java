package com.hh.mirishop.activity.like.entity;

import com.hh.mirishop.activity.like.domain.LikeType;
import com.hh.mirishop.activity.like.domain.LikeTypeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
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

    @JoinColumn(name = "member_number")
    private Long memberNumber;

    @Column(name = "item_id")
    private Long itemId; // 게시글 또는 댓글의 ID

    @Convert(converter = LikeTypeConverter.class)
    @Column(name = "like_type")
    private LikeType likeType;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    public Like(Long memberNumber, Long itemId, LikeType likeType) {
        this.memberNumber = memberNumber;
        this.itemId = itemId;
        this.likeType = likeType;
    }
}
