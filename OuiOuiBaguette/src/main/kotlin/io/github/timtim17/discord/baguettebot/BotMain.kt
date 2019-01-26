package io.github.timtim17.discord.baguettebot

import org.javacord.api.DiscordApi
import org.javacord.api.DiscordApiBuilder
import org.javacord.api.entity.message.MessageBuilder
import kotlin.random.Random

/**
 * @author Austin Jenchi (timtim17)
 */
object BotMain {
    private val BAGUETTE_GREMLIN_ID: Long = 130467300745674754
    private val SHULK_ID: Long = 351197640584724480
    private val DISCORD_TOKEN_FILE = "discord_token.secret"
    private val TOKEN_ENV_VAR = "DISCORD_TOKEN"
    private val TOKEN: String = BotMain::class.java.getResource("/" + this.DISCORD_TOKEN_FILE)?.readText()
        ?: System.getenv(TOKEN_ENV_VAR)   // if neither of these exist it immediately crashes

    val api: DiscordApi = DiscordApiBuilder().setToken(TOKEN).login().join()

    @JvmStatic
    fun main(args: Array<String>) {
        println("Hello world!")
        println("Starting Discord bot.")

        println("Discord API object created!")

        api.addMessageCreateListener { event ->
            val content = event.message.content
            if (!event.messageAuthor.isYourself) {
                if (content.contains("baguette") || event.messageAuthor.id == BAGUETTE_GREMLIN_ID) {
                    val user = api.getUserById(BAGUETTE_GREMLIN_ID).get()
                    user.openPrivateChannel().get().sendMessage("Oui Oui Baguette")
                    val builder = MessageBuilder()
                    event.channel.sendMessage("Oui Oui Baguette")
                    if (event.server.get().members.contains(user)) {
                        builder.append(user)
                    }
                    builder.send(event.channel)
                }

                if (event.messageAuthor.id == SHULK_ID) {
                    event.channel.sendMessage("down 🅱")
                    val dm = event.messageAuthor.asUser().get().openPrivateChannel().get()
                    for (i in 1..10) {
                        if (Random.nextBoolean()) {
                            dm.sendMessage("down 🅱")
                        } else {
                            dm.sendMessage("down 🐝")
                        }
                    }
                }
            }
        }

        println("Added Message Create Listener")

        println("You can invite the bot by using the following url: " + api.createBotInvite())
    }
}
