# DeliveryProject - 배송대행 신청 모듈

기술: Java 11+ 권장, Jakarta Servlet 5/6, JSP, Oracle XE, JDBC, Eclipse, Tomcat 10+

## 구성
- `/delivery/list` : 로그인 회원 신청 내역
- `/delivery/new` : 신청서 작성
- `/delivery/detail?id=...` : 신청서 상세
- `/delivery/edit?id=...` : 수정
- `/delivery/cancel` : 취소(POST)
- `/delivery/save` : 신규 등록(POST)
- `/delivery/update` : 수정(POST)

## 사전 준비
1. Eclipse에서 Dynamic Web Project를 만들고 Tomcat 10 이상을 연결합니다.
2. 프로젝트의 Java Build Path에 Oracle JDBC 드라이버(`ojdbc`)를 추가합니다.
3. Jakarta Servlet API는 Tomcat이 제공하므로 일반적인 Tomcat 배포에서는 서버 런타임에 포함시킵니다.
4. `database/delivery_schema.sql`을 Oracle에서 실행합니다.
5. `src/main/java/delivery/common/DBManager.java`의 DB URL, 계정, 비밀번호를 본인 환경에 맞게 수정합니다.
6. 파일들을 프로젝트의 같은 경로에 복사합니다. `src/main/webapp`을 WebContent로 사용하는 구형 Dynamic Web Project라면 webapp 폴더 내용을 `WebContent`에 넣습니다.

## 회원 로그인 연동 계약
현재 코드는 세션에서 `memberId`를 읽습니다. 로그인 담당 A가 로그인 성공 시 다음과 같이 저장하도록 협의하세요.
`session.setAttribute("memberId", memberDto.getMemberId());`
세션 값은 String 또는 숫자형이어도 처리하도록 구현했습니다. 로그인 전에는 신청 기능 접근이 차단됩니다.
회원 테이블과 FK는 팀 공통 스키마가 정해진 뒤 추가하세요.

## 유의사항
- 신청서와 상품은 별도 테이블이며 저장/수정은 트랜잭션으로 처리합니다.
- 취소는 물리 삭제 대신 상태를 `CANCELLED`로 변경합니다.
- 신청서 수정/취소는 본인 소유이고 `REQUESTED` 상태일 때만 허용합니다.
- 파일 업로드는 이번 기본 버전에 포함하지 않고 상품 URL만 저장합니다.
- 배송비/실제 운송장/입고 처리/관리자 상태 변경은 C, D 담당 영역입니다.
- 운영 배포 전 CSRF 방어, 비밀번호/설정 분리, 로깅, 입력 제한, 개인정보 보호 및 테스트를 보강해야 합니다.
