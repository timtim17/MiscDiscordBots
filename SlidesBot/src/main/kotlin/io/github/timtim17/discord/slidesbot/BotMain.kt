package io.github.timtim17.discord.slidesbot

import org.javacord.api.DiscordApi
import org.javacord.api.DiscordApiBuilder
import org.javacord.api.entity.message.MessageBuilder
import kotlin.random.Random

/**
 * @author Austin Jenchi (timtim17)
 */
object BotMain {
//    private val ABI_USER_ID: Long = 440297287965474826
//    private val LUKAS_USER_ID: Long = 158687676587966465
    private val DISCORD_TOKEN_FILE = "discord_token.secret"
    private val TOKEN_ENV_VAR = "DISCORD_TOKEN"
    private val TOKEN: String = BotMain::class.java.getResource("/" + DISCORD_TOKEN_FILE)?.readText()
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
                val builder = MessageBuilder()
                if (content.contains("slides on my mind on my slides")) {
                    for (i in 1..5) {
                        builder.append("slides on my mind on my slides\n")
                    }

            //                        val abiUser = api.getUserById(ABI_USER_ID).get()
            //                        if (event.server.get().members.contains(abiUser)) {
            //                            builder.append(abiUser)
            //                        }
            //
            //                        val lukasUser = api.getUserById(LUKAS_USER_ID).get()
            //                        if (event.server.get().members.contains(lukasUser)) {
            //                            builder.append(lukasUser)
            //                        }
                } else {
                    if (content.contains("slides")) builder.append("on my mind\n")
                    if (content.contains("my mind")) builder.append("on my slides\n")
                }
                builder.send(event.channel)
            }
        }

        println("Added Message Create Listener")

        println("You can invite the bot by using the following url: " + api.createBotInvite())
    }
}
