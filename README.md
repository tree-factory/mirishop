# mirishop

미리샵은 유저의 활동피드 기능을 제공하고, 예약 상품을 등록,구매하는 서비스를 제공하는 API 입니다.

## 서비스 아키텍쳐
![image](https://github.com/mirishop/mirishop/assets/116651434/9fb938d8-8333-49a9-a1e2-f081bcc81cb2)

## API 명세서

[POSTMAN API(링크)](https://documenter.getpostman.com/view/28990636/2sA2rGuJnr)


## 서비스별 주요 기능

### apigateway_service

> 스택 : Java 17, Spring Boot 3.2.2, Spring Cloud(jwt 확인용), JWT(jjwt 0.11.2)

#### 주요기능 
 - jwt 기반 사용자 검증 : 사용자 인증이 필요한 엔드포인트에서 jwt 토큰을 기반으로 사용자를 검증하는 기능을 제공합니다.
 - API 게이트웨이 라우팅 : 사용자 인증 후 또는 인증 없이 접근 가능한 서비스에 대해 연결해주는 기능을 제공합니다.

### user_service

> 스택 : Java 17, Spring Boot 3.2.2, Spring Security, Spring JPA, JWT(jjwt 0.11.2), MySQL, Redis(이메일 인증용 1, jwt token 저장용 1)

#### 주요기능
- 회원가입 및 관리 : 사용자의 회원가입, 정보조회 및 수정, 비밀번호 변경 등의 기능 제공합니다.
- 이메일 인증 : 회원가입 시 이메일 인증을 요구하며, 인증코드를 통한 이메일 검증을 수행합니다.
- 프로필 이미지 업로드 : 가입시 프로필 이미지를 업로드할 수 있고, 프로필 이미지 변경 기능을 제공합니다.

### activity_service

> 스택 : Java 17, Spring Boot 3.2.2, MySQL

#### 주요기능 
- 게시글 기능 : 게시글 작성, 수정, 삭제, 나의 게시글 전체보기 기능을 제공합니다.
- 댓글 기능 : 게시글 작성, 삭제, 대댓글 기능을 제공합니다.
- 좋아요 기능 : 게시글 좋아요 및 댓글 좋아요 기능을 제공합니다. 

### newsfeed_service

> 스택 : Java 17, Spring Boot 3.2.2, Spring JPA, Spring MongoDB, MySQL, MongoDB(뉴스피드 활동 저장용)

#### 주요기능
- 팔로우 기능 : 사용자끼리 팔로우 기능을 제공합니다.
- 뉴스피드 기능
  - 내가 팔로우 한 유저의 활동을 볼 수 있습니다.
  - 나의 활동에 대한 정보를 볼 수 있습니다.

### productmanagement_service

> 스택 : Java 17, Spring Boot 3.2.2, Spring JPA, MySQL, Redisson(재고처리 분산락용)

#### 주요기능
- 상품 기능 : 상품을 일반상품과 예약상품으로 나누어 상품 등록, 수정, 삭제, 조회할 수 있습니다.
- 재고 기능 : 상품의 재고를 별도로 분리하여 재고를 관리할 수 있습니다.

### orderpayment_service

> 스택 : Java 17, Spring Boot 3.2.2, Spring JPA, MySQL

#### 주요기능
- 주문 기능 : 주문 생성, 취소, 조회 기능을 제공합니다.
- 결제 기능 : 결제 정보를 받아 결제하는 기능을 제공합니다.

### ERD 테이블
![image](https://github.com/mirishop/mirishop/assets/116651434/af284932-98b5-40dd-8cbb-03fc844cd40a)

## 프로젝트 설치 및 실행방법
1. github에서 파일을 내려받습니다.
2. CLI로 docker를 이용하여 실행합니다.
   - Docker 실행방법
     
     ~~~cli
     $ cd docker
     $ docker-compose up -d
     ~~~
