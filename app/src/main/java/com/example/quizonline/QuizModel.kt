package com.example.quizonline

data class QuizModel(
    val id: String,
    val title : String,
    val subtitle: String,
    val time: String,
    val questionList: List<QuestionModel>
){
    constructor() : this("","","","", emptyList())
}

data class QuestionModel(
    val question: String,
    val image: String,
    val option: List<String>,
    val correct: String,
){
    constructor(): this("","", emptyList(),"")
}