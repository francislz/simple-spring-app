# Fullstack Technical Assessment
A take home assessment designed for Full-stack or Backend developers
## Best matched restaurants
### Introduction
This assessment is designed to test your thinking process and coding skills when facing a real industry problem. We will assess you based on the requirements listed in the problem description below. Please note that we're looking for code that is clean, readable, testable, robust, performant, and maintainable. If you continue to our interview panel stage, we will ask you questions about your implementation.

Since this assessment includes a searching function, we kindly ask you to avoid out of box search engines such as ElasticSearch. Instead, you should write the searching logic by yourself. Also, please choose one of the below languages
- Java
- Kotlin
- JavaScript
- Ruby

and any frameworks you are familiar with to complete the assessment. We are focusing on your idea and your code quality, hence will not make judgments on which technologies you choose. All the data you will need in this assessment will be provided to you as **.csv** files.

When you are done, please return the task by email. We expect to receive your response within 5 days after you receive this assessment.

*Note: We expect you to do this assessment by yourself without other people's help. We ask you to keep this assessment private and not to share it with others. All data provided are fake and used only for this assessment.*

### Find the best-matched restaurants
You have data about local restaurants located near your company, which you can find in the **restaurants.csv** file. You would like to develop a basic search function that allows your colleagues to search those restaurants to help them find where they would like to have lunch. The search is based on five criteria: **Restaurant Name, Customer Rating(1 star ~ 5 stars), Distance(1 mile ~ 10 miles), Price(how much one person will spend on average, $10 ~ $50), Cuisine(Chinese, American, Thai, etc.).** The requirements are listed below.

1. The function should allow users to provide up to five parameters based on the criteria listed above. *You can assume each parameter can contain only one value.*
2. If parameter values are invalid, return an error message.
3. The function should return up to five matches based on the provided criteria. If no matches are found, return an empty list. If less than 5 matches are found, return them all. If more than 5 matches are found, return the best 5 matches. The returned results should be sorted according to the rules explained below. Every record in the search results should at least contain the restaurant name.
4. “Best match” is defined as below:
   - A Restaurant Name match is defined as an exact or partial String match with what users provided. For example, “Mcd” would match “Mcdonald’s”.
   - A Customer Rating match is defined as a Customer Rating equal to or more than what users have asked for. For example, “3” would match all the 3 stars restaurants plus all the 4 stars and 5 stars restaurants.
   - A Distance match is defined as a Distance equal to or less than what users have asked for. For example, “2” would match any distance that is equal to or less than 2 miles from your company.
   - A Price match is defined as a Price equal to or less than what users have asked for. For example, “15” would match any price that is equal to or less than $15 per person.
   - A Cuisine match is defined as an exact or partial String match with what users provided. For example, “Chi” would match “Chinese”. You can find all the possible Cuisines in the **cuisines.csv** file. *You can assume each restaurant offers only one cuisine.*
   - The five parameters are holding an “AND” relationship. For example, if users provide Name = “Mcdonald’s” and Distance = 2, you should find all “Mcdonald’s” within 2 miles.
   - When multiple matches are found, you should sort them as described below.
     - Sort the restaurants by Distance first.
     - After the above process, if two matches are still equal, then the restaurant with a higher customer rating wins.
     - After the above process, if two matches are still equal, then the restaurant with a lower price wins.
     - After the above process, if two matches are still equal, then you can randomly decide the order.
     - Example: if the input is Customer Rating = 3 and Price = 15. Mcdonald’s is 4 stars with an average spend = $10, and it is 1 mile away. And KFC is 3 stars with an average spend = $8, and it is 1 mile away. Then we should consider Mcdonald’s as a better match than KFC. (They both matches the search criteria -> we compare distance -> we get a tie -> we then compare customer rating -> Mcdonald’s wins)
5. The final submitted work should include a README file. No UI is required in this assessment, but you may implement one if you would like. **The steps to run and test your program should be clearly introduced in the README file.** If you have made any additional **Assumptions** besides what we have listed above while working on this assessment, please document them so that we can better understand your solution.

### Evaluation
Your work will be evaluated with the following criteria:

- Functionality: the app fulfills the requirements, with no major bugs.
- Maintainability: the code is clean, extensible, and easy to work with.
- Usability: the user will be able to easily use your program to get the desired results, with minimal or no instruction.

## Plans

- [x] Setup project (Gradle, SpringBoot)
- [x] Add swagger for API testing and usage
- [x] Implement logic to read data from the csv files
- [x] Handle query parameters validation
- [x] Implement generic search functionality
- [x] Implement the scenario to handle the first assumption
- [x] Implement best match using the search algorithm and the parameters
    - Option 1: Simple search on a list using string matching (Less efficient, but we don´t have much data so not an issue)
    - Option 2: Create a Data Structure such as a suffix tree to optimize the search to support scalability (better search but can be memory intensive if the data scales a lot)
    - Option 3: Load everything into a SQLite, index the columns used in the search and let the DB handle the search
    - Option 4: Group the data using a Hash and do a linear search on the group, this allows us to avoid search the entire dataset in this particular case.
- [x] Implement sort logic

NOTE: For the search algorithm I used a combination of a HashMap and a TreeMap. To match the cuisines I create a HashMap that mapped the cuisine Id to a list of restaurants. This way by getting the cuisine Id I was able to get all the restaurants that provide that cuisine in O(1). To handle the range searches (distance and price) I used a tree map that is basically a balanced binary tree.

## Assumptions

1. Calling the search endpoint without any of the query parameters should return the top five restaurants following the sorting criteria.

2. All the inputs validation on the SearchController were created based on the ranges defined on the requirements: **Restaurant Name, Customer Rating(1 star ~ 5 stars), Distance(1 mile ~ 10 miles), Price(how much one person will spend on average, $10 ~ $50), Cuisine(Chinese, American, Thai, etc.).**

## Other notes
- I added a CI pipeline using github actions to run the unit tests
- I added a docker file to facilitate running this app in all the platforms.
- In case this application scales I move the data to a DB and use elastic search to optimize the search.
- I added some unit tests for the controller and the service
- I used reflection to make a generic class able to read both of the CSVs

## Runing this application


### Option 1: Docker container

Running this application using docker is pretty simple and straight forward, to do it simple run on the terminal:

```sh
docker build -t spring-app .
docker run -p 8080:8080 spring-app
```

### Option 2: Using gradle

- Ensure you have gradle 8.x installed: https://gradle.org/install/
- Run the commands on the terminal:
```sh
gradle clean
gradle bootJar
java -jar build/libs/bestmatch-0.0.1-SNAPSHOT.jar
```

### Testing the search

- For both options after the application has started just access
  * http://localhost:8080/swagger-ui/index.html#/Search%20Restaurants/search

- Click on the "Try it out" button
- Provide the search information you would like
  * If you would like the top 5 best restaurants just let the fields empty
- Click on the execute button
- Top 5 restaurants should be on the response body if available