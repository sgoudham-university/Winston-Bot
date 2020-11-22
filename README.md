# Winston | Personal Project for HTLANL

![version-shield](https://img.shields.io/badge/version-v0.1--alpha-informational)
![project-type-shield](https://img.shields.io/badge/project%20type-personal-blueviolet)
![last-commit-shield](https://img.shields.io/github/last-commit/sgoudham-university/Winston-Bot)
![issues-shield](https://img.shields.io/github/issues/sgoudham-university/Winston-Bot?label=issues)

Creating a Discord Bot within [Discord JDA](https://github.com/DV8FromTheWorld/JDA) For Real-Time Player Statistics in Overwatch.

## Table of Contents

- [Description](#Description)
- [Functionality](#Functionality)
    - [Commands](#Commands)
    - [Testing](#Testing)
    - [Frameworks / Libraries](#Frameworks-/-Libraries)
    - [Misc](#Misc)
- [API Issues I encountered](#API-Issues)

## Description

This project is part of my How To Learn a New Language Course. I decided on another discord bot but in a different language to expose myself
to new languages and frameworks. Languages and frameworks that I will be using in the workplace.

## Functionality

Listed below are the tasks that I will set out to complete:

#### Commands

- [x] Set bot activity to "Playing Overwatch"
- [x] Display nicely formatted embeds that include Player Information
- [ ] Display nicely formatted embeds for Overwatch Hero Information
- [ ] Implement a help command that lists all the commands

#### Testing

- [x] Use JUnit to successfully test
- [x] Use Mockito to successfully Mock and test

#### Frameworks / Libraries

- [x] Use JDA to connect to the Discord API
- [x] Set up Logging for any commands / errors thrown
- [x] Use Maven as a package manager to build, package and release
- [x] Use Jackson to successfully match JSON -> POJO

#### Misc

- [x] Host bot on Virtual Private Server (Running Ubuntu 20.04) for 24/7 uptime
- [x] Configuration file to store Environment variables
- [ ] Include Asynchronous Programming Methods for API retrieval

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
