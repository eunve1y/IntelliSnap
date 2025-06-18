package com.eunyoung.android_class

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class MyInfoActivity : Fragment() {
    private lateinit var idTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var birthTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_info_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom)
            insets
        }

        idTextView    = view.findViewById(R.id.idText)
        nameTextView  = view.findViewById(R.id.nameText)
        birthTextView = view.findViewById(R.id.birthText)

        idTextView.text    = "DEBUG ID입니다"
        nameTextView.text  = "DEBUG 이름입니다"
        birthTextView.text = "DEBUG 생일입니다"

        val pkey = activity
            ?.intent
            ?.getIntExtra("PKEY", 0)
            ?: 0

        if (pkey <= 0) {
            showAlert("유효하지 않은 사용자입니다.")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("http://39.118.94.53/android/myinfo.php")
                val conn = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    setRequestProperty(
                        "Content-Type",
                        "application/x-www-form-urlencoded"
                    )
                    doOutput = true
                }

                val postData = "pkey=$pkey"
                OutputStreamWriter(conn.outputStream).use {
                    it.write(postData)
                    it.flush()
                }

                val raw = conn.inputStream.bufferedReader().use { it.readText() }
                conn.disconnect()

                val response = raw.trim()
                println("MyInfo 서버 응답(raw) ▶ <$raw>")
                println("MyInfo 서버 응답(trim)▶ <$response>")

                withContext(Dispatchers.Main) {
                    when {
                        response == "0" -> {
                            showAlert("내 정보 조회 실패")
                        }
                        response.startsWith("{") -> {
                            try {
                                val rootJson = JSONObject(response)
                                val userObj = rootJson.optJSONObject("user")
                                if (userObj != null) {
                                    // userObj 에서 꺼내서 바인딩
                                    val id    = userObj.optString("id", "")
                                    val name  = userObj.optString("name", "")
                                    val birth = userObj.optString("birth", "")
                                    idTextView.text    = "ID: $id"
                                    nameTextView.text  = "이름: $name"
                                    birthTextView.text = "생년월일: $birth"
                                } else {
                                    showAlert("내 정보 파싱 오류")
                                }
                            } catch (e: JSONException) {
                                showAlert("내 정보 파싱 오류")
                            }
                        }
                        else -> {
                            showAlert("알 수 없는 응답: $response")
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showAlert("통신 오류: ${e.localizedMessage}")
                }
            }
        }
    }

    private fun showAlert(msg: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(msg)
            .setNeutralButton("확인", null)
            .show()
    }
}
