package com.hh.mirishop.user.member.repository;

import com.hh.mirishop.user.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNumberAndIsDeletedFalse(Long memberNumber);

    List<Member> findAllByIsDeletedFalse();
}
