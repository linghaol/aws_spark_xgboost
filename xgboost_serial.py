"""
Created on Sat April 7, 2018

@author: LLH
"""

# EE451 Project xgboost serial


import numpy as np
import xgboost as xgb
from datetime import datetime

# read dataset.csv as numpy array
train_path = '/home/hadoop/aws_spark_xgboost/train_7w.csv'
test_path = '/home/hadoop/aws_spark_xgboost/test_7w.csv'
traindata = np.genfromtxt(train_path, delimiter=',')
testdata = np.genfromtxt(test_path, delimiter=',')


# set training/testing X and Y
train_x = traindata[:, :-1]
train_y = traindata[:, -1]
test_x = testdata[:, :-1]
test_y = testdata[:, -1]

dtrain = xgb.DMatrix(train_x, label=train_y)
dtest = xgb.DMatrix(test_x, label=test_y)


# define model
param = {'booster': 'gbtree', 
         'max_depth': 30, 
		 'eta': 0.1, 
		 'silent': 1, 
		 'objective': 'binary:logistic', 
		 'eval_metric': 'error'}

num_round = 100


# train model & record training time
t1 = datetime.now()

model = xgb.train(param, dtrain, num_round)

t2 = datetime.now()


# evaluate

error = model.eval(dtest, '7w')

# evaluate
print('Training time: %d \n' % (t2-t1).seconds)
print(error)










