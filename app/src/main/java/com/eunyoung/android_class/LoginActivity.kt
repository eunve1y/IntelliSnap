package com.eunyoung.android_class

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom)
            insets
        }

        val idEt       = findViewById<EditText>(R.id.idEditText)
        val pwdEt      = findViewById<EditText>(R.id.pwdEditText)
        val loginBtn   = findViewById<Button>(R.id.loginButton)
        val signupBtn  = findViewById<Button>(R.id.signupButton)

        loginBtn.setOnClickListener {
            val id  = idEt.text.toString().trim()
            val pwd = pwdEt.text.toString().trim()

            if (id.length < 5) {
                showAlert("ID를 5글자 이상 입력해주세요.")
                return@setOnClickListener
            }
            if ('#' in id || '$' in id) {
                showAlert("특수문자는 입력할 수 없습니다.")
                return@setOnClickListener
            }
            if (!id.contains("@") || !id.contains(".")) {
                showAlert("ID는 이메일 형식이어야 합니다.")
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val url = URL("http://39.118.94.53/android/login.php")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                    connection.doOutput = true

                    val params = mapOf(
                        "id" to "${idEt.text}",
                        "pwd" to "${pwdEt.text}"
                    )

                    val postData = params.map { (key, value) ->
                        "$key=$value"
                    }.joinToString("&")

                    OutputStreamWriter(connection.outputStream).use { writer ->
                        writer.write(postData)
                        writer.flush()
                    }

                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    connection.disconnect()

                    println("서버 응답: $response")
                    val result = response.toIntOrNull()
                    withContext(Dispatchers.Main) {
                        if(result != null && result > 0) {
                            val intent = Intent(this@LoginActivity, RootActivity::class.java).apply {
                                putExtra("PKEY", result)
                            }
                            startActivity(intent)
                        } else {
                            showAlert("로그인 실패: 아이디/비번을 확인하세요")
                        }
                    }
                }catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        showAlert("통신 오류: ${e.localizedMessage}")
                    }
                }
            }
        }

        signupBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun showAlert(msg: String) {
        AlertDialog.Builder(this)
            .setTitle(msg)
            .setNeutralButton("확인", null)
            .show()
    }
}
