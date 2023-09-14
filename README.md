## 상품 및 연관 상품 CRUD API (성명: 전승환)

### 프로젝트 소개

_product.csv, rec.csv 두 파일을 활용해 상품 정보를 생성, 조회, 수정, 삭제할 수 있는 API 구현 프로젝트_

### ERD


![erd.png](images/erd.png)

### 개발 환경

- 자바: ``JAVA 11``
- 개발 툴: ``Spring Boot 2.7.15``
- 필수 라이브러리: ``SpringBoot Web``, ``MySQL``, ``Spring Data JPA``, ``Lombok``, ``Swagger``
- 빌드: ``Gradle 8.2.1``
- DB: ``MySQL``
- CLOUD: ``AWS EC2``

### Swagger API 실행 방법
<hr>

#### MySQL DB를 AWS EC2 서버에 올려 사용했습니다. 다음과 같이 설정 후 빌드하시면 됩니다.
1. 실행 환경변수를 설정하기 위해 아래의 화면에서 Edit에 들어갑니다. 

![img_1.png](images/img_1.png)

2. 아래와 같은 화면을 볼 수 있습니다. Environment variables 칸에 아래 문자열을 입력합니다:
```
SPRING_DATASOURCE_PASSWORD=Ingod2013!;SPRING_DATASOURCE_URL=jdbc:mysql://ec2-13-209-88-90.ap-northeast-2.compute.amazonaws.com:3306/techlabs-db;SPRING_DATASOURCE_USERNAME=root
```
![img_3.png](images/img_3.png)

3. run 을 클릭하여 실행합니다.

![img_5.png](images/img_5.png)

4. 실행이 되면 브라우저에 접속합니다.

5. 주소창에 다음을 입력합니다:
``http://localhost:8080/swagger-ui/``

6. 아래와 같은 화면을 볼 수 있습니다. 

![img_6.png](images/img_6.png)

### 사용 가이드

#### 1. "[사용자] 상품 등록 API"

-설명:
-입력:
-출력:

#### 2. "[사용자] 연관 상품 등록 API"

- 설명: 상품 정보를 입력하면 이미 있는 상품일 경우 새롭게 추가하지 않고, 기존에 없는 상품일 경우 새롭게 추가해서 연관된 상품의 정보와 함께 저장합니다.
- 입력:
- 출력:

#### 3. "[사용자] 상품 수정 API"

- 설명: 수정 시 아이디를 제외한 상품 정보와 함께 수정할 상품의 고유 아이디를 입력하면 수정 가능합니다.
- 입력:
- 출력:

#### 4. "[사용자] 상품 삭제 API"

- 설명: Soft Delete 로 구현하였습니다.
- 입력:
- 출력: