package com.eunyoung.android_class

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

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom)
            insets
        }

        val idEt     = findViewById<EditText>(R.id.signupIdEdit)
        val pwdEt    = findViewById<EditText>(R.id.signupPwdEdit)
        val nameEt   = findViewById<EditText>(R.id.signupNameEdit)
        val birthEt  = findViewById<EditText>(R.id.signupBirthEdit)
        val complete = findViewById<Button>(R.id.btnCompleteSignUp)
        val cancel   = findViewById<Button>(R.id.btnCancelSignUp)

        complete.setOnClickListener {
            val id    = idEt.text.toString().trim()
            val pwd   = pwdEt.text.toString().trim()
            val name  = nameEt.text.toString().trim()
            val birth = birthEt.text.toString().trim()

            if (id.isEmpty() || pwd.isEmpty() || name.isEmpty() || birth.length != 8) {
                showAlert("모든 필드를 올바르게 입력해주세요.\n생년월일 8자리(예:20000101)")
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val url = URL("http://39.118.94.53/android/signup.php")
                    val conn = (url.openConnection() as HttpURLConnection).apply {
                        requestMethod = "POST"
                        setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                        doOutput = true
                    }

                    val postData = "id=${id}&pwd=${pwd}&name=${name}&birth=${birth}"
                    OutputStreamWriter(conn.outputStream).use { it.write(postData) }

                    val response = conn.inputStream.bufferedReader().use { it.readText() }
                    conn.disconnect()

                    val result = response.toIntOrNull()

                    withContext(Dispatchers.Main) {
                        if (result != null && result > 0) {
                            showAlert("가입 성공! 회원번호: $result")
                            finish()
                        } else {
                            showAlert("가입 실패: 입력값을 확인해주세요.")
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        showAlert("통신 오류: ${e.localizedMessage}")
                    }
                }
            }
        }

        cancel.setOnClickListener {
            finish()
        }
    }

    private fun showAlert(msg: String) {
        AlertDialog.Builder(this)
            .setTitle(msg)
            .setNeutralButton("확인", null)
            .show()
    }
}
