## Maven Pratice

   _All pratice of maven_

 > Maven release plugin

 ```xml
   <scm>
       <developerConnection>scm:svn:https://svn.mycompany.com/repos/myapplication/trunk/mycomponent/</developerConnection>
   </scm>
 ```

 ```shell
    1. mvn release:prepare -DdryRun=true -Dresume=false

    2. mvn release:clean

    3. mvn --batch-mode release:prepare

    4. mvn release:rollback

 ```