# Documentation
In the documentation directory you will find some sequence diagrams. Great tip, if you don't have the plantuml plugin in your intellij, just copy the raw puml file and paste it in the plantuml web server (https://www.plantuml.com/plantuml/uml/). Off-course this is only for non-confidential information.

# Start up
To start the application simply run the `TransactionServiceApplication` and after that go to `http://localhost:8080`

# Choices being made
## One test to rule them all
I used only one 'integration' test. This test will test the whole application. My vision on testing strategy is that every situation asked for its own approach and in general I do like to test bigger logically combined parts together and only use unit tests to test when there is a need for it. You can think of a very complex part of logic having multiple if's, switches or other complexity. 

## Usage of thymeleaf as frontend
As I showed in my 2021 JFall talk (https://www.youtube.com/watch?v=nLGpAqhylE4), Thymeleaf is a great way to deliver value quickly. It does have its usages, like a simple application as this. Off-course, it is not always the best choice :-).   

## Clean code
I do like to keep my code as clean as possible. That is why I use Lombok for example. But also the new records come in handy. Clean code means something different for all of us, but having a talk about it is something very important where all developers benefit from!

# Assumptions being made
## Login gives accountId
I didn't do security, see below for more information. Normally you would need to authenticate to perform a transaction. For now, to show functionality I added a screen where you can fill in from and to account. I totally agree this is not a real world example, and it is to showcase my thought process.

## Minimum account amount is 0
You could have thought about 'rood staan', but I decided for  now that 0 was the minimum. This also means that if you want to transfer more money then you have, you will get an insufficient funds result.

## Accounts can't have the same name and email address
I made the assumption that if you try to add a new account with an existing name and email combination, you would get an error. So That is what I build in. For now case sensitive and not fuzzy. 

# What I still wanted to do
I wanted to restrict myself to the four hours that this challenge should take. There is always more to do and I decided to put some of the work lower on my todo list. I will explain quickly why I chose to put them lower. 

## Backend validation
This is really important. The only reason I left this behind is the fact that there is already frontend validation and I don't have enough time to do both. 
Normally I would use @Valid on my endpoints. 

## Security
It just would take me too much time to implement security while it wouldn't show you the style of programmer I am.

## Styling
You will need to look through the ugly styling a bit. Off-course this is important but If I need to choose between a little bit less beautiful, but working application or a beautiful non-working application, my choice would be the first one. 
