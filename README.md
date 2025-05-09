# 🎒 조선대학교 분실물 관리 시스템

> **로스트 조선**: 조선대학교 내 분실물 접수, 조회, 실시간 소통을 지원하는 Android 모바일 애플리케이션

---

## 📖 프로젝트 개요

- 조선대학교 캠퍼스 내 분실물 문제를 효율적으로 해결하기 위해 개발된 모바일 앱입니다.
- 학생 및 교직원이 분실물 등록, 검색, 회수 과정을 간편하고 빠르게 수행할 수 있도록 지원합니다.
- 기존 커뮤니티(에브리타임 등) 기반 분실물 처리 방식의 한계를 극복하고, 분실물 회수율을 높이는 것을 목표로 합니다.

---

## 🧩 기존 문제점 및 개선 방향

| 기존 문제점 | 개선 방안 |
|:------------|:----------|
| 커뮤니티 글 속에서 분실물 찾기 어려움 | 분실물 전용 플랫폼 제공 |
| 분실물 회수까지 시간이 오래 걸림 | 실시간 알림 및 채팅 기능 제공 |
| 별도의 분실물 통합 관리 시스템 부재 | 앱 기반 통합 관리 및 검색 기능 제공 |

---

## 🚀 주요 기능

- **분실물 등록**  
  사용자가 분실한 물건을 사진과 함께 등록할 수 있습니다. (장소, 시간, 설명 포함)

- **습득물 등록**  
  습득자가 발견한 물건을 등록하여 분실자에게 알릴 수 있습니다.

- **분실물 검색**  
  키워드, 장소, 시간 등 다양한 조건으로 검색할 수 있습니다.

- **회원가입 및 로그인**  
  Firebase Authentication 기반 사용자 관리 기능을 지원합니다.

- **실시간 채팅 (DM 기능)**  
  분실자와 습득자가 직접 대화하여 빠른 분실물 회수를 돕습니다.

- **이미지 업로드 기능**  
  분실물, 습득물 사진을 실시간 등록할 수 있습니다.

---

## 🛠 기술 스택

| 구분 | 내용 |
|:-----|:-----|
| 언어 | Kotlin |
| 플랫폼 | Android (모바일 앱) |
| 서버/DB | Firebase Realtime Database |
| 인증 | Firebase Authentication |
| 빌드 도구 | Gradle (Kotlin DSL) |
| 개발 환경 | Android Studio |

---
## 🖼️ 화면 예시

<div align="center">

<table>
<tr>
<td align="center" width="33%">
<img src="https://github.com/user-attachments/assets/6b3eb239-baf8-42f8-a88a-cc7e36b1c8d9" width="250px"/><br/>
메인 페이지
</td>
<td align="center" width="33%">
<img src="https://github.com/user-attachments/assets/f7f08430-a85d-404b-9692-a09107fc3806" width="250px"/><br/>
분실물 등록 페이지
</td>
<td align="center" width="33%">
<img src="https://github.com/user-attachments/assets/9b3303bc-bc3f-485c-9d3c-7a017f6f85e0" width="250px"/><br/>
분실물 검색 및 조회 페이지
</td>
</tr>

<tr>
<td align="center" width="33%">
<img src="https://github.com/user-attachments/assets/34249b46-ab1a-4d79-a010-19551d5a6b35" width="250px"/><br/>
분실물 상세 조회 페이지
</td>
<td align="center" width="33%">
<img src="https://github.com/user-attachments/assets/e58d0d10-d2f3-4dab-8cf1-86b35c8e27f6" width="250px"/><br/>
습득물 검색 및 조회 페이지
</td>
<td align="center" width="33%">
<img src="https://github.com/user-attachments/assets/f92b274d-db3b-4d1a-b2b1-b1cf8b4dd01b" width="250px"/><br/>
실시간 채팅 기능 화면
</td>
</tr>
</table>

</div>

---

## 🎯 기대 효과 및 활용 방안

- 분실물 회수율을 대폭 향상시킬 수 있습니다.
- 사용자 간 직접 소통을 통해 분실물 반환 속도를 높일 수 있습니다.
- 조선대학교 내부 커뮤니티 활성화에 기여할 수 있습니다.


---

## 👥 팀원

| 이름 | 역할 |
|:----|:----|
| 전호원 | 프로젝트 총괄, Firebase Authentication ,Firebase 연동, 채팅 기능 구현, 주요 화면 개발 |
| 김다현 | 메인페이지, 마이페이지 구현 |
| 임지아 | Firebase 연동 |
| 박다은 | 디자인 |

---
