package com.example.montapalavras

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val okButton: MaterialButton = findViewById(R.id.btn_ok)
        val initialTitle: MaterialTextView = findViewById(R.id.title)
        val resultViewGroup: ViewGroup = findViewById(R.id.result_layout)
        val editText: EditText = findViewById(R.id.edtText)
        val pointsTextView: MaterialTextView = findViewById(R.id.points)
        val wordTextView: MaterialTextView = findViewById(R.id.word_txt)
        val remainsTextView: MaterialTextView = findViewById(R.id.remain_text)



        okButton.setOnClickListener {

            initialTitle.visibility = View.GONE
            resultViewGroup.visibility = View.VISIBLE

            val availableLetters = editText.text.toString()



            val (result, points, remains) = MatchMaker(availableLetters).findBestWord()


            pointsTextView.text = getString(R.string.score, points)
            wordTextView.text = getString(R.string.word_letters, result).toUpperCase(
                Locale.US
            )
            remainsTextView.text = getString(R.string.remain_label_letters, remains).toUpperCase(Locale.US)


        }

        }
    }
