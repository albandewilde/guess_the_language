package utils

import io.javalin.ForbiddenResponse
import java.nio.file.Files
import java.nio.file.Paths

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*

fun main(){

    val serv = Javalin.create()
        // all static request point to the `project_folder`/src/main/resources/public folder
        // that mean if /`the_thing_you_want` isn't in the routes path,
        // the server search in the public folder if your resource is present,
        // if not, you got a 404 page
        .enableStaticFiles("/public")
        // the server listen on port 7000
        .start(7000)

    // make the routes
    serv.routes{
        path("/") {
            get(::accueil)
            post({ctx -> ctx.status(403)})
        }
        // the player give us his pseudo
        path("/subscribe_or_connect/:pseudo") {
            get({ctx -> ctx.status(403)})
            put(::subscribeOrConnect)
        }
        // the player pass a level
        path("/next_level/:pseudo/:level") {
            post{::nextLevel}
        }
        // the player ask for more logo
        path("/get_more_logo/:level") {
            get(::getMoreLogo)
        }
        // the player ask for the rules
        path("/rules") {
            get(::how_the_fuck_i_play_your_game)
        }
    }

    // configure the errors
    serv.error(400) {
        ctx -> ctx.html(
            {}.javaClass.getResource("/NOPE.HTML").readText()
        )
    }
    serv.error(403) {
        ctx -> ctx.html(
            {}.javaClass.getResource("/what_the_fuck_are_you_trying_to_do.html").readText()
        )
    }
    serv.error(404) {
        ctx -> ctx.html(
            {}.javaClass.getResource("/the_not_found_page.html").readText()
        )
    }
    serv.error(500) {
        ctx -> ctx.html(
            {}.javaClass.getResource("/when_server_error_occur.html").readText()
        )
    }
}