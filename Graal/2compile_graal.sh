
# this script assumes that this file was already created
# with sbt-assembly
JAR_FILE=FPToDoList-assembly-0.1.jar

cp ../target/scala-2.12/${JAR_FILE} .

# create a native image from the jar file and name
# the resulting executable `todo`
native-image --no-server -jar ${JAR_FILE} todo

