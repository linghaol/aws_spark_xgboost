/*
 Copyright (c) 2014 by Contributors
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package ml.dmlc.xgboost4j.scala.example.spark

import org.apache.spark.ml.feature.{LabeledPoint => MLLabeledPoint}
import org.apache.spark.ml.linalg.{DenseVector => MLDenseVector}
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

import ml.dmlc.xgboost4j.scala.spark.XGBoost

object SparkWithRDD {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("XGBoost-spark-example")
    
    implicit val sc = new SparkContext(sparkConf)
    
    // settings
    val inputTrainPath = "s3://ee451-team-project/input/train_28w.csv"
    val inputTestPath = "s3://ee451-team-project/input/test_28w.csv"
    val outputModelPath = "s3://ee451-team-project/output/model_28w_demo"
    val outputTextPath = "s3://ee451-team-project/output/time_28w_demo"
    val outputErrorPath = "s3://ee451-team-project/output/error_28w_demo"

    // number of iterations
    val numRound = 300
    val num_workers = 2 

    // processing
    val trainCSV = sc.textFile(inputTrainPath).map(line =>line.split(",").map(_.trim.toDouble))
    val trainRDD = trainCSV.map(lp => MLLabeledPoint(lp.last, new MLDenseVector(lp.slice(0,lp.length-1))))
    
    val testCSV = sc.textFile(inputTestPath).map(line =>line.split(",").map(_.trim.toDouble))
    val testSet = testCSV.map(lp => MLLabeledPoint(lp.last, new MLDenseVector(lp.slice(0,lp.length-1))))
    

    // training parameters
    val paramMap = List(
      "eta" -> 0.1f,
      "max_depth" -> 30,
      "objective" -> "binary:logistic").toMap

    //************
    val t0 = System.nanoTime()
    val xgboostModel = XGBoost.trainWithRDD(trainRDD, paramMap, numRound, nWorkers = num_workers,
      useExternalMemory = true)
    val t1 = System.nanoTime()
    println(s"Elapsed time: ${t1 - t0} ns")

    //************

    val error = xgboostModel.eval(testSet, "28w_demo", iter=1)

    // save model to S3 path
    xgboostModel.saveModelAsHadoopFile(outputModelPath)
    sc.parallelize(Array(t1-t0)).coalesce(1).saveAsTextFile(outputTextPath)
    sc.parallelize(Array(error)).coalesce(1).saveAsTextFile(outputErrorPath)
  }
}
