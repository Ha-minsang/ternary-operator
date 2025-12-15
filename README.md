# TaskFlow 백엔드 아웃소싱 프로젝트

## 📌 프로젝트 개요

**TaskFlow**는 기업용 태스크 관리 시스템으로, 팀 단위 협업과 작업 관리를 효율적으로 지원하는 서비스입니다.

본 프로젝트는 이미 완성된 프론트엔드와 연동되는 **REST API 기반 백엔드 서버를 구축**하는 것을 목표로 하는 아웃소싱 프로젝트입니다.

* 프로젝트 성격: **외주(Outsourcing) 백엔드 개발**
* 개발 기간: **1주일**
* 개발 범위: **CRUD + 인증·인가 + 통계·로그·검색 기능 구현**

---

## 🎯 프로젝트 목표

* 실무에서 자주 사용되는 협업 툴(Task 관리 시스템)의 백엔드 구조 이해
* 프론트엔드와 실제로 연동되는 API 설계 및 구현 경험
* JWT 기반 인증·인가 처리 경험
* JPA·Hibernate를 활용한 도메인 중심 설계
* 대시보드 통계, 활동 로그, 통합 검색 등 실무형 기능 구현

---

## 팀원

| 이름 | 역할 |
| ------ | -------------------------- |
| 이상무  | 댓글 기능 CRUD, 대시 보드(주간 작업 추세), 댓글 Test Code |
| 신호준  | 영상 제작, Code Refactoring, AOP |
| 오은지  | 팀 관리 CRUD, 팀 Test Code, 발표 자료 |
| 최승희  | 작업 기능 CRUD, 대시 보드(대시 보드 통계), 작업 Test Code, 발표 자료 |
| 하민상  | 대시 보드(내 작업 요약), 통합 검색, 유저 기능 CRUD, JWT 기반 인증·인가, 발표 |



---

## 🛠️ 기술 스택

### Backend

* Java 17
* Spring Boot 3.5.8
* Spring Security (JWT 인증·인가)
* Spring Data JPA (Hibernate)
* AOP (로깅 처리)

### Database

* MySQL

### DevOps (이번 주차 제한 사항)

* Docker (설치 및 실행 테스트까지만 활용)

---

## 🔐 인증 및 권한

* JWT 기반 인증 방식 사용
* 로그인 성공 시 Access Token 발급
* 사용자 권한에 따른 API 접근 제어

---

## 🧩 주요 기능

### 1. 사용자 관리 (Users)

* 회원가입
* 로그인
* 사용자 정보 조회·수정·삭제
* 추가 가능한 사용자 조회

### 2. 작업 관리 (Tasks)

* 작업 생성·조회·수정·삭제
* 작업 상태 변경 (TODO → IN_PROGRESS → DONE)
* 작업 목록 페이징 및 필터링

### 3. 팀 & 멤버 관리 (Teams)

* 팀 생성·조회·수정·삭제
* 팀 멤버 추가 및 제거
* 팀별 멤버 조회

### 4. 댓글 기능 (Comments)

* 작업별 댓글 조회 (페이징)
* 댓글 생성·수정·삭제
* 본인이 작성한 댓글만 수정 및 삭제 가능

### 5. 대시보드 (Dashboard)

* 전체 통계 데이터 제공
* 내 작업 요약
* 주간 작업 추세 데이터 제공

### 6. 활동 로그 (Activities)

* 전체 활동 로그 조회 (필터링 가능)
* 내 활동 로그 조회

### 7. 통합 검색 (Search)

* 작업, 댓글, 사용자 등 통합 검색 기능 제공

---

## 📡 API 명세 요약

### 인증 (Authentication)

| Method | Endpoint                   | 설명      | 인증 |
| ------ | -------------------------- | ------- | -- |
| POST   | /api/auth/login            | 로그인     | ❌  |
| POST   | /api/users/verify-password | 비밀번호 확인 | ✅  |

### 사용자 (Users)

| Method | Endpoint             | 설명         | 인증 |
| ------ | -------------------- | ---------- | -- |
| POST   | /api/users           | 회원가입       | ❌  |
| GET    | /api/users/{id}      | 사용자 조회     | ✅  |
| GET    | /api/users           | 사용자 목록 조회  | ✅  |
| PUT    | /api/users/{id}      | 사용자 수정     | ✅  |
| DELETE | /api/users/{id}      | 회원 탈퇴      | ✅  |
| GET    | /api/users/available | 추가 가능한 사용자 | ✅  |

### 작업 (Tasks)

| Method | Endpoint               | 설명       | 인증 |
| ------ | ---------------------- | -------- | -- |
| GET    | /api/tasks             | 작업 목록 조회 | ✅  |
| GET    | /api/tasks/{id}        | 작업 상세 조회 | ✅  |
| POST   | /api/tasks             | 작업 생성    | ✅  |
| PUT    | /api/tasks/{id}        | 작업 수정    | ✅  |
| DELETE | /api/tasks/{id}        | 작업 삭제    | ✅  |
| PATCH  | /api/tasks/{id}/status | 작업 상태 변경 | ✅  |

### 팀 (Teams)

| Method | Endpoint                             | 설명      | 인증 |
| ------ | ------------------------------------ | ------- | -- |
| GET    | /api/teams                           | 팀 목록 조회 | ✅  |
| GET    | /api/teams/{id}                      | 팀 상세 조회 | ✅  |
| GET    | /api/teams/{teamId}/members          | 팀 멤버 조회 | ✅  |
| POST   | /api/teams                           | 팀 생성    | ✅  |
| PUT    | /api/teams/{id}                      | 팀 수정    | ✅  |
| DELETE | /api/teams/{id}                      | 팀 삭제    | ✅  |
| POST   | /api/teams/{teamId}/members          | 팀 멤버 추가 | ✅  |
| DELETE | /api/teams/{teamId}/members/{userId} | 팀 멤버 제거 | ✅  |

### 댓글 (Comments)

| Method | Endpoint                                 | 설명    | 인증 |
| ------ | ---------------------------------------- | ----- | -- |
| GET    | /api/tasks/{taskId}/comments             | 댓글 조회 | ✅  |
| POST   | /api/tasks/{taskId}/comments             | 댓글 생성 | ✅  |
| PUT    | /api/tasks/{taskId}/comments/{commentId} | 댓글 수정 | ✅  |
| DELETE | /api/tasks/{taskId}/comments/{commentId} | 댓글 삭제 | ✅  |

### 대시보드 (Dashboard)

| Method | Endpoint                    | 설명       | 인증 |
| ------ | --------------------------- | -------- | -- |
| GET    | /api/dashboard/stats        | 대시보드 통계  | ✅  |
| GET    | /api/dashboard/tasks        | 내 작업 요약  | ✅  |
| GET    | /api/dashboard/weekly-trend | 주간 작업 추세 | ✅  |

### 활동 로그 (Activities)

| Method | Endpoint           | 설명       | 인증 |
| ------ | ------------------ | -------- | -- |
| GET    | /api/activities    | 전체 활동 로그 | ✅  |
| GET    | /api/activities/me | 내 활동 로그  | ✅  |

### 검색 (Search)

| Method | Endpoint    | 설명    | 인증 |
| ------ | ----------- | ----- | -- |
| GET    | /api/search | 통합 검색 | ✅  |

---

## 🚀 실행 방법

1. MySQL 데이터베이스 생성
2. `application.yml` 설정
3. Spring Boot 애플리케이션 실행
4. 프론트엔드 서버와 API 연동 확인

---

## 📎 참고 자료

* 프론트엔드 사이트: [https://taskflow-frontend-phi.vercel.app/login](https://taskflow-frontend-phi.vercel.app/login)
* 요구사항 정의서 및 API 명세서: 제공된 Notion 문서 참고

---

## ✅ 정리

본 프로젝트는 **실제 실무 환경을 가정한 백엔드 아웃소싱 프로젝트**로,
프론트엔드와의 연동, 인증·인가, 협업 기능 구현까지 포함한 종합적인 백엔드 개발 경험을 목표로 합니다.

단순 CRUD를 넘어, **실제 서비스 수준의 백엔드 구조와 흐름을 이해하는 것**이 핵심입니다.
