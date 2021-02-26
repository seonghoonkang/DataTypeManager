# DataTypeManager

origin document: https://www.evernote.com/shard/s653/sh/32659bb9-8104-ec0f-56f9-17027b77b474/3b29c73b8849c500e15e9b145ef8c224

Abstract
---
- 'Data Type Manager' 는 Action APIs Executor 에서 Producer와 Consumer 사이의 데이터 교환에 필요한 Java Object의 Data Type을 관리한다.
- 'Data Type Manager' 는 JSON, XML 과 같은 텍스트 형태의 타입 정의서를 Java Class로 변환하여 저장하는 기능을 제공한다.
-  타입정의서는 Parser에 의해 Data Type Metadata와 Java Object Type 변환에 필요한 RowData로 분리된다.
- RowData는 Java Byte Code 로 컴파일 되고 class-path하위에 Metadata에 정의된 패키지와 타입이름으로 저장된다.

Subject
---
1. Parser - 타입 정의서 규약에 따라 작성된 JSON/XML등의 문서를  Metadata와 Row Data 로 변환한다.
    - Standardize Data Type protocol for JSON/XML
    - JSON/XML Parsing
    - Extract Row Data and Metadata
2. Transformer - Row Data 를 class Template을 이용하여 java source code 로 변환한다
    - Define class Template
    - Transform Java source code by Template Engine
3. Complier - 작성한 Java source code 를  실행가능한 Byte Code로 Compile 하여 Action APIs Executor의  Interface Parameter의 Data Type으로 사용할 수 있도록 도와준다.
    - Java code reader
    - Compile java code and save class file into class-path.

Glossary
---
- Universal Data Modeler  – Fact Data 의 Type을 모델링하는 UI tool 이다.
- Action APIs Executor – Pre Process와 같은 Consumer의 요청에 따라 적당한 Producer의 API를 실행하고 그 결과를 돌려주는 모듈이다.
- Execution Engine – Rule 실행에 필요한 선/후행 작업과 Rule 엔진에 Rule
- WDL Studio  – Rule 을 모델링 하는 UI tool이다. Fact Data Type의 Binding 기능을 포함한다.
- RowData – Template를 이용하여 소스 코드를 작성하기 위해 필요한 기초 데이터.
- Metadata –Parameter Data Type 정보나 Rule 관련 정보등 Executor의 실행에 필요한 정보  
  
Basic Constraint
---
1. 타입을 생성하는 작업을 수행하는 사람은 관리자와 같은 특수한 사용자들로 제한한다.
2. 로그인 사용자의 Session 별로 기능이 동작하는 것을 가정한다.
3. 중복을 방지하기 위해 패키지명은 시스템에서 관리하며 사용자가 임의로 작성할 수 없다.

System Structure
---



1. Action Management System은 Execution engine Part 와 Action Management Part 로 나뉜다. 해당 문서는 Action Management Part만 다룬다.
2. Action Management Part는 Data Type Manager, Action Manager, Data Collect 모듈로 나뉜다.
    - Data Type Manager
        - Parser: 데이터 타입이 정의된JSON/XML등의 Text 문서를 Parsing 하여 Metadata와 RowData 를 분리한다.
        - Transformer: Template 엔진을 이용하여 Row Data를 Java source code로 변환한다.
        - Compiler: 변환된 소스 코드를 Byte Code로 Compile 하여 Class-path에 class파일로 저장한다.


Detail
---
- How to implementation
    1. Parser - BIO에서 제공하는 공식 Json/XML Parser를 사용한다.
    2. Template Engine (https://en.wikipedia.org/wiki/Comparison_of_web_template_engines) - Apache velocity library 사용

        - 위 도표는 위키에 공식적으로 등록된 Java언어를 사용하는 템플릿 엔진 목록이다.
        - FreeMaker, Thymeleaf, JSP, WebMacro는 웹 템플릿 엔진
        - JET (https://www.eclipse.org/articles/Article-JET/jet_tutorial1.html)
            - 범용 템플릿엔진으로 JSP 형식의 코드 템플릿을 제공하여 소스 코드를 생성하는 라이브러리
            - JET은 EMF(Eclipse Model Framework) 의 하위 프로젝트로 Eclipse 에 의존적임 ("This project is part of Helios, Galileo, and Ganymede.")
            - 현재는 위 목록에서 볼 수 있듯 Eclipse Project에서도 EGL(https://projects.eclipse.org/projects/tools.edt) 을 사용함
        - Apache Velocity (stable release version 1.7)
          -  범용 템플릿엔진으로 *.vm 템플릿을 사용하면 Java source code로 변환 가능
            - 오래된 기술이며 매우 안정적임. 2010년 1.7 릴리즈 이후 현재까지 사용됨.
    3. Compiler - javax.tools.javaCompiler API를 사용한다. (JREㅇㅔ서 컴파일 하는 것을 찾기)
        - 잇점
            - java 표준 API 이다
            - JVM 위에서 java source code를 직접 컴파일 하므로 유효성 검사 같은 걱정이 필요없다.
            - 간단하고, 표준적이며 Code generation을 위한 단일 메커니즘을 제공한다.
            - 파일기반 소스코드로 제한하지 않고 사용할 수 있다.


Future
---
1. JRE에서 컴파일이 가능한지에대한 조사 필요
2. 동적생성 클래스의 관리 문제는?
