# PostChat - Backend

## Build
`gradlew clean build`

## Run
`java -jar .\compiled\PostChatBackend-*.jar`

### Environment Variables 
* `POSTCHAT_TEMPLATES` - Absolute path to the directory containing server's templates
* `POSTCHAT_DB_CONNECTION` - PostgreSQL database connection string
* `POSTCHAT_DB_TEST_CONNECTION` - PostgreSQL test database connection string
* `POSTCHAT_HTR` - Absolute path to the src folder containing the main python HTRs program