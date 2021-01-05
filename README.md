# kafka_learning_project

 - generator - create random numbers and send them to topic in kafka
 - reciever - receive all numbers from kafka (from generator) in ver 2 - reciever receive numbers by id in 3 partitions
 - dispatcher - receive all numbers from generator (by kafka) in ver 2 - dispatcher receive numbers by id in 3 partitions and
   work with numbers. If numbers bigger than 180 - dispatcher send them to big-numbers, if numbers less than 120 - 
   dispatcher send them to small numbers. if numbers in
   range - dispatcher receive averageNumber by Id and print it 

