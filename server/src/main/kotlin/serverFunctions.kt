package utils

import com.beust.klaxon.Klaxon
import io.javalin.Context
import utils.utils.Question
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import kotlin.math.min
import utils.utils.QuestionList
import java.io.IOException
import java.lang.Error
import java.util.*

fun accueil(ctx: Context) {
}

fun forbidden(ctx: Context) {
    ctx.result("THIS IS FORBIDDEN !")
    //²ctx.status(403)
}

fun subscribeOrConnect(ctx: Context) {
    try {
        var conn: Connection? = DriverManager.getConnection(utils.BDD_URL)

        if (conn != null) {
            var regex = "^[a-zA-Z0-9_]+$".toRegex()
            val pseudo = ctx.pathParam("pseudo")

            var exists : Boolean?
            var selectRequest = "SELECT * FROM users WHERE pseudo = $pseudo"
            val select = conn.createStatement()
            var result : ResultSet = select.executeQuery(selectRequest)

            if(!result.wasNull()){
                if(regex.matches(pseudo)) {

                    var insertRequest = "INSERT OR IGNORE INTO users(pseudo) VALUES($pseudo)"
                    val insert = conn.prepareStatement(insertRequest)
                    insert.executeUpdate()
                    conn.close()

                } else {
                    //WRONG PSEUDO
                }
            } else {
                conn.close()
                //ALREADY EXISTS
            }
        } else {
            //CONNECTION FAILED
        }
    } catch(e : Exception) {
        e.printStackTrace()
    }
}

fun nextLevel(ctx: Context) {
    try {
        var conn: Connection? = DriverManager.getConnection(utils.BDD_URL)

        if (conn != null) {

            val regex = "^[0-9]+$".toRegex()
            val newLevel = ctx.pathParam("level")
            val pseudo = ctx.pathParam("pseudo")

            if(!pseudo.isNullOrEmpty()){
                if(regex.matches(newLevel)){
                    var insertRequest = "UPDATE users SET level = $newLevel WHERE pseudo = $pseudo"
                    val insert = conn.prepareStatement(insertRequest)
                    insert.executeUpdate()
                    conn.close()
                } else {
                    //INCORRECT LEVEL
                }
            } else {
                //PSEUDO WAS NULL
            }

        } else {
            //CONNECTION FAILED
        }

    } catch(e : Exception) {
        e.printStackTrace()
    }
}

fun getMoreLogo(ctx: Context) {
    // the level of the player
    val playerLevel: Int
    try {
        playerLevel = ctx.pathParam("level").toInt()
        // throw error if the level is negative to call the invalid_level_function and
        // don't write a second line where we call it
        if (playerLevel < 0) throw Error()
    } catch (e: Exception) {
        // invalid level number is a bad request
        return badRequest(ctx)
    }

    // get the json with all questions
    var questionsToSend: List<Question> = listOf()
    try {

        val questions = {}.javaClass.getResource("/questions.json").readText()

        val questionList = Klaxon().parse<QuestionList>(questions)?.questions
        if (questionList == null) {
            throw Exception();
        }

        // prepare the slice to send
        val numberSend = min(NUMBER_QUESTION_SEND, questionList.size - playerLevel)
        questionsToSend = questionList.slice(playerLevel..playerLevel + numberSend - 1)

    } catch (e: IOException) {
        e.printStackTrace()
    }
    // prepare the images and change the path properties of each question because they go on the client device
    for (question in questionsToSend) {
        val pic: ByteArray
        try {
            pic = {}.javaClass.getResource(question.path).readBytes()
        } catch (e: IOException) {
            return internalError(ctx)
        }
        val encodedPic = Base64.getEncoder().encode(pic)

        question.path = encodedPic.toString()
    }

    // parse the list to a json
    ctx.result(questionsToSend.toString())
}

fun badRequest(ctx: Context) {
    ctx.status(400)
}

fun endGame(ctx: Context) {
    val picture = {}.javaClass.getResource("/endGame.png").readBytes()

    val encodedString = Base64.getEncoder().encode(picture)

    ctx.result(encodedString.toString())
}

fun how_the_fuck_i_play_your_game(ctx: Context) {
}