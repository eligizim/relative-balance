# relative-balance
This a samll java project to calculate relative balance of an accountId within a given time frame. 
This project consisits of one class -TransactionProcessor- which calculates the relative balance and one test class to test the different scenarios of transaction records combinations.

Due to the samll size and simplicity of the project no framework such as spring, in memory DB and etc has been used. The only dependendies used are:
Junit, log4j, apache-commons-csv which all are listed in gradle file.

##Solution Assumptions :

1- select a transaction when "createdAt" is within the time frame.
2- ignore a transaction if there is a reversal transaction related to it, even if the reversal is before of after the given time frame.
3- if there is a reversal transaction within the time frame which is related to a transaction outside of the given time frame,ignore it
4- ignore transactions with same from and to accounts
5- reversal transaction amount is not taken to the account in the solution, which means:
    if transactionAB amount is = 100$ and transactionCD is it's reversal with amount=50$, solution ignores transactionAB entirely without paying attention to the amount differences between transactionAB and it's reversal.

6- all values are in correct format, therefore no validation applied to file reading and parsing the records
7- columns are in expected order
8- the csv file has not been loaded to a DB (in memory or persistent) therefore solution streams and scans all the records to fetch the designated records
9- no dependency injection in unit tests has used. Each Test initilize the class and invokes the calculation method. 






