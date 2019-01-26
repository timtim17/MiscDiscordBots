package io.github.timtim17.discord.beebot

import org.javacord.api.DiscordApi
import org.javacord.api.DiscordApiBuilder
import org.javacord.api.entity.activity.ActivityType
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.entity.user.UserStatus
import kotlin.random.Random

/**
 * Setup: Create a Discord Bot API Key and set it as the value to an environmental variable called "DISCORD_TOKEN"
 * (alternative setup: create a JAR file with the key as a file in its resources named "discord_token.secret")
 *
 * Uses JavaCord. Easiest way to run is probably {@code ./gradlew run}
 *
 * @author Austin Jenchi (timtim17)
 */
object BotMain {
//    private val MAX_USER_ID: Long = 84799072321404928
    private val DISCORD_TOKEN_FILE = "discord_token.secret"
    private val TOKEN_ENV_VAR = "DISCORD_TOKEN"
    private val TOKEN: String = BotMain::class.java.getResource("/" + DISCORD_TOKEN_FILE)?.readText()
        ?: System.getenv(TOKEN_ENV_VAR)   // if neither of these exist it immediately crashes

    private val MESSAGE_CHOICES = arrayOf("bee", "2beeornot2bee", "2b||!2b", "bees", "🅱", "b", "bee", "🐝🐝🐝")

    val api: DiscordApi = DiscordApiBuilder().setToken(TOKEN).login().join()

    @JvmStatic
    fun main(args: Array<String>) {
        println("Hello world!")
        println("Starting Discord bot.")

        println("Discord API object created!")

        api.addMessageCreateListener { event ->
            val content = event.message.content
            if (!event.messageAuthor.isYourself) {
                if (content.contains("bee")) {
                    // gets a user (max) and if they're in the server dm them a bee message
                    /*
                    val user = api.getUserById(MAX_USER_ID).get()
                    if (event.messageAuthor.id == MAX_USER_ID) {
                        user.openPrivateChannel().get().sendMessage(getMessageChoice())
                    }
                    */
                    val builder = MessageBuilder()
                    event.channel.sendMessage(getMessageChoice())

                    // pings the given user -- in this case max -- if they're in the server
                    /*
                    if (event.server.get().members.contains(user)) {
                        builder.append(user)
                    }
                    */

                    builder.send(event.channel)
                }
            }
        }

        println("Added Message Create Listener")

        Thread {
            val sleepTime: Long = 2500
            while (!Thread.interrupted()) {
                api.updateStatus(UserStatus.ONLINE)
                Thread.sleep(sleepTime)
                api.updateStatus(UserStatus.IDLE)
                Thread.sleep(sleepTime)
                api.updateStatus(UserStatus.DO_NOT_DISTURB)
                Thread.sleep(sleepTime)
            }
        }.start()

        api.updateActivity(ActivityType.PLAYING, "with bees")

        Runtime.getRuntime().addShutdownHook(Thread {
            api.disconnect()
            println("Disconnected")
        })

        println("You can invite the bot by using the following url: " + api.createBotInvite())
    }

    private fun getMessageChoice(): String {
        return MESSAGE_CHOICES[Random.nextInt(MESSAGE_CHOICES.size)]
    }
}
