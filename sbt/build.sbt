
val xgboostSparkPath = "file://home/hadoop/aws_spark_xgboost/sbt/lib/xgboost4j-spark-0.72-jar-with-dependencies.jar"

val xgboostPath = "file://home/hadoop/aws_spark_xgboost/sbt/lib/xgboost4j-0.72-jar-with-dependencies.jar"

val xgboostExamplePath = "file://home/hadoop/aws_spark_xgboost/sbt/lib/xgboost4j-example-0.72-jar-with-dependencies.jar"


name := "Spark XGBoost"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % "2.3.0" ,
  "org.apache.spark" %% "spark-mllib" % "2.3.0",
  "ml.dmlc" % "xgboost4j" % "0.72" % "provided" from xgboostPath,
  "ml.dmlc" % "xgboost4j-spark" % "0.72" % "provided" from xgboostSparkPath,
  "ml.dmlc" % "xgboost4j-example" % "0.72" % "provided" from xgboostExamplePath
)

