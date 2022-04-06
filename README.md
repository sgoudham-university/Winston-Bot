<h1 align="center">Winston | Personal Project for HTLANL</h1>

<p align="center">
    <img src="https://goudham.me/jenkins/job/sgoudham-university/job/Winston-Bot/job/main/badge/icon"/>
    <a href="https://codecov.io/gh/sgoudham-university/Winston-Bot">
    <img src="https://codecov.io/gh/sgoudham-university/Winston-Bot/branch/main/graph/badge.svg?token=Q40v3fyItO"/>
    </a>
</p>
<p align="center">
    <img src="https://img.shields.io/badge/version-v1.2.0-informational"/>
    <img src="https://img.shields.io/badge/project%20type-personal-blueviolet"/>
    <img src="https://img.shields.io/github/last-commit/sgoudham-university/Winston-Bot"/>
    <img src="https://img.shields.io/github/issues/sgoudham-university/Winston-Bot?label=issues"/>
</p>

A Powerful Discord Music Bot made using [Discord JDA](https://github.com/DV8FromTheWorld/JDA) & [Lavaplayer](https://github.com/sedmelluq/lavaplayer) themed around Winston
(From Overwatch, the hit 2016 IGN Game of The Year)

## Description

This bot was previously made for my How To Learn a New Language Course in the first year of my Software Engineering Graduate Apprenticeship.
The previous README.md can be found [here](old/README.md)

## Commands

TODO

## Installation

You can deploy an instance of winston on your own server with the steps below!

1. **First, you're going to want to `git clone` the repo as shown below**

```
git clone https://github.com/sgoudham-university/Winston-Bot
```

I've laid out 2 methods below for further installation, it's up to you which one you choose. I'd recommend method 1 for
more experienced users in the terminal and method 2 for beginner users.

### Method 1

2. **After successfully cloning the repo, run**

```
mvn package
```

**This will read the `pom.xml` file and build all the dependencies that the bot will need to run on your local machine.
This is assuming that you have [maven](https://maven.apache.org/) installed and ready to go on your computer, if not,
you can grab it [here](https://maven.apache.org/download.cgi)**

3. **Now you can run the bot through the terminal by using the command shown below (after configuring your environment properly)**

```
java -jar target/winston.jar
```

### Method 2

If you have [Intellij](https://www.jetbrains.com/idea/) installed, you can directly read in the `pom.xml` file, and it can resolve all your
dependencies for you.

2. **After successfully cloning the repo, start-up Intellij and then select the `pom.xml` and then `Open as Project`**

![](https://i.imgur.com/ypW6awm.png)

![](https://imgur.com/EedEKss.png)

## Configuration

- Open the `application.yml` file within the root directory
- To ensure the application can run, the variables inside `${}` must be set as environment variables

In order to get your bot token above, you will need to head over to
the [Discord Developers](https://discord.com/developers/applications) website and register a bot application. There are
many other tutorials out there that cover this, personally, I recommend
reading [this](https://discordpy.readthedocs.io/en/latest/discord.html) to get your bot setup. To enable the usage of 
Slash Commands, the `application.commands` scope must also be ticked. Furthermore, please visit
this [discord support article](https://support.discord.com/hc/en-us/articles/206346498-Where-can-I-find-my-User-Server-Message-ID-)
for help in obtaining your Owner ID.
