## 상품 및 연관 상품 CRUD API (성명: 전승환)

### 프로젝트 소개

_product.csv, rec.csv 두 파일을 활용해 상품 정보를 생성, 조회, 수정, 삭제할 수 있는 API 구현 프로젝트_

### ERD

![img.png](images/erd.png)

### 개발 환경

- 자바: ``JAVA 11``
- 개발 툴: ``Spring Boot 2.7.15``
- 필수 라이브러리: ``SpringBoot Web``, ``MySQL``, ``Spring Data JPA``, ``Lombok``, ``Swagger``
- 빌드: ``Gradle 8.2.1``
- DB: ``MySQL``
- CLOUD: ``AWS EC2``

<hr>

### Swagger API 실행 방법

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

<hr>

### 사용 가이드 - 상품 등록, 수정, 삭제에 대한 설명

#### 1. [사용자] 상품 등록

- 엔드포인트: ``POST /rec/items/extra``
- 설명: 상품 정보를 입력하면 DB 에 저장합니다. 
- 입력: ``Long item_id : 상품 고유 아이디``, ``String item_image : 상품 이미지 url``, ``String item_name : 상품명``, ``String item_url : 상품 판매처 url``, ``Integer original_price : 상품 원가``, ``Integer sale_price : 상품 판매가``


#### 2. [사용자] 연관 상품 등록

- 엔드포인트: ``POST /rec/items/chain``
- 설명: 상품 정보를 입력하면 이미 있는 상품일 경우 새롭게 추가하지 않고, 기존에 없는 상품일 경우 새롭게 추가해서 연관된 상품의 정보와 함께 저장합니다.
- 입력: 상품 등록의 입력과 동일 + ``Long target_item_id : 연관 상품 고유 아이디``, ``Integer rank : 연관도 순위``, ``Integer score : 연관도 점수``

#### 3. [사용자] 상품 수정

- 엔드포인트: ``PUT /rec/items/update``
- 설명: 수정 시 아이디를 제외한 상품 정보와 함께 수정할 상품의 고유 아이디를 입력하면 수정 가능합니다.
- 입력: ``Long item_id : 수정할 상품 고유 아이디``, 상품 등록의 입력과 동일 (단, ``Long item_id`` 는 제외)

#### 4. [사용자] 상품 삭제

- 엔드포인트: ``DELETE /rec/items/removal``
- 설명: Soft Delete 로 구현하였습니다. 삭제 요청 시 상품의 컬럼에 있는 ``deleted_at`` 값이 현재 시간으로 채워집니다. 
- 입력: ``Long item_id : 삭제할 상품 고유 아이디``

<hr>

### 개발 중 고려한 사항

1. 콤마(,)로 구분된 상품 고유 아이디를 입력 받아서 배열로 만든 후 JPA 로 In 절을 사용해 배열에 있는 상품 고유 아이디에 해당하는 row 를 모두 찾는 로직을 구현하였습니다. 
- 참고: [JPA In 절 사용하기](https://way-be-developer.tistory.com/214)

2. 연관 상품과 상품의 관계를 더 직관적이게 표현하기 위해 한 상품에서 연관 상품으로 PK 인 데이터의 id 가 아닌 unique key 인 상품의 고유 번호를 참조하도록 하였습니다.
이 때 Product 가 Serializable 을 구현하도록 하여 ClassCastException 을 해결하였습니다. 
- 참고: [PK 가 아닌 컬럼을 조인할 때 주의할 점](https://hanke-r.tistory.com/136)

3. DB를 수동으로 지운 뒤 처음부터 csv 파일의 내용을 DB 에 저장하면 데이터의 아이디가 1부터 증가하지 않게 됩니다.
이 때 ALTER TABLE 을 사용하여 다시 1부터 시작하도록 만들어주었습니다.
- 참고: [AUTO_INCREMENT 초기화하기](https://m.blog.naver.com/dldudcks1779/222006115309)

4. Json 형태 데이터 출력 시 카멜 케이스로 나오는 것을 스네이크 케이스가 되도록 변경해주어 가독성을 높였습니다.
- 참고 : [Json 형태 반환 (SnakeCase or CamelCase)](https://velog.io/@ililil9482/spring-json-%ED%98%95%ED%83%9C-%EB%B0%98%ED%99%98-SnakeCase-or-CamelCase), [@JsonProperty, @JsonNaming 정리](https://dev-jwblog.tistory.com/120)

5. 커밋 메시지를 통해 직관적으로 어떤 내용이 수정되었는지 쉽게 알아채게 하기 위해 커밋 컨벤션을 따랐습니다.
- 참고: [커밋 컨벤션](https://velog.io/@archivvonjang/Git-Commit-Message-Convention)

6. 실수로 실행되는 프로젝트를 강제종료하지 않고 Disconnect 하는 바람에 이후에 실행이 안되고 Port 8080 was already in use 에러가 발생하는 문제를 해결하였습니다.
- 참고: [Port 8080 was already in use 에러](https://dundung.tistory.com/148)

<hr>

### 참고할 사항
- 모든 API 들은 TDD(Test Driven Development) 로 테스트 케이스를 우선 작성한 후 기능을 구현하는 방식으로 개발하였습니다.

### 그 외 참고 자료
- [JUNIT 테스트 Too Many Actual Invocations 해결](https://zi-c.tistory.com/47)
- [JUNIT 테스트 wanted but not invoked 에러 해결](https://velog.io/@dmdwns2/Wanted-but-not-invoked)
- [Spring Boot 에서 테스트 코드 작성하기](https://velog.io/@shawnhansh/SpringBoot%EC%97%90%EC%84%9C-%ED%85%8C%EC%8A%A4%ED%8A%B8%EC%BD%94%EB%93%9C-%EC%9E%91%EC%84%B1%ED%95%98%EA%B8%B0)