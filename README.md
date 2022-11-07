# Cron expression description

Basic maven java application with junit test setup for human-readable representation of cron expressions.

Given an expression of form

<pre>
${minute} ${hour} ${day of month} ${month} ${day of week} ${command}
</pre>

generates output as

<pre>
minute        ${minute}
hour          ${hour}
day of month  ${day of month}
month         ${day of month}
day of week   ${day of week}
command       ${command}

</pre>
where 1st column is of 14 space width

For ex: 
<pre>
Input:
*/15 0 1,15 * 1-5 /usr/bin/find

Output:
minute        0 15 30 45
hour          0
day of month  1 15
month         1 2 3 4 5 6 7 8 9 10 11 12
day of week   1 2 3 4 5
command       /usr/bin/find
</pre>

Acceptable input ranges:

<pre>
minute        0-59
hour          0-23
day of month  1-31
month         1-12
day of week   0-6
command       Any String

Supported special characters: '*', '*/', ',', '-' 
</pre>

Any alphabetical character/string is not acceptable input except for command and exception is thrown for the same.

<pre>
How to run application:

java -jar ${applicationName}.jar "*/15 0 1,15 * 1-5 /usr/bin/find"

</pre>