# Winston | Personal Project for HTLANL

![version-shield](https://img.shields.io/badge/version-v1.1.2-informational)
![project-type-shield](https://img.shields.io/badge/project%20type-personal-blueviolet)
![last-commit-shield](https://img.shields.io/github/last-commit/sgoudham-university/Winston-Bot)
![issues-shield](https://img.shields.io/github/issues/sgoudham-university/Winston-Bot?label=issues)

Creating a Discord Bot within [Discord JDA](https://github.com/DV8FromTheWorld/JDA) For Real-Time Player Statistics in Overwatch.

## Table of Contents

- [Description](#Description)
- [Functionality](#Functionality)
    - [Commands](#Commands)
    - [Testing](#Testing)
    - [Frameworks / Libraries](#frameworks--libraries)
    - [Misc](#Misc)
- [Installation](#Installation)
    - [Method 1](#Method-1)
    - [Method 2](#Method-2)
- [Configuration](#Configuration)
- [API Issues](#API-Issues)
- [Services / Libraries Used](#services--libraries-used)

## Description

This project is part of my How To Learn a New Language Course. I decided on another discord bot but in a different language to expose myself
to new languages and frameworks. Languages and frameworks that I will be using in the workplace.

## Functionality

Listed below are the tasks that I will set out to complete:

#### Commands

- [x] Set bot activity to "Playing Overwatch"
- [x] Display nicely formatted embeds that include Player Information (Includes Competitive and Achievements)
- [x] Display nicely formatted embeds for Overwatch Hero Information
- [x] Implement a help command that lists all the commands

#### Testing

- [x] Use JUnit to successfully test
- [x] Use Mockito to successfully Mock Resources and test

#### Frameworks / Libraries

- [x] Use JDA to connect to the Discord API
- [x] Set up Logging for any commands / errors thrown
- [x] Use Maven as a package manager to build, package and release
- [x] Use Jackson to successfully match JSON -> POJO

#### Misc

- [x] Host bot on Virtual Private Server (Running Ubuntu 20.04) for 24/7 uptime
- [x] Integrate Kotlin with Java
- [x] Make new CustomException Classes to handle exceptions
- [x] Configuration file to store Environment variables
- [ ] ~~Include Asynchronous Programming Methods for API retrieval~~

## Installation

You can deploy an instance of winston on your own server with the steps below! 
1. **First of all, you're going to want to `git clone` the repo as shown below**
```
git clone https://github.com/sgoudham-university/winston-Bot.git
```

### Method 1

2. **After successfully cloning the repo, run** 
```
mvn package
```
**This will read the `pom.xml` file and build all the dependencies that the bot will need to run on your local machine. 
This is assuming that you have [maven](https://maven.apache.org/) installed and ready to go on your computer, if not, you can grab it [here](https://maven.apache.org/download.cgi)**

3. **Now you can run the bot through the terminal by using the command shown below. However, you will have to replace the VERSION with the version number that you chose to download**
```
java -jar target/Winston-Bot-VERSION-jar-with-dependencies.jar
```

### Method 2

If you have [Intellij]() installed, you can directly read in the `pom.xml` file, and it can resolve all your dependencies for you. 

2. **After successfully cloning the repo, start-up Intellij and then select the `pom.xml` and then `Open as Project`**

![](https://i.imgur.com/ypW6awm.png)

![](https://imgur.com/EedEKss.png)

## Configuration

Create an `.env` file in your root directory and fill in the fields with your respective values

```.env
TOKEN=<Your_Bot_Token_Here>
OWNER_ID=<Your_Owner_ID_Here>
PREFIX=<Bot_Prefix_here>
```

This will allow the bot to boot up successfully, at the moment, changeable prefixes are not a feature in the bot itself but 
may be at a later time. The only way to change the prefix is through hosting the bot locally yourself and changing it as shown above.

## API Issues

- Disappointingly, Blizzard does not have an official API for Overwatch (I have no idea why they don't) which results in a massive headache in using
  API's to retrieve data as they are all web-scraped and can take long to update, not to mention the extremely long response times. 
- Due to this, the bot isn't as snappy and fast and only retrieves a limited amount of information.
- I've tried my best to hack together more information but as stated above, this makes the bot quite slow in retrieving requests

## Services / Libraries Used

- [Discord JDA](https://github.com/DV8FromTheWorld/JDA)
- [Discord Pagination Utils](https://github.com/ygimenez/Pagination-Utils)
- [HTTPClient](https://hc.apache.org/)
- [Swagger Overwatch API](https://swagger-owapi.tekrop.fr/)
- [Jackson](https://github.com/FasterXML/jackson)
- [Dotenv](https://github.com/cdimascio/dotenv-java)
- [JUnit 5](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)
