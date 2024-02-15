package com.hh.mirishop.user.member;

import static org.assertj.core.api.Assertions.assertThat;

import com.hh.mirishop.user.member.entity.Member;
import com.hh.mirishop.user.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DisplayName("유저 DB 저장 테스트")
class MemberRepositoryTest {

    @Autowired
    MemberRepository userRepository;

    private Member createUser() {
        return Member.builder()
                .email("noyes5@naver.com")
                .password("password")
                .nickname("임상현")
                .profileImage("profile.jpg")
                .bio("자기소개입니다.")
                .build();
    }

    @Test
    @DisplayName("정상 가입 테스트")
    @Transactional
    public void createUserTest() throws Exception {
        Member user = createUser();

        Member findUser = userRepository.save(user);

        assertThat(findUser).isEqualTo(user);
    }
}