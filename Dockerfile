FROM java:openjdk-8-jre

ENV APP_HOME "/opt/app"

ADD build/libs/*.jar $APP_HOME/persons.jar

CMD	java -Xmx1024M -Xms512M -Dspring.profigotles.active=local -jar /opt/app/persons.jar
