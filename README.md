# Java 콘솔 기반 e-Commerce 애플리케이션 사용설명서

이 프로젝트는 자바 콘솔 환경에서 동작하는 e-Commerce 시스템입니다.  
Chat GPT를 참조하여 작성하였습니다.

## 🛠 기술 스택

![Java](https://img.shields.io/badge/Java_17%2B-007396?style=flat&logo=java&logoColor=white)
![Oracle](https://img.shields.io/badge/Oracle_DB-F80000?style=flat&logo=oracle&logoColor=white)
![JDBC](https://img.shields.io/badge/JDBC-007396?style=flat&logo=java&logoColor=white)

## 📋 요구 사항

- Java JDK 17 이상
- Oracle Database 접속 정보
- ojdbc11 드라이버

 ## 📂 프로젝트 구조

```text
java_final_eCommerce/
├── src/
│   ├── config/           # 애플리케이션 설정
│   ├── controller/       # 컨트롤러 계층
│   ├── domain/           # 도메인 모델 및 리포지토리
│   │   ├── model/        # 데이터 모델 (User, Product 등)
│   │   └── repository/   # 데이터 액세스 계층
│   ├── exception/        # 커스텀 예외 클래스
│   ├── lib/              # 외부 라이브러리
│   ├── resources/        # 설정 파일 (DB 연결 등)
│   ├── service/          # 비즈니스 로직
│   ├── test/             # 테스트 코드
│   ├── util/             # 유틸리티 함수
│   └── view/             # UI 관련 코드 (콘솔 IO)
│       └── Main.java     # 애플리케이션 진입점
│
└── lib/                  # 외부 라이브러리 JAR 파일
    └── ojdbc11.jar       # Oracle JDBC 드라이버
```

## 🏃‍♂️ 실행 방법

### 1. 프로젝트 클론

```bash
git clone [repository-url]
cd java_final_eCommerce
```

### 2. 데이터베이스 설정

`src/resources/db.properties` 파일에서 데이터베이스 접속 정보를 설정합니다.

### 3. 실행 방법

#### ✅ 명령행에서 실행

```bash
# 인코딩 설정
$OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::InputEncoding = [System.Text.Encoding]::UTF8
chcp 65001

# 컴파일
javac -encoding UTF-8 -d out/production/java_final_eCommerce -cp "lib/*" src/view/Main.java src/config/*.java src/controller/*.java src/domain/model/*.java src/domain/repository/*.java src/exception/*.java src/service/*.java src/util/*.java src/view/*.java

# 실행
java -cp "out/production/java_final_eCommerce;lib/*" view.Main
```
#### ✅ IDE에서 실행
src\lib에 있는 라이브러리를 추가하고 `src\view\Main.java`를 Run


## 🔑 테스트용 계정 정보

프로젝트를 빠르게 테스트하기 위한 샘플 계정 정보입니다:

| 이메일 | 비밀번호 | 사용자 유형 | 이름 |
|---|---|---|---|
| user1@example.com | Test1234! | 일반사용자 | 홍길동 |
| user2@example.com | Test1234! | 일반사용자 | 김철수 |
| user3@example.com | Test1234! | 일반사용자 | 이영희 |
| user4@example.com | Test1234! | 일반사용자 | 박민수 |
| user5@example.com | Test1234! | 일반사용자 | 정지원 |
| admin@example.com | Admin1234! | 관리자 | 관리자 |

> 참고: 위 계정은 데이터베이스에 미리 설정되어 있습니다.


## 📊 메뉴 구조

애플리케이션은 다음과 같은 메뉴 구조로 구성되어 있습니다:

### 1. 메인 메뉴 (비로그인 상태)

```
===== Java 콘솔 기반 e-Commerce 애플리케이션 =====
메인 메뉴
1. 로그인
2. 회원가입
3. 종료
선택: 
```

### 2. 사용자 메뉴 (일반 사용자 로그인)

```
사용자 메뉴
1. 상품 목록 보기
2. 상품 상세 보기
3. 내 정보 수정
4. 비밀번호 변경
5. 회원 탈퇴 요청
6. 로그아웃
선택:
```

### 3. 관리자 메뉴 (관리자 로그인)

```
관리자 메뉴
1. 상품 등록
2. 상품 수정
3. 상품 삭제
4. 상품 목록 보기
5. 상품 상세 보기
6. 상품 판매 상태 관리
7. 재고 관리
8. 품절 처리
9. 내 정보 수정
10. 비밀번호 변경
11. 회원 탈퇴 요청
12. 로그아웃
선택:
```

## 📝 상세 사용 설명서

## 1. 비로그인 메인 메뉴

비로그인 상태에서는 다음과 같은 3가지 메뉴가 제공됩니다:

### 1.1. 로그인
- **기능**: 사용자 인증을 위한 로그인 기능을 제공합니다.
- **사용방법**: 사용자 ID와 비밀번호를 입력하여 로그인합니다.
- **결과**: 로그인 성공 시 사용자 유형(일반/관리자)에 따라 해당 메뉴로 이동합니다.

### 1.2. 회원가입
- **기능**: 새로운 계정을 생성합니다.
- **사용방법**: 필수 정보(ID, 비밀번호, 이름, 이메일, 전화번호 등)를 입력하여 회원가입합니다. 비밀번호는 영문 대/소문자와 숫자를 각각 1개 이상으로 구성된 5~15자 문자열이어야 합니다.
- **결과**: 회원가입 성공 시 메인 메뉴로 돌아갑니다.

### 1.3. 종료
- **기능**: 애플리케이션을 종료합니다.
- **사용방법**: 메뉴에서 '종료' 옵션을 선택합니다.
- **결과**: 프로그램이 종료되고 "애플리케이션을 종료합니다. 이용해 주셔서 감사합니다." 메시지가 표시됩니다.

## 2. 일반 사용자 메뉴

일반 사용자로 로그인 시 다음 메뉴가 제공됩니다:

### 2.1. 상품 목록 보기
- **기능**: 등록된 모든 상품의 목록을 확인합니다.
- **사용방법**: 메뉴에서 '상품 목록 보기'를 선택합니다.
- **결과**: 상품 코드, 상품명, 가격, 재고, 상태(판매중, 품절 등) 정보가 표시됩니다.
- **추가 기능**: 가격 낮은순/높은순으로 정렬하여 볼 수 있습니다.

### 2.2. 상품 상세 보기
- **기능**: 특정 상품의 상세 정보를 확인합니다.
- **사용방법**: 상품 코드를 입력하여 해당 상품의 상세 정보를 조회합니다.
- **결과**: 선택한 상품의 상세 정보(상품 코드, 상품명, 상품 설명, 판매가, 소비자가, 재고, 배송비, 판매 기간, 상품 상태)가 표시됩니다.

### 2.3. 내 정보 수정
- **기능**: 로그인한 사용자의 개인 정보를 수정합니다.
- **사용방법**: 변경할 정보를 입력합니다.
- **결과**: 정보 수정 성공 시 사용자 메뉴로 돌아갑니다.

### 2.4. 비밀번호 변경
- **기능**: 로그인한 사용자의 비밀번호를 변경합니다.
- **사용방법**: 현재 비밀번호 확인 후 새 비밀번호를 입력합니다. 비밀번호는 영문 대/소문자와 숫자를 각각 1개 이상으로 구성된 5~15자 문자열이어야 합니다.
- **결과**: 비밀번호 변경 성공 시 사용자 메뉴로 돌아갑니다.

### 2.5. 회원 탈퇴 요청
- **기능**: 회원 탈퇴를 요청합니다.
- **사용방법**: 탈퇴 확인을 위한 절차를 진행합니다.
- **결과**: 탈퇴 요청 성공 시 사용자 메뉴로 돌아갑니다.

### 2.6. 로그아웃
- **기능**: 현재 로그인된 계정에서 로그아웃합니다.
- **사용방법**: 메뉴에서 '로그아웃'을 선택합니다.
- **결과**: 로그아웃 후 메인 메뉴로 돌아갑니다.

## 3. 관리자 메뉴

관리자로 로그인 시 다음 메뉴가 제공됩니다:

### 3.1. 상품 등록
- **기능**: 새로운 상품을 시스템에 등록합니다.
- **사용방법**: 상품 정보(상품명, 상품 설명, 판매가, 소비자가, 재고 수량, 판매 기간, 배송비 등)를 입력합니다.
- **결과**: 상품 등록 성공 시 관리자 메뉴로 돌아갑니다.

### 3.2. 상품 수정
- **기능**: 기존 등록된 상품 정보를 수정합니다.
- **사용방법**: 수정할 상품 코드를 입력한 후, 변경할 정보(상품명, 상품 설명, 판매가, 재고 등)를 입력합니다.
- **결과**: 상품 정보 수정 성공 시 관리자 메뉴로 돌아갑니다.

### 3.3. 상품 삭제
- **기능**: 등록된 상품을 시스템에서 삭제합니다.
- **사용방법**: 삭제할 상품 코드를 입력한 후, 삭제 확인 과정을 진행합니다.
- **결과**: 상품 삭제 성공 시 관리자 메뉴로 돌아갑니다.

### 3.4. 상품 목록 보기
- **기능**: 일반 사용자 메뉴와 동일하게 모든 상품 목록을 확인합니다.
- **사용방법**: 메뉴에서 '상품 목록 보기'를 선택합니다.
- **결과**: 상품 코드, 상품명, 가격, 재고, 상태 정보가 표시됩니다.

### 3.5. 상품 상세 보기
- **기능**: 일반 사용자 메뉴와 동일하게 특정 상품의 상세 정보를 확인합니다.
- **사용방법**: 상품 코드를 입력하여 해당 상품의 상세 정보를 조회합니다.
- **결과**: 선택한 상품의 상세 정보가 표시됩니다.

### 3.6. 상품 판매 상태 관리
- **기능**: 상품의 판매 상태를 관리합니다.
- **사용방법**: 관리할 상품 코드를 입력한 후, 판매 기간 설정/판매 중지/판매 재개 중 선택합니다.
- **결과**: 상품의 판매 상태가 변경됩니다.
- **옵션**:
  - **판매 기간 설정**: 상품의 판매 시작일과 종료일을 설정합니다.
  - **판매 중지 처리**: 상품의 판매를 중지합니다.
  - **판매 재개**: 현재 날짜부터 1년 후까지 판매 기간을 설정하여 판매를 재개합니다.

### 3.7. 재고 관리
- **기능**: 상품의 재고를 관리합니다.
- **사용방법**: 재고를 관리할 상품 코드를 입력한 후, 새 재고 수량을 설정합니다.
- **결과**: 상품의 재고가 업데이트됩니다.

### 3.8. 품절 처리
- **기능**: 특정 상품을 품절 처리합니다.
- **사용방법**: 품절 처리할 상품 코드를 입력한 후, 품절 처리 확인 과정을 진행합니다.
- **결과**: 상품이 품절 상태로 변경됩니다.

### 3.9. 내 정보 수정
- **기능**: 일반 사용자 메뉴와 동일하게 관리자 자신의 개인 정보를 수정합니다.
- **사용방법**: 변경할 정보를 입력합니다.
- **결과**: 정보 수정 성공 시 관리자 메뉴로 돌아갑니다.

### 3.10. 비밀번호 변경
- **기능**: 일반 사용자 메뉴와 동일하게 관리자 자신의 비밀번호를 변경합니다.
- **사용방법**: 현재 비밀번호 확인 후 새 비밀번호를 입력합니다. 비밀번호는 영문 대/소문자와 숫자를 각각 1개 이상으로 구성된 5~15자 문자열이어야 합니다.
- **결과**: 비밀번호 변경 성공 시 관리자 메뉴로 돌아갑니다.

### 3.11. 회원 탈퇴 요청
- **기능**: 일반 사용자 메뉴와 동일하게 관리자 자신의 회원 탈퇴를 요청합니다.
- **사용방법**: 탈퇴 확인을 위한 절차를 진행합니다.
- **결과**: 탈퇴 요청 성공 시 관리자 메뉴로 돌아갑니다.

### 3.12. 로그아웃
- **기능**: 현재 로그인된 관리자 계정에서 로그아웃합니다.
- **사용방법**: 메뉴에서 '로그아웃'을 선택합니다.
- **결과**: 로그아웃 후 메인 메뉴로 돌아갑니다.
