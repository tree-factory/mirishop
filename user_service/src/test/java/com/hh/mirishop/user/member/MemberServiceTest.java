package com.hh.mirishop.user.member;

import static org.assertj.core.api.Assertions.assertThat;

import com.hh.mirishop.user.member.dto.MemberJoinResponse;
import com.hh.mirishop.user.member.entity.Member;
import com.hh.mirishop.user.member.dto.MemberRequest;
import com.hh.mirishop.user.member.repository.MemberRepository;
import com.hh.mirishop.user.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class MemberServiceTest {

    private MemberService memberService;
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
//        memberRepository = new testRegisterUser(); // 사용자 정의 스텁(repository 대용)
//        memberService = new MemberService(memberRepository);
    }


    @Test
    @DisplayName("정상 가입 테스트")
    @Transactional
    public void testRegisterUser() throws Exception {
        MemberRequest memberRequest = MemberRequest.builder()
                .name("임상현")
                .email("noyes5@naver.com")
                .password("12345678")
                .profileImage("profile.jpg")
                .bio("hello")
                .build();

        MemberJoinResponse register = memberService.register(memberRequest);

        Member savedUser = memberRepository.findById(register.getNumber()).orElse(null);
        assertThat(savedUser).isEqualTo(memberRequest);
    }
}
