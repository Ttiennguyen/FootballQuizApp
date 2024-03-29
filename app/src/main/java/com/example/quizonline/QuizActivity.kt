package com.example.quizonline

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizonline.databinding.ActivityQuizBinding
import com.example.quizonline.databinding.QuizItemRecyclerRowBinding
import com.example.quizonline.databinding.ScoreDialogBinding

class QuizActivity : AppCompatActivity(),View.OnClickListener {

    companion object{
        var questionModelList : List<QuestionModel> = listOf()
        var time : String = ""
    }

    lateinit var binding: ActivityQuizBinding

    var currentQuestionIndex = 0
    var selectAnswer =""
    var score =0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            nextBtn.setOnClickListener(this@QuizActivity)
        }
        loadQuestions()
        startTimer()
    }

    private fun startTimer() {
        val totalTimeInMillis = time.toInt() * 60 * 1000L
        object : CountDownTimer(totalTimeInMillis,1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished /1000
                val minutes = seconds/60
                val remainingSeconds = seconds%60
                binding.timerIndicatorTextview.text = String.format("%02d:%02d",minutes,remainingSeconds)
            }

            override fun onFinish() {
                // finish the quiz

            }

        }.start()
    }

    private fun loadQuestions(){
        selectAnswer =""
        if(currentQuestionIndex == questionModelList.size){
            finishQuiz()
            return
        }
        binding.apply {
            questionIndicatorTextview.text = "Question ${currentQuestionIndex+1}/${questionModelList.size}"
            questionProgressIndicator.progress =
                (currentQuestionIndex.toFloat() / questionModelList.size.toFloat() * 100).toInt()
            questionTextview.text = questionModelList[currentQuestionIndex].question
            // Lấy tên hình ảnh từ đối tượng QuestionModel hiện tại
            val imageName = questionModelList[currentQuestionIndex].image
            // Lấy ID của hình ảnh từ tên hình ảnh sử dụng phương thức getIdentifier()
            val imageResourceId = resources.getIdentifier(imageName, "drawable", packageName)
            // Đảm bảo rằng ID hình ảnh khác 0 (tức là hình ảnh tồn tại)
            if (imageResourceId != 0) {
                // Set hình ảnh cho ImageView
                questionImage.setImageResource(imageResourceId)
            }
//            else {
//                // Xử lý trường hợp không tìm thấy hình ảnh
//                // Ví dụ: hiển thị một hình ảnh mặc định hoặc thông báo lỗi
//                questionImage.setImageResource(R.drawable.default_image)
//            }
            btn0.text = questionModelList[currentQuestionIndex].option[0]
            btn1.text = questionModelList[currentQuestionIndex].option[1]
            btn2.text = questionModelList[currentQuestionIndex].option[2]
            btn3.text = questionModelList[currentQuestionIndex].option[3]

        }
    }

    override fun onClick(view: View?) {

        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.gray))
            btn1.setBackgroundColor(getColor(R.color.gray))
            btn2.setBackgroundColor(getColor(R.color.gray))
            btn3.setBackgroundColor(getColor(R.color.gray))
        }

        val clickedBtn = view as Button
        if(clickedBtn.id ==R.id.next_btn){
            if(selectAnswer.isEmpty()){
                Toast.makeText(applicationContext,"Please select answer to continue",Toast.LENGTH_SHORT).show()
                return;

            }
            if (selectAnswer == questionModelList[currentQuestionIndex].correct){
                score++
                Log.i("Score of quiz",score.toString())
            }
            currentQuestionIndex++
            loadQuestions()
        }else{
            selectAnswer = clickedBtn.text.toString()
            clickedBtn.setBackgroundColor(getColor(R.color.orange))
        }
    }

    private fun finishQuiz(){
        val totalQuestions = questionModelList.size
        val percentage = ((score.toFloat()/totalQuestions.toFloat())*100).toInt()

        val dialogBinding = ScoreDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgressIndicator.progress = percentage
            scoreProgressText.text = "$percentage %"
            // phần tính điểm nếu trên 60 điểm thì pass
            if(percentage>60){
                scoreTitle.text = "Congrats! You have passed"
                scoreTitle.setTextColor(Color.BLUE)
            }else{
                scoreTitle.text = "Oops! You have fails"
                scoreTitle.setTextColor(Color.RED)
            }
            scoreSubtitle.text = "$score out of $totalQuestions are correct!"
            finishBtn.setOnClickListener{
                finish()
            }
        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()

    }
}