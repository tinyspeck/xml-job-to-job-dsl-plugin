## Generating an hpi file 

###### Ensure you have Maven 3 installed


To generate a new .hpi:
  - run `mvn install` in the terminal
  
  - if you come across a Build Failure or compilation failure you may have to rebuild the project: Build -> Rebuild Project and then run `mvn install`.
  
       <img width="1395" alt="Screen Shot 2022-10-27 at 11 31 31 AM" src="https://user-images.githubusercontent.com/112515811/198342917-e0956d14-55e4-4af1-816f-3f5a26cdef8a.png">

  - A frequent error we've come across is the following plugin error:
  
      <img width="1805" alt="Screen Shot 2022-10-27 at 11 34 04 AM" src="https://user-images.githubusercontent.com/112515811/198338397-86f23d4b-6940-4f80-b133-0db2324c542e.png">

  - Given this error, the hpi should still generate using `mvn hpi:hpi` 
  
  - If the build succeeds the .hpi will be visible within the 'target' folder
  
      <img width="233" alt="Screen Shot 2022-10-27 at 11 34 58 AM" src="https://user-images.githubusercontent.com/112515811/198340031-280b4651-6e38-417d-8763-a41478ff0af3.png"> <img width="256" alt="Screen Shot 2022-10-27 at 11 35 17 AM" src="https://user-images.githubusercontent.com/112515811/198340506-a41d1bc6-c381-484d-9277-602061f818c7.png">

