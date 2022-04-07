# ArchGuard backend

[![CI](https://github.com/archguard/archguard-backend/actions/workflows/ci.yml/badge.svg)](https://github.com/archguard/archguard-backend/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/archguard/archguard-backend/branch/master/graph/badge.svg?token=QS5H866CWH)](https://codecov.io/gh/archguard/archguard-backend)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/archguard/archguard-backend)

> ArchGuard is a architecture governance tool which can analysis architecture in container, component, code level, database, create architecture fitness functions, and test for architecture rules. 

Chinese: ArchGuard 是一个架构治理工具，用于管理和分析组织级别的软件架构。 结合 [C4 模型](https://c4model.com)，进行依赖分析，含容器级别（服务级别）、组件级别（/模块级别）、代码级别、数据库级别等。 
同时，可以创建系统的架构适应度函数，度量系统的各项指标。

All Components is opensource. Others:

- [ArchGuard Frontend](https://github.com/archguard/archguard-frontend)
- [ArchGuard Scanner](https://github.com/archguard/scanner)
- [Chapi](https://github.com/modernizing/chapi) source code analysis

Screenshots:

<table>
  <tr>
    <td><img src="https://archguard.org/assets/screenshots/archguard-20-overview.png"  alt="1" width = 480px ></td>
    <td><img src="https://archguard.org/assets/screenshots/archguard-20-apilist.png" alt="2" width = 480px ></td>
   </tr> 
   <tr>
      <td><img src="https://archguard.org/assets/screenshots/archguard-20-class.png" alt="3" width = 480px x></td>
      <td><img src="https://archguard.org/assets/screenshots/archguard-20-servicesmap.png" align="right" width = 480px ></td>
  </tr>
</table>

特性（Features）：

- 容器级别依赖分析（当前支持 HTTP API）。API 生产者支持语言：Java、Kotlin、C#，API 消费者支持语言：TypeScript/JavaScript、Kotlin、Java 等。
   - HTTP API 使用清单、调用清单
   - HTTP API 依赖可视化分析
- 五大维度架构质量评估以及对应的指标分析。
   - 体量维度。过大的包、类、方法、模块
   - 耦合维度。枢纽模块、包、类、方法，数据泥团、过深继承、循环依赖
   - 内聚维度。霰弹式修改、数据类
   - 冗余维度。冗余元素、过度泛化
   - 质量维度（Java）。包含休眠的测试、被忽略的测试、缺乏校验的测试、包含繁杂判断的测试、包含冗余打印的测试、静态方法
- 代码坏味道分析。常见的那些。
- 还有其它相关的总览 
   - 代码间依赖分析。支持级别模块、包、类、方法四个级别。
   - 代码行数分析。
   - 系统不稳定性模块分析。
- 数据库地图（进行中）
- 精准测试/变化分析（进行中）

Features: 

- C4 analysis
    - [x] container dependency analysis. (level: HTTP API)
      - [x] basic fe/be call
      - [x] RestTemplate for backend to backend
      - [ ] GraphQL
      - [ ] Kong Gateway
    - [x] component (module) dependency analysis.
    - [x] code dependency analysis. (level: pa``ckage, class, method)
    - [ ] database dependency analysis
- Scanner integration
    - [x] PMD
    - [x] Git with jGit
        - [x] HotFile
    - [x] Java/Jvm only
        - [x] JVM Bytecode (need to rewrite with License issue)
        - [x] CheckStyle
        - [x] Badsmell by DesigniteJava
        - [x] Test Badsmell by Coca (Java only)
    - [x] TypeScript with Chapi
    - [x] Kotlin with Chapi
    - [x] Git Hot File 
- System Info
    - [ ] Custom build command  

Languages parse by [Chapi](https://github.com/modernizing/chapi)

| Features/Languages  | Java | Python | Go  | Kotlin | TypeScript | C   | C#  | Scala | C++ |
|---------------------|------|--------|-----|--------|------------|-----|-----|-------|-----|
| http api decl       | ✅    | 🆕     | 🆕  | ✅      | ✅          | 🆕  | ✅  | 🆕    | 🆕  |
| syntax parse        | ✅    | ✅      | ✅   | ✅     | ✅         | 🆕  | ✅  | ✅     | 🆕  |
| function call       | ✅    | 🆕     |     | ✅     | ✅          |     |     |       |     |
| arch/package        | ✅    |        |     | ✅        | ✅       |     |  ✅ | ✅     |     |
| real world validate | ✅    |        |     |        | ✅          |     |     |       |     |

### Tech decision (framework)

- languages：Kotlin
- frameworks：Spring Boot，JDBI
- test frameworks：Junit5，Spring Boot Test，Flyway，H2
- build tool：Gradle
- data storage：MySQL, InfluxDB

### Local setup
#### database setup
1. Local mysql, or docker created
- `docker pull mysql:8`
- `docker run --name=mysql -it -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password -d mysql`
2. Create archguard database
- `create database archguard default character set utf8mb4 collate utf8mb4_unicode_ci;`
- `./gradlew -Dflyway.configFiles=flyway.conf flywayMigrate` (probably not needed)

#### run
`./gradlew bootrun`

### Docker

```
mkdir -p archguard_mysql
chmod -R 777 archguard_mysql
```

```
docker-compose up
```

Known issues with Colima: `docker mysql exited 137 memory`

The default VM created by Colima has 2 CPUs, 2GiB memory and 60GiB storage.  When run scanner in large projects, the default config will make MySQL exited, can to set more memory for ArchGuard:

```
colima start --cpu 4 --memory 8
```

### Chat

欢迎加入我们：

<img src="https://archguard.org/qrcode.jpg" width="380" height="480">

## Q & A

### Scanner 没有数据

```
java "-Ddburl=jdbc:mysql://localhost:3306/archguard?user=root&password=&useSSL=false" -jar scan_sourcecode.jar --system-id=6 --language=java --path=.
```

1. 运行目录 scanner 中的 .jar 是否完整。如果出错了，需要从 GitHub 重新下载。
2. 查看是否生成对应的 sql 文件。如果没有的话，建议可以提交 issue，包含错误日志。

### vs APM

APM is awesome for developer. APM is build in runtime, ArchGuard is focus on development and rules. In archguard, not follow rule will not show data, better for governance.

APM 是在运行态发现架构问题的，ArchGuard 是运行在开发态。两者之间存在一些 gap，ArchGuard 专注于代码，更适用于通过规范来治理架构 —— 没有规范，没有数据。

License
---

@2020~2022 Thoughtworks. This code is distributed under the MPL license. See `LICENSE` in this directory.
