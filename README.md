# telegram-gpt-bot

The telegram-gpt-bot project is a simple integration between Telegram and OpenAI Apis, that enables you to create a personalized GPT assistant in the form of a Telegram bot. With this tool, you can access your GPT assistant from any device using your regular Telegram app, making it incredibly convenient to use.

One of the benefits of telegram-gpt-bot is the ability to configure the personality of your GPT assistant. You can choose the tone, language, specialization, and even the name of your assistant to make it feel more personalized and engaging.

## How to use

### Prerequisites

- You must have an OpenAI Api key. [get it here](https://platform.openai.com)
- You must obtain a Telegram bot token. [talk to Botfather about it](https://t.me/botfather), [more about it here](https://t.me/botfather)
- You need Java JRE installed (11 at least) [download from here](https://adoptium.net/es/installation/#x64_win-jre)
- You need apache maven (only if you want to build from source). [download from here](https://maven.apache.org/download.cgi)

### Instalation & startup

1. Go to releases and download the latest
2. Unzip the file on your local drive
3. Edit application.properties to include you api key and bot token, you can leave the rest as is.
4. Execute run.sh / run.cmd
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
| openai.model | Name of the gpt-3 model |   Yes   |
| openai.temperature | Measure of the model creativity from 0 to 1 |   Yes   |
| openai.maxtokens | Maximum number of tokens a request can consume |   Yes   |
| openai.max.message.pool.size | Number of previous messages that are kept in the context of the conversation |   Yes   |
| openai.systemprompt | This is where you tell the bot, in natural language, what to do, and how to behave |   Yes   |
| openai.example.1 | This is the first example (in role: content format) |   No   |
| openai.example.2 | This is the second example (in role: content format) |   No   |

#### About examples

Examples are aditional prompts that will help the bot to know how answers should look like. You can read more about this in the [openAI api docs](https://platform.openai.com/docs/guides/chat/introduction)

```
openai.systemprompt=You are \"Alfred\" a helpful translator and language assistant.
openai.example.1=user: How do you say in Spanish: yesterday
openai.example.2=assistant: In Spanish, we say: ayer
openai.example.3=user: What language is this: Es ist Zeit zu essen
openai.example.4=assistant: It is German
```

Examples are optional, but if provided they must be in the "role: content" format, they also need to be suffixed with a dot and a sequential continuous number.

Note: if you want to include quotes in your prompts


## Development set-up

clone the project

`git clone https://github.com/achousa/telegram-gpt-bot`

install dependencies and build

`mvn install`

