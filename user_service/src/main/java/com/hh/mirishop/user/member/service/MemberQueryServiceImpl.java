package com.hh.mirishop.user.member.service;

import com.hh.mirishop.user.member.dto.MemberDetailResponse;
import com.hh.mirishop.user.member.dto.MemberListResponse;
import com.hh.mirishop.user.member.entity.Member;
import com.hh.mirishop.user.member.repository.MemberRepository;
import com.hh.mirishop.user.common.exception.ErrorCode;
import com.hh.mirishop.user.common.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;

    @Override
    public boolean existsMemberByNumber(Long memberNumber) {
        return memberRepository.findByNumberAndIsDeletedFalse(memberNumber).isPresent();
    }

    /*
    특정 회원 상세 정보 조회
    */
    @Override
    @Transactional(readOnly = true)
    public MemberDetailResponse getMemberDetail(Long memberNumber) {
        return memberRepository.findByNumberAndIsDeletedFalse(memberNumber)
                .map(MemberDetailResponse::new)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
    }

    /*
    회원 목록 조회
    */
    @Override
    @Transactional(readOnly = true)
    public List<MemberListResponse> listMembers() {
        List<Member> members = memberRepository.findAllByIsDeletedFalse();
        return members.stream()
                .map(MemberListResponse::new) // Member 엔티티를 리스트 DTO로 변환
                .toList();
    }
}
