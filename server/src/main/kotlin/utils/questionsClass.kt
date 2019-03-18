package utils.utils

class QuestionList(var questions: List<Question>) {}

class Question(var path: String, var choices: List<String>, var response_idx: Int){}

class ThingToSend(var finishEh: Boolean, var content: Any) {}