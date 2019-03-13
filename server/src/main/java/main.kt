import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*

fun main(list: Array<String>){

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
        path("/subscribe_or_connect") {
            get({ctx -> ctx.status(403)})
            put(::subscribeOrConnect)
        }
        // the player pass a level
        path("/pass_level/:new_level") {
            post{::pass_level}
        }
        // the player ask for more logo
        path("/more_logo/:curent_level") {
            get(::give_more_logo)
        }
        // the player ask for the rules
        path("/rules") {
            get(::how_the_fuck_i_play_your_game)
        }
    }

    // configure the errors
    serv.error(404) {ctx -> ctx.result("We don't find it, sorry <°)))<")}
    serv.error(403) {ctx -> forbidden(ctx)}
}