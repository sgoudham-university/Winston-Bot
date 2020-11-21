# Winston | Personal Project for HTLANL

![version-shield](https://img.shields.io/badge/version-v1.0.1-informational)
![project-type-shield](https://img.shields.io/badge/project%20type-personal-blueviolet)
![last-commit-shield](https://img.shields.io/github/last-commit/sgoudham-university/Winston-Bot)
![issues-shield](https://img.shields.io/github/issues/sgoudham-university/Winston-Bot?label=issues)

Creating a Discord Bot within [Discord JDA](https://github.com/DV8FromTheWorld/JDA) For Real-Time Player Statistics in Overwatch.
- [Read Here For API Issues I encountered](#API-Issues)

## Description

This project is part of my How To Learn a New Language Course. I decided on another discord bot but in a different language to expose myself
to new languages and frameworks. Languages and frameworks that I will be using in the workplace.

## API Issues

- Disappointingly, Blizzard does not have an official API for Overwatch (I have no idea why they don't) which results in a massive headache in using
  API's to retrieve data as they are all web-scraped and can take long to update, not to mention the extremely long response times. 
- Due to this, the bot isn't as snappy and fast and only retrieves a limited amount of information.

## Functionality

Listed below are the tasks that I will set out to complete:

#### Discord

- [x] Configuration file to store Environment variables
- [x] Set up Logging for any commands / errors thrown
- [x] Set bot activity to "Playing Overwatch"
- [x] Display nicely formatted embeds that include Player Information
- [ ] Display nicely formatted embeds for Overwatch Hero Information

#### Testing

- [x] Use JUnit to successfully test
- [x] Use Mockito to successfully Mock and test

#### Frameworks / Libraries

- [x] Use JDA to connect to the Discord API
- [x] Use Maven as a package manager to build, package and release
- [x] Use Jackson to successfully match JSON -> POJO

#### Misc

- [x] Host bot on Virtual Private Server (Running Ubuntu 20.04) for 24/7 uptime


## Services / Libraries Used

- [Discord JDA](https://github.com/DV8FromTheWorld/JDA)
- [Swagger Overwatch API](https://swagger-owapi.tekrop.fr/)
- [Jackson](https://github.com/FasterXML/jackson)
- [Dotenv](https://github.com/cdimascio/dotenv-java)
- [JUnit 5](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)
