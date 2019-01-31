Access Log Analyzer
=================================

A analyzer for Nginx's logs to expose metrics:
  + Page views
  + Unique visitors

Prepare
=================================
- Java OpenJDK 1.8 or higher
- Scala 2.11 or higher
- Sbt

Installation
=================================
- Apache Spark & Hadoop:
```bash
$ wget https://archive.apache.org/dist/spark/spark-2.4.0/spark-2.4.0-bin-hadoop2.6.tgz
$ tar -xvf spark-2.4.0-bin-hadoop2.6.tgz -C /usr/local/spark
```
```bash
$ nano ~/.bashrc

# Add below code snippet to the bash file
SPARK_HOME=/usr/local/spark
export PATH=$SPARK_HOME/bin:$PATH
```
```bash
# Execute below command after editing the bashsrc
$ source ~/.bashrc
```
- Verify the installation:
```bash
$ cd /usr/local/spark/bin
$ ./spark-shell
# Make sure below screen is displayed
==========================================
Welcome to
      ____              __
     / __/__  ___ _____/ /__
    _\ \/ _ \/ _ `/ __/  '_/
   /___/ .__/\_,_/_/ /_/\_\   version 2.4.0
      /_/
         
Using Scala version 2.11.12 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_121)
Type in expressions to have them evaluated.
Type :help for more information.

scala>
```

Run application:
=================================
```bash
$ sbt "runMain com.likecms.analytics.accesslog.AccessLogAnalyzer /absolute/path/to/access/log/dir/"
# Output:
Total page views: xxxx
Total unique visitors: xxxx
```
eg:
```bash
# specify file path:
$ sbt "runMain com.likecms.analytics.accesslog.AccessLogAnalyzer /Library/Logs/nginx/error.log"

# or specify dir path for multi file:
$ sbt "runMain com.likecms.analytics.accesslog.AccessLogAnalyzer /Library/Logs/nginx/"
```
