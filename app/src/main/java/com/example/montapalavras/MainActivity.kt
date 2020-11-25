package com.example.montapalavras

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val okButton: MaterialButton = findViewById(R.id.btn_ok)
        val initialTitle: MaterialTextView = findViewById(R.id.title)
        val resultViewGroup: ViewGroup = findViewById(R.id.result_layout)
        val editText: EditText = findViewById(R.id.edtText)


        okButton.setOnClickListener {

            initialTitle.visibility = View.GONE
            resultViewGroup.visibility = View.VISIBLE

            val availableLetters = editText.text.toString()

            var nSyllables = 0

            for (letter in availableLetters) {
                if(letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u')
                    nSyllables++
            }

            var (result, remains, points) = ScrabbleWithMaps(availableLetters).findBestCandidate(nSyllables)

        }

        }
    }
