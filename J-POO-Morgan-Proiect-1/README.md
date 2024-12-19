# J POO Morgan Assignment 1

	The aim of this project is to implement a banking app with various functionalities. The implementation I have thought out works as follows:
## The Input class
	The input class is basically the backbone of the program. It handles not only the processing of input but it also does the execution of commands and generation of transactions history as well as saving it. It does so by using functions that process each part of the input, as well as two visitors that execute commands, respectively generate and save transaction history where the commands require, which brings us to the next part.
## The Command class and its children
	The command class and its children represent each individual command that the app supports. They contain functions that help most of the functions in the visitor classes, as well as the required variables for each command in particular.
## The user package
	The user package contains the classes that represent objects that relate to the user in any way, such as the Account, Card, Client, Transactions, etc.
## The visitor patterns
	The ExecuteVisitor basically puts each command in action and also handles corner cases, then enacts the effects of the commands on the Arraylist of clients that is passed through reference to each command object. Where it is needed, it also outputs errors.
	The History visitor handles solely the generation of transactions and then stores them in their corresponding accounts. It also handles the printTransaction command. 
