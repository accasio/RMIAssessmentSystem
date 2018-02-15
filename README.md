# RMIAssessmentSystem

SERVER

javac ct414/ExamServer.java ct414/Assessment.java ct414/Question.java

jar cvf runner.jar ct414\ExamServer.class ct414/Assessment.class ct414/Question.class

javac -cp runner.jar ct414\ExamEngine.java ct414\InvalidOptionNumber.java ct414\InvalidQuestionNumber.java ct414\NoMatchingAssessment.java ct414\UnauthorizedAccess.java

start rmiregistry

java -cp .;.\runner.jar -Djava.rmi.server.codebase=file:./runner.jar -Djava.rmi.server.hostname=localhost -Djava.security.policy=global.policy ct414.ExamEngine


Client

javac -cp runner.jar ct414\Client.java ct414\UnauthorizedAccess.java ct414\NoMatchingAssessment.java ct414\InvalidQuestionNumber.java ct414\InvalidOptionNumber.java

java -cp .;.\runner.jar -Djava.rmi.server.codebase=file:/C:/Users/accas/documents/RMIAssessmentSystem/src/ -Djava.security.policy=global.policy ct414.Client
