# mirishop

미리샵은 사용자별 팔로우 뉴스피드를 제공하고, 상품을 미리 예약하는 서비스를 제공하는 API 입니다.

## 서비스 아키텍쳐
![image](https://github.com/noyes5/mirishop/assets/116651434/9bb77971-4ab5-4887-af60-aead3ed486c1)

------
## 서비스별 주요 기능

### apigateway_service

> 스택 : Java 17, Spring Boot 3.2.2, Spring Cloud(jwt 확인용), JWT(jjwt 0.11.2)

#### 주요기능 
 - 사용자 인증이 필요한 엔드포인트에서 jwt 토큰으로 사용자를 검증하는 기능을 제공합니다.

### user_service

> 스택 : Java 17, Spring Boot 3.2.2, Spring Security, JWT(jjwt 0.11.2), MySQL, Redis(이메일 인증용 1개, Auth용 1개)

#### 주요기능
- 회원가입 및 관리 : 사용자의 회원가입, 정보조회 및 수정, 비밀번호 변경 등의 기능 제공합니다.
- 이메일 인증 : 회원가입 시 이메일 인증을 요구하며, 인증코드를 통한 이메일 검증을 수행합니다.
- 프로필 이미지 업로드 : 가입시 프로필 이미지를 업로드할 수 있고, 프로필 이미지 변경 기능을 제공합니다.

### activity_service

> 스택 : Java 17, Spring Boot 3.2.2, MySQL

#### 주요기능 
- 게시글 작성,수정, 삭제, 나의 게시글 전체보기 기능을 제공합니다.
- 댓글 작성, 삭제, 대댓글 기능을 제공합니다.
- 게시글 좋아요 및 댓글 좋아요 기능을 제공합니다. 

### newsfeed_service

> 스택 : Java 17, Spring Boot 3.2.2, MySQL, MongoDB(뉴스피드 활동 저장용)

#### 주요기능
- 사용자의 팔로우(팔로잉,팔로워) 기능을 제공합니다.
- 뉴스피드 영역에서 사용자별 팔로우 유저의 활동을 볼 수 있습니다.
- 뉴스피드 영역에서 사용자별  활동에 대한 정보를 볼 수 있습니다.

### 프로젝트 설치 및 실행방법
1. github에서 파일을 내려받습니다.
2. CLI로 docker를 이용하여 실행합니다.
   - Docker 실행방법
     
     ~~~cli
     $ cd docker
     $ docker-compose up -d
     ~~~

### ERD 테이블
![image](https://github.com/noyes5/mirishop/assets/116651434/77a966cd-3a03-4f95-a078-0fa71cbbd6cc)
