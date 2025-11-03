아래는 작성해드린 내용을 그대로 README.md 파일 형식으로 정리한 것입니다.
복사 후 프로젝트 루트에 README.md로 저장하세요.

⸻


# KTCU Demo API  
Java 17 + Spring Boot 3 + Gradle

간단한 **회원 생성/조회 + 로그인(Mock)** REST API 예제입니다.  
면접에서 “Spring Boot로 최소 API를 직접 만들고 실행해봤다”는 **증거**로 사용하기 좋도록 구성했습니다.

---

## ✅ 기술 스택
- Java 17 (Temurin/OpenJDK)
- Spring Boot 3.x
- Gradle (Wrapper 포함)
- Validation (Jakarta)
- 내장 Tomcat (기본 포트: 8080)

---

## 📂 프로젝트 구조

src/main/java/com/example/demo
├─ DemoApplication.java                 # 실행 진입점 (@SpringBootApplication)
│
├─ common
│  ├─ GlobalExceptionHandler.java       # 예외 → HTTP 상태코드 매핑(400/404 등)
│  └─ PingController.java (선택)        # /ping 헬스체크 (pong 반환)
│
└─ user
├─ UserController.java               # REST 엔드포인트: /api/users, /api/login
├─ UserService.java                  # 인메모리 저장소/로직(ConcurrentHashMap)
└─ dto
├─ UserCreateRequest.java         # 생성 요청 바디 (name/email/password + 검증)
├─ UserResponse.java              # 조회 응답 DTO (id/name/email)
└─ LoginRequest.java              # 로그인 요청 바디 (email/password + 검증)

#### 각 파일 역할 요약
| 파일 | 역할 |
|------|------|
| `DemoApplication.java` | 앱 부트스트랩 (필요 시 `scanBasePackages`로 스캔 고정) |
| `GlobalExceptionHandler.java` | `IllegalArgumentException` → 400 / `NoSuchElementException` → 404 |
| `PingController.java` | 서버/스캔 정상 여부 확인용 `/ping` (선택) |
| `UserController.java` | 회원 생성/조회/로그인 API 구현 |
| `UserService.java` | 인메모리 저장소 + 비즈니스 로직 |
| DTO 클래스 | 요청/응답 모델 + 입력값 검증(Validation) |

---

## ▶️ 실행 방법 (macOS, Gradle Wrapper)

```bash
# 1) 권한 부여(최초 1회)
chmod +x gradlew

# 2) 빌드
./gradlew clean build

# 3) 실행
./gradlew bootRun

	•	콘솔에 Tomcat started on port 8080가 보이면 준비 완료.
	•	포트 변경: src/main/resources/application.properties에 server.port=8081 등 추가.

✅ PathVariable 경고 방지 (권장)
build.gradle에 아래 설정으로 컴파일 시 파라미터 이름 보존:

tasks.withType(JavaCompile).configureEach {
    options.compilerArgs += ['-parameters']
}

또는 컨트롤러에서 @PathVariable("id") Long id 같이 이름을 명시해도 무방합니다.

⸻

📡 API 엔드포인트 요약

메서드	경로	설명
POST	/api/users	회원 생성 (ID 반환)
GET	/api/users/{id}	회원 조회
POST	/api/login	로그인(Mock)


⸻

🧪 테스트 (curl 예시)

# 1) 생성
curl -s -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Lee","email":"lee1@test.com","password":"pw1234"}'

# 2) 조회
curl -s http://localhost:8080/api/users/1

# 3) 로그인
curl -s -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"email":"lee1@test.com","password":"pw1234"}'


⸻

📋 실제 실행 로그 (발췌)

# (초기) PathVariable 이름 플래그 미설정 시 경고
curl http://localhost:8080/api/users/1
Name for argument of type [java.lang.Long] not specified, and parameter name information not available via reflection. Ensure that the compiler uses the '-parameters' flag.

# (사용자 미생성 상태에서 조회)
curl http://localhost:8080/api/users/1
해당 ID의 사용자가 없습니다.

# (생성)
curl -s -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Lee","email":"lee1@test.com","password":"pw1234"}'
1

# (조회 성공)
curl http://localhost:8080/api/users/1
{"id":1,"name":"Lee","email":"lee1@test.com"}

# (로그인 성공)
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"email":"lee1@test.com","password":"pw1234"}'
LOGIN_OK


⸻

⚠️ 예외 처리 규칙

상황	예외	HTTP
이메일 중복	IllegalArgumentException	400
없는 ID 조회	NoSuchElementException	404
요청 본문 검증 실패	MethodArgumentNotValidException	400
로그인 실패	(내부 처리)	401 (바디: LOGIN_FAIL)

GlobalExceptionHandler에서 처리합니다.

⸻

🧩 트러블슈팅
	•	500이 뜨는 경우 → 예외 핸들러 스캔되지 않았을 가능성
	•	패키지: com.example.demo.common
	•	@RestControllerAdvice 확인
	•	DemoApplication에 @SpringBootApplication(scanBasePackages="com.example.demo") 명시 가능
	•	서버 포트 충돌
	•	lsof -i :8080로 점유 프로세스 확인
	•	포트 변경: application.properties에 server.port=8081

⸻

🎯 다음 단계(선택)

🟢 JPA + H2로 영속 저장 구현
🟡 JWT 인증 + Spring Security 추가
🟣 JUnit 단위 테스트 작성
🔵 GitHub Actions(CI/CD) / Dockerize 예제 추가

⸻

📄 라이선스

MIT (자유롭게 수정/활용 가능)

---

