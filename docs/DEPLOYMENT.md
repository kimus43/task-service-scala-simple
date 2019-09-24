# Todo Service #

Todo service provide API for guid for create Simple Rest API with Play Framework and MongoDB.

## Build App ##

- Prerequisite
  - sbt 0.13.13
  - scala 2.11.8
  - docker > 17.05
  - mongo > 3.4.8
  
- Build step
  - sbt clean compile assembly
  - docker build
  
## Dependency Services ##

- mongodb
    - protocol: TCP
    - port: 27017
  
## Configurations ##

- Container Configuration
  - Exposed Ports *(exposed_port : internal_port)*
   - ????:8080

- App Configuration

| *Props*                                             | *Env*                                            | *Default*                                                                        |
| --------------------------------------------------- | ------------------------------------------------ | -------------------------------------------------------------------------------- |
|identityCode.timeout.second                          | CODE_TIMEOUT_SECOND                              | 300                                                                              |
|mongo.uri                                            | MONGO_URI                                        | mongodb://cluster0-shard-00-00-vfg7o.mongodb.net:27017,cluster0-shard-00-01-vfg7o.mongodb.net:27017,cluster0-shard-00-02-vfg7o.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin |
|mongo.credentials.username                           | MONGO_CREDENTIALS_USERNAME                       | admin                                                                            |
|mongo.credentials.password                           | MONGO_CREDENTIALS_PASSWORD                       | admin                                                                            |
|mongo.database                                       | MONGO_DATABASE                                   | avocado                                                                          |
|jwt.secret                                           | JWT_SECRET                                       | Some1Arbitrary3Long2String4That3No5One4Would6Be5Able7To6Guess8                   |
|jwt.tokenLifeSecond                                  | JWT_TOKEN_LIFE_SECOND                            | 3600                                                                             |

