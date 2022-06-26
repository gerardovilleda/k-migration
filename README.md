# K-Migration
Kafka migration

## TO-DO
Add kafka-streams to read and cache useful information like Applications

I think environment is an important setup parameter even if it defaults to "none" or "default" or "kmig".
This could help isolate other things like temporary tasks, tests, maybe undo. 
This may be one of those "Enterprise" features

Security... how to make it easy to run everywhere, on-prem or any cloud?

Need a "context" to store objects that will be used by migrations like the admin client, consumer, producer, etc. 
It'd be nice to allow custom objects to be added there. That way, custom migrations could reference them.

## Questions & Answers
If we account for Applications running migrations, what to do with migrations tha may be embedded in referenced libraries? 
Each migration is still unique and should only be executed once.

Should there be an application configured by different environments? 
In other words, multiple ApplicationInfo records (I don't like it). Still debating that...

It may be useful to create environment-specific migration topics (history, executions, approvals, etc.)

How to load custom-built migrations?
https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#appendix.executable-jar.launching
https://stackoverflow.com/questions/60323876/loader-properties-are-not-read-in-spring-boot
If that doesn't work we can always use:
java -cp k-migration-server/target/k-migration-server-0.1.0-SNAPSHOT.jar \
  -Dloader.path=sample-custom-migration/target/sample-custom-migration-1.0-SNAPSHOT.jar \
  -Dloader.main=org.eventdrivendevelopment.kafka.migration.server.KafkaMigrationServer \
  org.springframework.boot.loader.PropertiesLauncher

export LOADER_PATH=file:"C:\src\eventdrivendevelopment\k-migration\sample-custom-migration\target\sample-custom-migration-1.0-SNAPSHOT.jar"

params...

When validating and executing migrations, should I post ALL migrations at once or one migration at a time?
Pushing all at once allows validating all previously executed migrations to make sure they are accounted for and have not changed.
It could allow for some performance improvements as well.
Maybe is possible to just send file names, keys and checksums to avoid having to push the content as well. 


java -cp k-migration-server/target/k-migration-server-0.1.0-SNAPSHOT.jar  

-Dloader.path=file:/c/src/eventdrivendevelopment/k-migration/sample-custom-migration/target/sample-custom-migration-1.0-SNAPSHOT.jar -jar k-migration-server/target/k-migration-server-0.1.0-SNAPSHOT.jar