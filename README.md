# Introduction 
S4 Integration Gateway Application 

# Author / Contribute
- [Kevin]

# Getting Started
TODO: Guide users through getting your code up and running on their own system. In this section you can talk about:
1.  Installation process
2.  Software dependencies
3.  Latest releases
4.  API references

# Build and Test
TODO:
- Move mock data from './srv/src/main/resources/application-data' to './db/data' to have the data to test
- Comment @cds.persistence.exists annotation in administrator
- Remove file default-env.json in project if existing

# Run Application (Service)
- Run app with 'default' profile (dev): mvn clean spring-boot:run
- Run app with 'cloud' profile (production): mvn clean spring-boot:run -Dspring-boot.run.profiles=cloud

# Deployment
- Init step: just put undeploy.json file when you really want reset your schema structure. when everything fine, please remove or move it to another place (backup file directory for example)
- Step 0: Uncomment annotation '@cds.persistence.exists' in administrator.cds, remove data folder (mock data) in db, or move it into /resources/application-data folder
- Step 1(optional if missing some dependency): npm install
- Step 2(build and package project): mbt build
- Step 3(Deploy archive file to cloud): cf deploy mta_archives/[mta file]

# Undeploy
- Step 1(Show mta project in CF): cf mtas
- Step 2(Undeploy mta project by id): cf undeploy [mta id] --delete-services --delete-service-keys --delete-service-brokers

# Git logs information
- git log --graph --oneline (show git commit as graph)

# Note before release
- Check all hard code data (if exist)
- Change IDP connect information to adopt with new sub account


# CAP / CDS Feature usages in source code
* Building CQL Statement: https://cap.cloud.sap/docs/java/query-api
    Example:
    + ![Matching Search](assets/matching-search.png) 


# Documentations Logic
![Risk Score Source System](./assets/riskscoresourcesystem.png)



# Fix CVEs Privileges in SAP Business Technology Platform (BTP) Security Services Integration Libraries
* Link: https://me.sap.com/notes/3411067

1. Version of com.sap.cloud.security must >= 3.3.0 or >= 2.17.0
    - Current 2.13.0 -- update to --> 2.17.0 (The newest is 3.3.2)
2. Sap Java Build Pack (SJB) must >= 1.81.1
    - Current SJB is 1.82.0
    * Note: we can check the SJB following procedure:
    + Step 1: Login to SAP BTP
    + Step 2: command: cf app <your app name>
    + Step 3: See the output log, SJB will show you the version

3. SAP Cloud SDK for Java must equal or bigger 4.28.0 -> Solution: up to 5.0.0
    - Current: 4.8.0
    * Link: https://sap.github.io/cloud-sdk/docs/java/overview-cloud-sdk-for-java

4. Cloud Application Programming Model (CAP) JAVA V2 must >= 2.4.1


# SAP BTP API Endpoint
- PayAsYouGo: https://api.cf.eu10.hana.ondemand.com
- PRODUCTION: https://api.cf.eu10-004.hana.ondemand.com
# SAP BTP Deployment Account:
- sapbtp.deployer@conarum.com / COnarum@01++
# Cloud Foundry CommandLines
- Login: cf login -a <Api_Endpoint>
- List all mta applications: cf mtas




adapter-module/
 ├── config/
 │    ├── ClientConfig.java
 │    ├── PackageConfig.java
 │
 ├── core/
 │    ├── DispatcherService.java   # route request đến package handler
 │    ├── RequestContext.java
 │
 ├── packages/
 │    ├── sa/
 │    │    ├── SAHandler.java
 │    │    ├── SAHandlerBrose.java
 │    │    ├── SAHandlerStabilo.java
 │    ├── poc/
 │    │    ├── POCHandler.java
 │    │    ├── POCHandlerManroland.java
 │    └── ...
 │
 ├── connectivity/
 │    ├── SAPConnectivityClient.java
 │    ├── SecurityManager.java
 │
 ├── controller/
 │    ├── AdapterController.java
 │
 ├── test/
 │    └── packages/...