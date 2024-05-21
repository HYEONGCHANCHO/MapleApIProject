# MapleCombat


<div align=left>

  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"> 
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white">
  <img src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white">
  <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white">
  <img src="https://img.shields.io/badge/Jmeter-6DB33F?style=for-the-badge&logo=Jmeter&logoColor=white">
</div>
<br/><br/>

<div align=center>
<img src=https://github.com/HYEONGCHANCHO/MapleApIProject/assets/118033064/bad350f1-57f8-449c-829a-190ed67a5b6f) >
</div>

<br/>

### 서비스 소개

MapleCombat은 Nexon Open API를 기반으로 한 전투력 정보 서비스입니다.<br/>
Open API를 통해 제공 받을 수 있는 정보는 현 시점의 전투력입니다.<br/>
본인 캐릭터에 맞는 다양한 장비 착용을 통해 전투력 시물레이션을 그려볼 수 있습니다.<br/>
가격대비 전투력 상승력 등을 확인해보며 가성비를 확인해볼 수 있는 서비스 입니다.

<br/><br/>
### 서비스 이용방법

1. 캐릭터 이름과 기준 날짜를 검색합니다.
2. Open API에서 가져올 수 없는 추가 스펙 정보를 입력합니다.
3. 다양한 장비 시물레이션을 추가하여 전투력 상승량을 확인합니다.

<br/><br/>
### 서비스 진행상황
2024.2~ 트래픽이 몰리는 상황을 고려한 캐릭터 OCID 정보 호출 및 저장 코드 작성 <br/>
(테스트 과정 기록 https://developerkiwi.tistory.com/entry/webclient)

<br/><br/>

## Project Sturucture

```
src
├── config //설정 관련
│   └── AsyncConfig
├── controller
│   └── CharacterController
├── dto
│   ├── itemInfoDTO
│   └── characterDTO
├── domain
│   ├── CharacterBaseTotalInfo
│   ├── CharactersInfo
│   ├── CharactersItemEquip
│   ├── Characterskey
│   └── CharactersStatInfo
├── repository //jpa
│   ├── CharacterBaseTotalInfoRepository
│   ├── CharactersInfoRepository
│   ├── CharactersItemEquipRepository
│   ├── CharacterskeyRepository
│   ├── CharactersInfoRepository
│   └── CharactersStatInfoRepository
├── service // 상품 도메인
│   └── CharacterService
└── application.yml // 환경변수
```
### 서비스 업데이트 일정
- 2024.3~ 캐릭터 세부, 착용 장비 정보 호출 및 저장 기능 추가. 전투력 계산식 적용.
- 2024.4~ 계산식 수정 및 정보 저장 코드 수정


## 테스트 진행

- “MapleCombat” 테스트 진행 과정 및 결과

- 테스트 설계 이유
    - Nexon Open API에서 데이터를 받기 위해 webclient를 사용하였으나 null값을 반환하는
     오류 발생
    - Thread Pool을 사용하는 환경에서 비동기적 방식인 webclient 사용으로 오류가 생김을 확인
    - 동기적인 처리를 추가하면 정상 데이터를 얻을 수 있지만 성능적인 하락이 있지 않을까 고려하여 테스트를 설계

- Thread pool 설정
    - 기본 Thread: 8 ,최대 Thread: 8, 1초당 요청수: 16으로 설정

- 설정 근거 (*Jmeter 테스트로 나온 수치 적용)
    - Thread의 개수 = cpu 수(8) *(1+대기 시간(48)/ 서비스 시간(50))
    - 1초당 요청수 = 스레드 풀 크기(8)/ 평균 응답시간(50)

- A,B 두 상황 가정하여 테스트 진행
    - A :Thread Pool 설정을 적용, webclient에 동기적 처리 적용
    - B :Thread Pool 설정을 미적용, webclient에 동기적 처리 미적용
    
- 테스트 결과 요약 (* 각각 3회의 테스트 진행 후 평균값 도출)
    - **표본 50일때 (저 부하 상태)**
        - A : 99%선 : 912, 처리량 : 51.4/sec
        - B : 99%선 : 982, 처리량 : 44.7/sec
    - **표본 500일때 (중간 부하 상태)**
        - A : 99%선 : 1608, 처리량 : 271.53/sec
        - B : 99%선 : 1133, 처리량 : 361.56/sec
    - **표본 5000일때 (고 부하 상태)**
        - A : 99%선 : 5839, 처리량 : 827.13/sec
        - B : 99%선 : 2136, 처리량 : 152.63/sec
        
- 테스트 한계점
    - 서버를 가동하는 컴퓨터와 테스트 컴퓨터가 동일함.
    - 테스트마다 편차가 있었다. 3회씩의 테스트 평균값을 냈지만 부족함.
    - Thread Pool 설정을 더 최적화 할 경우 다른 결과값이 나올 수 있다.

- 테스트 결과 정리
    - 처리량과 99% 처리 평균 속도를 보았을 때 저, 중간부하 테스트에서 두 방식의 성능차이는 크지 않았다.
    - 피크타임에 해당하는 고부하 테스트에서 A 방식이 99% 처리속도는 느리지만 
    처리량이 훨씬 많은 것으로 보아 더욱 적합한 방식이라 생각했다.
    - 기본적으로 A방식은 Thread Pool을 사용하기 때문에 서버 안정성에 더 신경을 쓴 방식이다. 따라서 B방식이 월등한 성능 차이를 내지 못한다면 A방식 적용이 
    적합하다 생각하였고 이를 프로젝트에 반영하였다.

## API

```
@RestController
   @Value("${external.api.key}")
    private String apiKey;

    @Value("${external.api.url}")
    private String apiUrl;
```
- 캐릭터 고유 ocid 제공 ```(GET /maplestory/v1/id)```
- 캐릭터 기본 정보 제공 ```(GET /maplestory/v1/character/basic)```
- 캐릭터 스탯 정보 제공 ```(GET /maplestory/v1/character/stat)```
- 캐릭터 착용 장비 정보 제공 ```(GET /maplestory/v1/character/item-equipment)```
- 착용 아이템 스탯정보 종합 (세트효과 미포함)제공 ```(GET /CharactersItemTotalStatInfo)```
- 아이템 정보 제공 ```(GET /CharactersEquipInfo)```
- 아이템 세트효과 제공 ```(GET /CharactersSetEffect)```
- 유니온 아티팩트 정보 제공 ```(GET /CharactersArtiInfo)```
- 유니온 공격대, 유니온 점령 정보 제공 ```(GET /CharactersUnionInfo)```
- 하이퍼스탯 정보 제공 ```(GET /CharactersHyperStatInfo)```
- 어빌리티 정보 제공 ```(GET /CharactersAbilityStatInfo)```
- 캐쉬 정보 제공 ```(GET /CharactersCashItemInfo)```
- 심볼 정보 제공 ```(GET /CharactersSimbolInfo)```
- 펫 정보 제공 ```(GET /CharactersPetEquipInfo)```
- 스킬 정보 제공 ```(GET /CharactersSkillStatInfo)```
- 헥사 스탯 정보 제공 ```(GET /CharactersHexaStatInfo)```
- 전체 스탯 합친 정보 정보 제공 ```(GET /CharactersTotalStatInfo)```
- 정보 종합 후 전투력 계산 후 제공 ```(GET /calCharactersCombat)```


## 환경변수 및 Config 정보

```
@Configuration
@EnableAsync
public class AsyncConfig {
    private int corePoolSize=8;
    private int maxPoolSize=16;
    private int queueCapacity=100;

    @Bean(name = "characterThreadPool")
    public Executor characterThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("CharacterThread-");
        executor.initialize();
        return executor;
    }
 
```
```
  external:
    api:
      key:  //open api 연결키
      url: https://open.api.nexon.com

  spring:
    datasource:
      url: jdbc:mysql://localhost:3306/mapleCombat?useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8
      username: 
      password: 
      driver-class-name: com.mysql.cj.jdbc.Driver
    jpa:
      hibernate:
        ddl-auto: create  //개발단계라 create로 설정
      show-sql: true
    properties:
      hibernate.format_sql: true
      dialect: org.hibernate.dialect.MySQL8InnoDBDialect
```
