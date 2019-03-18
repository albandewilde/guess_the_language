package utils

const val BDD_URL = "jdbc:sqlite:./data/guessTheLanguage.db"
// the number of question send when the get_more_logo request is call
// don't try to put negative number otherwise the server run into error
const val NUMBER_QUESTION_SEND = 5