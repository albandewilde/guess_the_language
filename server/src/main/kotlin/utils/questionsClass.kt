package utils.utils

class QuestionList(var questions: List<Question>) {}

class Question(var path: String, var choices: List<String>, var response_idx: Int){
    override fun toString(): String {
    // hard convertion to a string which look like a json
        return "{" +
            "\"path\": \"${this.path}\"," +
            "\"choices\": \"${this.choices}\"," +
            "\"response_idx\": \"${this.response_idx}\"" +
        "}"
    }
}