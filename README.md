# telegram-gpt-bot

The telegram-gpt-bot project is a simple integration between Telegram and OpenAI Apis, that enables you to create a personalized GPT assistant in the form of a Telegram bot. With this tool, you can access your GPT assistant from any device using your regular Telegram app, making it incredibly convenient to use. The model used is gpt-3.5-turbo, which is the same model used in regular ChatGpt.

One of the benefits of telegram-gpt-bot is the ability to configure the personality of your GPT assistant. You can choose the tone, language, specialization, and even the name of your assistant to make it feel more personalized and engaging.

The purpose of this project is to be able to have a personal bot that you can share with your friends, not to be a bot platform.

## How to use

### Prerequisites

- You must have an OpenAI Api key. [get it here](https://platform.openai.com)
- You must obtain a Telegram bot token. [talk to Botfather about it](https://t.me/botfather), ([more about Botfather here](https://t.me/botfather)) apart from the token, take note of the url of the bot.
- You need Java JRE installed (11 at least) [download from here](https://adoptium.net/es/installation/#x64_win-jre)
- You need apache maven (only if you want to build from source). [download from here](https://maven.apache.org/download.cgi)

### Instalation & startup

1. Go to [releases](https://github.com/achousa/telegram-gpt-bot/releases) and download the latest ([direct link](https://github.com/achousa/telegram-gpt-bot/releases/download/0.0.1/telegram-gpt-bot-0.0.1.zip))
2. Unzip the file on your local drive
3. Edit application.properties to include you api key and bot token, you can leave the rest as is (read the next section to know the options).
4. Execute run.sh / run.cmd (leave this window open)
5. Open up Telegram and follow the link Botfather gave you to start talking to your bot!

### Configutation

Most properties from the application properties file are self explanatory, let's go over the important ones.

| Property | Description | Mandatory |
| ----------- | ----------- | -------- |
| bot.name | Your bot's name |   Yes   |
| bot.token | The token Botfather gave you when you registered the bot |   Yes   |
| bot.presentation | A natural language command, to tell the bot how to present himself |   Yes   |
| bot.whitelist | A comma separated list of users or groups which are granted to talk to the bot. You can leave this empty if you want the bot to be accessible to everyone |   Yes   |
| openai.url | Url of the Open Ai endpoint |   Yes   |
| openai.apikey | Your open AI Api Key |   Yes   |
| openai.model | Name of the gpt-3 model (defaults to gpt-3.5-turbo) |   Yes   |
| openai.temperature | Measure of the model creativity from 0 to 1 |   Yes   |
| openai.maxtokens | Maximum number of tokens a request can consume |   Yes   |
| openai.max.message.pool.size | Number of previous messages that are kept in the context of the conversation |   Yes   |
| openai.systemprompt | This is where you tell the bot, in natural language, what to do, and how to behave |   Yes   |
| openai.example.1 | This is the first example (in role: content format) |   No   |
| openai.example.2 | This is the second example (in role: content format) |   No   |

#### Bot behaviour configuration

Basically, you configure the general behaviour with systempromt. Then you can optionaly, provide a series of example messages, showing the model how the interaction with the user and the assistant is expected to be.

Examples are optional, but if provided they must be in the "role: content" format, they also need to be suffixed with a dot and a sequential continuous number. Role must be either "user" or "assistant"

The openai.max.message.pool.size parameter, specifies the number of previous messages that are kept in memory and sent with each request. The more messages the more context the model has about the conversation but greater token consumption

```
openai.systemprompt=You are "Alfred" a helpful translator and language assistant.
openai.example.1=user: How do you say in Spanish: yesterday
openai.example.2=assistant: In Spanish, we say: ayer
openai.example.3=user: What language is this: Es ist Zeit zu essen
openai.example.4=assistant: It is German
```

You can read more about this in the [openAI api documentation](https://platform.openai.com/docs/guides/chat/introduction)

The presentation property, configures how the bot introduces itself to a new user. It's not meant to be the actual text to be said, but instructions to the bot on how the presentation must be. This way the presentation text is different each time.

```
bot.presentation=Say your name, and succinctly state your purpose. At the end offer your help in the areas you excel at.
```

#### Bot in groups

If you want to be able to add the bot to groups, there's an extra configuration step. Talk to botfather again, and enable "Allow groups" (go to /mybots -> bot settings -> Allow Groups. In the same settings menu, select "Privacy mode" and set it to disabled.

When in a group, the bot doesn't store context about the conversation. Each request from a user to the bot, is efectively considered as if it was the first interaction of the user with the bot. In this mode, the bot responds always as reply-to the user which asked. The bot does only procees messages that contains "@botname" in its body.

#### Comands

For now, available commands are the following:

| Command | Action |
| ----------- | ----------- |
| /reset | Restores conversatio context, forgets all previouse messages you sent to the bot. It can only be used in a private chat |
| /usage | Prints the sum of tokens used in all conversations. This value is not stored, and get's reset with every application restart |

## Development set-up

clone the project

`git clone https://github.com/achousa/telegram-gpt-bot`

install dependencies and build

`mvn install`

## Backlog

- Doing a better job at ACL
- Using a small datastore to store statistic, ACL and other options
- Add UI to the bot, to do things like ACL Management from the Telegram app (probably add an "owner" tag)
- Posibility for custom /commands
- Support for openAI Moderation API
