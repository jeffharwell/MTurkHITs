# MTurkHITs
Create MTurk External Questions using Groovy .. hopefully

The example_iframes.html file contains a sample that calls your external site in the same way that Amazon MTurk does. Modify the
src in the iframe and the height to match what you want to set in your own external question.

### Set Up
Copy mturk.properties.example to mturk.properties and fill in your Amazon 
access_key and secret_key.

### To Build:
gradle build

### To Run:
gradle run
