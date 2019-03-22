package utils

data class Question(var path: String, var choices: List<String>, var response_idx: Int)

data class QuestionsList(var content: List<Question>, var finishEh: Boolean)

data class EndGame(var content: String, var end: Boolean)