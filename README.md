# Studycafe_final
studycafe_renewal -> studycafe_final  

## 📋 개발환경
- IntelliJ
- Postman
- MySQL Workbench
- GitHub

## 📋 사용 기술
### 백엔드
### Spring boot
- JAVA 12
- Spring MVC
- Spring Boot Security
- Spring Data JPA
- Spring AOP
### Build tool
- Gradle
### Database
- Mysql
- H2

### 프론트엔드
- Javascript
- Thymeleaf

📋 주요 키워드
- RESTFUL API
- 시큐리티
- JPA
- 페이징
- 트랜잭션
- 예외처리
- Oauth 2.0
- 테스트
- Git 버전관리
- 클린코드


## 5/2
form에서 객체가 넘어오질 않는다. 타임리프 submit공부 필요.  

## 5/12
기초 기술들을 익혔지만 프로젝트의 흐름을 따라가면서 어떻게 작동하는지에 대한 이해가 많이 부족한 것을 느꼈다.   
앞으로 프로젝트를하면서 배운 기술을들 기술이 작동되는 전체적인 흐름을 잘 파악해서 오류나 하고 싶은 기능을 원활하게 할 수 있게     
하나의 기술을 사용하더라도 더 넓은 그림을 보고 기술이 어떻게 작용하는지 자세히 알아야겠다.  

## 5/13
Repository형태 고민...
jpa와 쿼리dsl을 재대로 편하게 쓰기위해서 프로젝트 구조를 바꿔야하는데 어떻게 해야할지 고민..  
+ 현재 jpaRepsoitory가 너무 형편없고 구지다...  
+ 프로젝트 다시 한번 보면서 재대로 코드 작성하자  

## 5/17
intercept와 exception은 나중에 작업하기로 결정.  

기존 코드는 공지사항과 커뮤니티를 따로 구분되어 있지만, 이번에는 하나 table로 관리하기로 결정.  
기존 작성일자인 board.time을 **날짜와 시간을 정확하게 저장하기 위해서** varchar가 아닌 timestamp로 사용하기로 결정.  

## 5/20
username을 검색기능에 추가하기 위해서 username을 db에 넣었다. 
이게 맞는 결과일까??

그러니 username을 사용하기 위한 boardForm이 현재 애매해졌다

그치만 추후에 10개나 20개 마다 아래 버튼을 만들어서 페이지를 넘어가게 해줄때 쓸 수 있을거 같다.

## 5/22
상품쪽 기본 뼈대 만들기

# 5/23
검색기능에 가격이랑 좋아요수 마저하기

home best 상품에도 상품 카테고리 검색은 만들기 

## 5/24
장바구니 기능을 구현하는데, 장바구니와 장바구니 상품을 담는 2가지 테이블이 필요할거 같았다.

처음에는 2개의 테이블을 하나로 구현하려고 했지만,
두개의 테이블은 엄연히 역할이 너무 다르므로 역할을 재대로 구분하지 못하여서 좋지 않은 코드가 될거 같다는 생각이 들어서 하던걸 갈아 엎고, 다시 작성하였다.

3가지 장점이 있따. 
- 확장성

장바구니에 여러 개의 상품을 담을 수 있고, 상품의 수량, 가격 등을 개별적으로 관리할 수 있습니다. 이는 장바구니 기능을 더욱 유연하게 확장할 수 있게 해줍니다
- 일관성 

-장바구니 정보를 수정할 때 장바구니 테이블과 장바구니 상품 테이블을 동시에 업데이트할 수 있습니다.
- 성능

장바구니에 대한 쿼리와 장바구니 상품에 대한 쿼리를 별도로 실행할 수 있으며, 성능을 최적화할 수 있습니다.

+ 장바구니에 중복으로 들어가는거 해결
## 5/25
장바구니에서 빼기

## 5/26
공지사항은 게시판 맨위로
게시판 권한에 따라서 특정 종류 게시판 쓰기,수정 접근 안되게하기
- 안 됬던 이유가 권한이 없던 페이지의 경로에서 redirectURI로 보냈을때, redirectURI가 요청 메서드에 도착을 못해서 defaultValue만 값이 지정되었다  
이유가 뭐였냐면 타임리프에서 form태그에서 th:action을 th:action="login"으로 수동 설정해서 그런거였다. th:action 만하여서 경로를 그대로 post로 보내줬어야했다.  
그래야지 뒤에 RequestParam까지 경로로 들어간다.

**인터셉터 활용시 주의 사항**  
클라이언트 부분에서 서버를 통하지 않고 바로 클라이언트나 서버사이드를 통해서 경로를 보낼경우 인터셉터가 작동하지 않는다.  
어떻게 보면 당연하다. dispatcherServlet에 도달하지 않으니 인터셉트가 작동할 수 없다.
ex)  
button th:onclick="|location.href='@{board/add}'|" type="button" ></button  
는 @{board/add}로 사용하여 서버사이드를 통해서 경로가 전달된다.그래서  
button th:onclick="|location.href='/board/add'|"></button

얘는 되는 이유가 뭐야?

## 5/27
인터셉터 기능 사용 완료

## 5/31
게시판 댓글, 답글 (DB고쳐야함)

## 6/1
게시판,상품(boards,products)에서 좋아요순,조회순,이런거 만들어주기

## 6/2 
팝업
formatter로 datetime바꾸기

## 6/3  
이상한 부분 전부 바꾸기 완료
페이지 넘버 붙이기 (1)

## 6/5
공지사항은 아예 완전 별도로 해서 맨 마지막에 붙이기
페이지 넘버 붙이기 (2)

## 6/7
상품에도 넘버 붙이기
게시판 10개나 20개 마다 아래 버튼을 만들어서 페이지를 넘어가게 해줄때 쓰기

## 6/8
소스트리 활용
develop 브랜치 생성 + 분리의 필요성 느낌.

## 6/9
OAuth2.0 (1)

## 6.10 
mysql
+ DB바꾸고 처음부터하니 몇몇 오류 수정
  OAuth2.0 (2) 감이 안잡히

## 6/12
bindResult를 통한 error controll(member)  

## 6/13
editmember 잘 작동하나 체크해야함.

## 6/22
logout 어떻게 진행되는 건지.

## 6/23 
ouath아이디 비밀번호, github에 안올라가게 하기.

## 7/6 (new~)  
아 했던거 다 날라갔다...  
버전관리잘못쓰다가 다 날라갔다.  
git이랑 sourcetree확실하게 공부해서 쓰자  

## 7/11 
mac 백틱 = option + ~  
```
//==연관관계 메서드==//
public void setBoard(Board board) {  
this.board = board;  
board.getAttachmentFiles().add(this);  
}
```
를 사용하기 위해서는 board객체의 attachmentfiles를 new ArrayList로 정의해둬야한다. 
 정의해두지않으면 nullpointException발생  

# 7/12
일벌이기
조금씩 해결하기

# 7/13
주소 객체 해결완료  
member Validation 몇몇 해결

# 7/14
게시판에 첨부파일 crud 전부 완료.  
Field 'member_id' doesn't have a default value (cart를 추가할때 연관관계 오류이다.)  

아!!!!!! 개발 편의성과 효율성의 갈림길을 만나버렸다... 맘 같아서 토이프로젝트여서 그냥 대충 편하게 하고싶지만, 먼가 방법이 없을까 고민하게 된다.....

## 7/15
mysql서버 접속이 안됨.  
온갖 블로그,커뮤니티 다 찾아서 다 해봤지만, 결국 안되고 늘은 것은 커맨드 명령어 숙지...  
모든 블로그의 마지막은 안되면 재설치 하라는 것이였다.  
교훈) DB백업은 필수다.  도커를 사용해보자  

# 7/17
member삭제하는데 연관관계 관련 삭제해서 지연로딩을 하는 과정에서 참조할 대상이 사라져서 그전꺼를 다 바꿔주고 해야하는데, 지연로딩떄문에 이게 한번에 되서 안된다.
board에서 첨부파일도 해결.
삭제시 연관객체들 관리
Oauth 완벽 완료

# 7/20
error 표시해주는 코드 완료.  
한국어 안나오는거 해결완료. - file encoding setting utf-8

# 7/24
order 서버단 코드 완료

# 7/27
order수정하기 왜 되는지 모르겠는데 일단 된다.

# 7/31
form에서 enum값을 string으로 바꿔서 보내줄까 하였는데,  
enum으로 쓰는게 코드 안정성이 높아지고, 기존 enum의 값이 바뀔 이유가 잘 없으므로, enum으로 쓰는게 안전할거 같아서 enum으로 작성해야겠다.

# 8/1 
좋아요 생성
search 완료
board 연관관계 수정

# 8/2
사용자 오류 발생시, Exception만든 후, Advice 활용하여서 사용자에게 어떻게 해야하는지 안내하기  

# 8/4
패키지 파일 하위파일은 10개 이하로,
3개 이상의 인스턴스 변수를 가진 클래스를 쓰지 않는다.

# 8/8
Board 의 Id를 진짜 조회만 하기위해서 하지만 변경을 막기위해서 board의 id를 새로운 객체로 깊은 복사해서 조회했다.
첨부파일을 받아오는 방법도 마찬가지로 바꿈.
```
return attachmentFiles.stream()
                .map(attachmentFile -> AttachmentFile.createAttachmentFile(attachmentFile))
                .collect(Collectors.toUnmodifiableList());
```

# 8/9
setter와 getter를 사용을 자제하자!
// createBoard, createComment() 를 만든 이후 생성에서 id 값을 못가져옴... domain단에서 만들어서 그런가..

# 8/10
nullporintException이 터지면 자료형이 null을 받을 수 있는지 확인해라.  

이렇게 하여서, 완전 새로운 값을 받아옴.

# 8/11
fetch join
// 사실 Form이나 DTO나 모두 단순히 계층간에 데이터를 전달할 때 사용합니다. 그래서 둘의 역할은 똑같습니다.  
// 다만 form이라는 것은 제약을 더 두어서 명확하게 컨트롤러 까지만 사용해야 한다는 의미를 강하게 두었습니다. form이라는 것 자체가 웹 기술에 종속적인 단어이니까요.  
// DTO는 이름 그대로 데이터 전송 객체인데, 더 범용적으로 사용되는 단어라 생각하시면 됩니다.  

# 8/12
JPA에서 Fetch Join의 조건은 다음과 같다.
ToOne은 몇개든 사용 가능  
ToMany는 1개만 가능  

예상하지 못한 Sql이 실행되 었는데 어디서 실행이 되는지 전혀 알 수가 없다.  
성능 향상을 시키려고 시도를 하다보니, 로그 추적기에 대한 필요성이 들었다.  

# 8/21
fetch join을 이용한 성능향상에서
board를 조회할때, comment나 attachment처럼 선택적으로 들어가는 것들을 fetch join 을 하니 없을시 null값이 반환되었다.  
그 뒤에 하나씩 존재하는 것을 하나씩 불러오는게 좋을거 같은데, 더 좋은 방법이 없을까?

우선 member는 board와 뗄 수 없는 관계이고, cart또한 member의 연관관계이므로

# 8/22
board의 선택적인 객체들을 어떻게 조회할까 고민하였는데, board 와 직접적인 연관관계가 맺혀있는 엔티티들은 어쩔 수 없이, 각각 따로 불러오는 방식을 사용했고,    
reply의 경우는 comment와 연관관계가 맺혀있기 떄문에  
삼항연산자로 comment 가 없으면 reply 쿼리를 안 불러와도 되게 하여 쿼리를 하나 줄였다.  

Error 발생시 , error 컨트롤(기본 error 화면이 아니라, 사용자 오류시, 어떤 원인으로 오류가 발생했는지 알려주고, 이전 페이지로 안내)
interceptor와 spring error controll로 만듬.  

# 8/23
oauth member naver login 로그인할떄마다 다른 로그인으로됨(해결)  
code error 여서 code refactoring 해결

# 8/24
핵심 domain에 로직 확인을 위한 aop적용  
2개이상의 fetchjoin 을 사용하려면, @NamedEntityGraph는 하나밖에 적용이 안되서 효율적이지 않다.  
(직접 createQuery 를 사용하는게 좋다)  

# 8/28
home controller home() 성능향상 174ms -> 112ms  
불필요한 board의 attachment를 불러오는 쿼리 삭제  

comment,reply 삭제 front에 적용.  
oauthMember도 기본회원처럼 아이디비밀번호로 로그인 가능  

# 8/29
interceptor 로 자신이 작성한게 아니면 수정 삭제가 안되게하기  
- board나 product의 정보를 가져와야하는데 
- 객체를 가져올 방법이 파라미터, db, 쿠키, 헤더, 세션로 가져오는 방식이 있는데,
- 보통 사용하는 방식은 파라미터를 이용한 방식이며 간단하지만 보안에 취약한 방법이다.
- 세션을 사용하는 방식은 리소스를 계속 잡아 먹고 있는 점이 마음에 안들었으며,
- 쿠키를 사용하는 방식 클라이언트에서 조작이 가능하므로, 보안에 취약해보였다.
- 그러하여 남은게 db,헤더 방식인데 db 방식은 데이터 중앙집중화가 가능하지만, 리소스나 성능측면에서 별로일거같아
- 헤더를 이용한 방식을 하기로하였다. 보안안정성이 다른것보다 좋아보였고, 
- 일부 프록시 서버나 캐싱 서버에서는 헤더 정보를 수정하거나 삭제할 수 있다는 단점이 있지만, 관리를 잘하면 좋은 방식일거 같았다.
- 라고 생각했는데, 파라메터를 이용하는 방식이 너무 간단해보여서 그걸로 바꿔 채택하였다. 기존 코드에서 크게 바꿀것이 없었다.
- 그리고 파라메터를 통한 전송의 단점이 URL에 노출되므로 안전상 문제인데, hidden을 사용해서 httpRequest본문으로 가져가면 편하게 가져갈 수 있다.
- 헤더를 하려하면 client에서 헤더를 설정하는 코드를 만들어야했는데, 그거 보다 편하고 좋은 방식인거 같다.


# 8/30
interceptor로 자기꺼아니면 수정삭제조회 안되게하기(댓글,덧글 삭제 / 게시판 수정)

# 8/31
interceptor에 팝업을 발생시키려고 했는데, preHandle()을 false로 반환을 시키면 그 뒤에 view를 원하는대로 controll할 수 가 없다.  
그렇다고 true로 반환시키면 뒤에 controller와 redirect가 섞여서 에러를 발생하는것 같은데 어떻게 해야할까?  
prehandle()에서 response를 통해서 problem이나 popup객체를 만들어서 json으로 client로 보내보자

# 9/1
아니? interceptor에서는 redirect로 주소를 정해주기떄문에, redirect를 하면 기존 response에 담았던 모든 정보가 사라지고 다시 주소로 할당해주는거여서,
interceptor에서는 할 수 없어.
그래서 loginform에서 redirect로 응답을 받았을때, ajax를 통해서 비동기처리를 해주면 될거 같은 느낌이 들어.

# 9/5
팝업을 서버를 통해서 띄우냐, js에서 바로 띄우냐의 2가지 방식으 고민했는데,  
단순 에러 메시지를 표현하는 팝업이므로,  
보안이나, 아직 동적인 작업이 없으니, js로 띄우주고  
추후 더 많은 에러 메시지를 포함해야하거나, 보안이 필요한 작업을 할때는 서버를 통해서 해야겠다.  

home에서만 로그아웃 뜨고 나머지에선 haeder에 로그아웃 안뜸문제  
- request.addattrubute는 jsp에서 사용하는 것으로 타임리프에서 호환이 안되는거 같다.  
- 그러하여 model.addattribute로 코드 교체해서 사용. 

로그인 권한이 없어서 걸러줫던 interceptor 는 redirect 기능이 필수였지만,   
권한부족으로인한 interceptor 는 주소 변경이 필수가 아니여서, 바로 json 으로 바로 데이터를 넣어줘서 처리하기로 결정.  

거기서 401 SC_UNAUTHORIZED 에러이냐... 403 SC_FORBIDDEN 에러이냐 고민...  
401코드는 인증이 실패한 경우에 보통 사용하고,  
403코드는 요청이 유효(이해)하지만 권한이 없는 경우에 사용되므로 403에 더 가까운 것같다.  
인증이 실패한것은 아니고 권한이 없는 경우이므로 

의미 없었고... spring 시큐리티로 설정하기로 결정.

# 9/6
주소를 바꿔주면서 팝업을 띄워주는 방법은 완전 노가다 방식말고는 없음을 깨달음.  
아님 -> interceptor에서 필요한 추가값들 더해서 true 로 반환해주고, 클라이언트에서 그 추가값들로 팝업을 먼저 띄워주고, 그리고 리다이렉트해주면  
그냥 spring 시큐리티로하자.  

# 10/2 
boardService Test 기본 테스트코드 작성 완료  

# 10/4
memberinfo 작동 안됨. - controller와 interceptor의 로직 충돌로 인해서 안된거였다. 변수명을 정확한 용도와 환경에 따라 분리해서 사용해야겠다.  
memberService 테스트 만들기,
memberController 테스트 만들기,

# 10/7
코드를 고칠 수록 자꾸 에러가 발생하고 에러가 어디서 나는지 알수 없으니 테스트 코드의 중요성을 깨닫는 중이다.  content
1. 매번 반복작업을 줄여준다 -> 빠른 피드백이 장점이다.
2. 자동검증
3. 개발자가 만든 기능을 안전하게 보호  

초기 테스트 작성은 테스트를 작성함으로 개발 단계 초기에 문제를 발견할 수 있게 도와준다.

# 10/13
git에서 properties(key정보있는거) 없애기 완료  
-> gitignore

## 레드 그린 사이클
1. 항상 실패하는 테스트를 먼저 작성하고(레드) - 기능 단위의 테스트
2. 테스트가 통과하는 프로덕션 코드를 작성하고(그린)
3. 테스트가 통과하면 프로덕션 코드를 리팩토링합니다.(Refactor)


### 해야할 것들
controllertest 작성(클라이언트와의 교류에서 오류가 나는 것도 서버 코드에 잘못이 있을 수 있다)
bulid패턴 이용하기.(생성자로 생성하는거에 비해 명시성이 뛰어남)  
controller에서만 form을 사용하는게 이름적으로 맞지않나?? service단에서 까지 form을 쓰는건 dto라고 이름을 해야할거 같은데...
#### entity클래스를 절대로 req/res클래스로 사용하면 안된다. (각 엔티티에가서 service나 controller에 있는거 고치기)
- 그치만 너무 먼길을 온거 같다. 다음부터 이걸 꼭 지키자.

테스트코드(각 기능별)

중복 쿼리 제거 (성능향상)
getter 쓰지 말라고 함. setter도 쓰지말라고 함.

관리자 페이지, 사용자 페이지 구분하기. 

알고리즘  
소스트리 작동법 배워서(api키 가리기)  

결제 코드 만들기
인증코드... spring security, jwt 인증


카카오 지도 api도 사용해보고 싶다.  
유닛테스트(SpringBootTest) (유지보수 기간의 생산성을 높여주고 새로 프로젝트에 투입될 사람에게도 이득을 주는 테스트   
프로젝트 오픈 일정 직전까지의 코드 변경과 버그 발견에 도움을 주는 테스트  
오늘 당장 프로그램을 목표한 곳까지 작성하는 일을 더 빨리 마치게 해주는 테스트)    단위 테스트: JUnit + Mockito 활용  
Cache 적용(Global/Local cache 적용범위, 라이프 싸이클, 솔루션 선택)  


컨테이너 - 도커  
쿠버네티스 -  
CI - 젠킨스 , 깃허브 액션 (지속통합)  
CD -  (지속배포)  
  
@Validated @ModelAttribute("member") CommonMemberForm form 이거에 대해서 좀 알아야함    


(aws)(EC2)(RDM)
(Docker)  
(jwt) - JSON 웹 토큰은 선택적 서명 및 선택적 암호화를 사용하여 데이터를 만들기 위한 인터넷 표준  
(restAPI)  
(EDR)  
(Swagger) - Swagger 는 REST API를 설계, 빌드, 문서화 및 사용하는 데 도움이되는 OpenAPI 사양을 중심으로 구축 된 오픈 소스 도구 세트입니다.
(Test code를 통한 시간 단축 (성능 향상 테스트))  
(ajax)  
(Exception 처리 ex) 결제기능 같은 경우는 실행되는거보다 예외처리가 더 중요함.)  

개발도구의 공식 레퍼런스를 보고 사용법을 스스로 익힐 수 있음  
자신이 경험한 사용법을 문서화해서 팀 내에 전파할 수 있음  
 
### 할까?
주문내역에서 Cancel 되면 며칠 후에 자동으로 entity 삭제해주기 (cancelDate 설정해서 처음 home화면 들어갈 때 체크해주면.) 쿠키?


# 환경 
spring boot 2.7.8      
java 11     
gradle  
Mysql  
(AWS)  
