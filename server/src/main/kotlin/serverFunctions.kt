package utils

import com.beust.klaxon.Klaxon
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.jdbc.JdbcConnectionSource

import io.javalin.Context
import io.javalin.InternalServerErrorResponse

import utils.utils.Question
import java.sql.Connection
import java.sql.DriverManager
import kotlin.math.min
import utils.utils.QuestionList
import utils.utils.ThingToSend
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
            val source = JdbcConnectionSource(utils.BDD_URL)
            var regex = "^[a-zA-Z0-9_]+$".toRegex()
            val pseudo = ctx.pathParam("pseudo")

            var userDao: Dao<Users, String>  = DaoManager.createDao(source, Users::class.java)

            try {
                if (regex.matches(pseudo)) {
                    // instantiate user and set user's pseudo
                    var user = Users()
                    user.pseudo = pseudo
                    //insert user into users Table if not exists
                    userDao.createIfNotExists(user)

                    // get the user infos
                    var query = userDao.queryBuilder()
                            .where()
                            .eq("pseudo", pseudo)
                            .prepare()

                    user = userDao.queryForFirst(query)

                    ctx.result(Klaxon().toJsonString(user))
                    println(Klaxon().toJsonString(user))
                } else {
                    badRequest(ctx)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            internalError(ctx)
        }
    } catch (e : Exception) {
        e.printStackTrace()
    }
}

fun nextLevel(ctx: Context) {
    try {
        var conn: Connection? = DriverManager.getConnection(utils.BDD_URL)
        val source = JdbcConnectionSource(utils.BDD_URL)
        var userDao: Dao<Users, String>  = DaoManager.createDao(source, Users::class.java)

        if (conn != null) {
            val pseudo = ctx.pathParam("pseudo")
            val newLevel = ctx.pathParam("level").toInt()
            var points = ctx.pathParam("points").toInt()

            if(!pseudo.isNullOrEmpty()){

                var insertRequest = "UPDATE users SET level = ?, points = ? WHERE pseudo = ?"
                val insert = conn.prepareStatement(insertRequest)
                insert.setInt(1, newLevel)
                insert.setInt(2, points)
                insert.setString(3, pseudo)
                insert.executeUpdate()
                conn.close()

            } else {
                badRequest(ctx)
            }
        } else {
            internalError(ctx)
        }

    } catch(e : Exception) {
        e.printStackTrace()
    }
}

fun updatePoints(ctx: Context) {
    try {
        var conn: Connection? = DriverManager.getConnection(utils.BDD_URL)
        val source = JdbcConnectionSource(utils.BDD_URL)
        var userDao: Dao<Users, String>  = DaoManager.createDao(source, Users::class.java)

        if (conn != null) {
            val pseudo = ctx.pathParam("pseudo")
            var points = ctx.pathParam("points").toInt()

            if(!pseudo.isNullOrEmpty()){

                var insertRequest = "UPDATE users SET  points = ? WHERE pseudo = ?"
                val insert = conn.prepareStatement(insertRequest)
                insert.setInt(1, points)
                insert.setString(2, pseudo)
                insert.executeUpdate()
                conn.close()

            } else {
                badRequest(ctx)
            }
        } else {
            internalError(ctx)
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
        if (playerLevel >= questionList.size) {
            // the player have reatch the highest level, we don"t have other logos to send him
            return endGame(ctx)
        } else {
            // the player can have more logo
            val numberSend = min(NUMBER_QUESTION_SEND, questionList.size - playerLevel)
            questionsToSend = questionList.slice(playerLevel..playerLevel + numberSend - 1)
        }

    } catch (e: IOException) {
        e.printStackTrace()
    }
    // prepare the images and change the path properties of each question because they go on the client device
    for (question in questionsToSend) {
        val pic: ByteArray
        try {
            pic = {}.javaClass.getResource(question.path).readBytes()
        } catch (e: IOException) {
            throw InternalServerErrorResponse()
        }
        val encodedPic = Base64.getEncoder().encodeToString(pic)

        question.path = encodedPic
    }

    // parse the list to a json
    ctx.result(Klaxon().toJsonString(ThingToSend(false, questionsToSend)))
}

fun badRequest(ctx: Context) {
    ctx.status(400)
}

fun endGame(ctx: Context) {
    var picture = Base64.getEncoder().encodeToString({}.javaClass.getResource("/endGame.png").readBytes())
    ctx.result(Klaxon().toJsonString(ThingToSend(true, picture)))
}

fun how_the_fuck_i_play_your_game(ctx: Context) {
}

fun internalError(ctx: Context) {
    ctx.status(500)
}