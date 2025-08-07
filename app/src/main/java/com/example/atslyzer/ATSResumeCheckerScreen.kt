package com.example.atslyzer

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.atslyzer.viewModel.ResumeViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.compose.koinViewModel
import java.io.File



@Composable
fun ATSResumeCheckerScreen(viewModel: ResumeViewModel = koinViewModel() ) {

    val context = LocalContext.current

    var jobRole by remember { mutableStateOf("") }
    var pdfUri by remember { mutableStateOf<Uri?>(null) }

    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        pdfUri = uri
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFF0F0C29),
                        Color(0xFF302B63),
                        Color(0xFF24243E)
                    ),
                    startY = 0f,
                    endY = 300f
                )
            )
    ) {

        val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = statusBarPadding + 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "ATS Resume Checker",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            OutlinedTextField(
                value = jobRole,
                onValueChange = { jobRole = it },
                placeholder = { Text("Enter Job Role...") },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedBorderColor = Color(0xFFB0BEC5),
                    cursorColor = Color(0xFF4CAF50),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(Color(0xFFE0F2F1), Color(0xFFCFD8DC))
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(1.dp, Color(0xFF4CAF50), RoundedCornerShape(20.dp))
                    .padding(4.dp)
            )



            Button(
                onClick = { pdfPickerLauncher.launch("application/pdf") },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pick PDF", color = Color.White)
            }

            pdfUri?.let { uri ->
                val fileName = getFileNameFromUri(context, uri)
                Text(
                    text = "$fileName uploaded ✅",
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Medium
                )
            }


            Button(
                onClick = {
                    if (pdfUri != null && jobRole.isNotBlank()) {
                        val file = uriToFile(pdfUri!!, context)
                        file?.let {
                            val filePart = it.asMultipart()
                            val jobRolePart = jobRole.toRequestBody()
                            viewModel.analyzeResume(filePart, jobRolePart)
                        }
                    }
                },
                enabled = !viewModel.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Analyze Resume")
            }

            if (viewModel.isLoading) {

                WavyProgressBar(progress = 0.6f, modifier = Modifier.padding(16.dp))

            } else {
                viewModel.errorMessage?.let {
                    Text("Error: $it", color = Color.Red)
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("ATS Score", fontSize = 18.sp)
                        Text(
                            "${viewModel.atsScore}%",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF6200EE)
                        )
                        Text(
                            text = when {
                                viewModel.atsScore >= 80 -> "Excellent Match"
                                viewModel.atsScore >= 50 -> "Moderate Match"
                                else -> "Low Match"
                            },
                            color = Color.DarkGray
                        )
                    }
                }

                Text("✅ Found Keywords", fontWeight = FontWeight.SemiBold , color = Color.White)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    viewModel.foundKeywords.forEach {
                        AssistChip(label = { Text(it, color = Color.White) }, onClick = {})
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("❌ Missing Keywords", fontWeight = FontWeight.SemiBold , color = Color.White)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    viewModel.missingKeywords.forEach {
                        AssistChip(label = { Text(it, color = Color.White) }, onClick = {})
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("📝 Grammar Mistakes", fontWeight = FontWeight.SemiBold, color = Color.White)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    viewModel.grammarMistakes.forEach {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEAEA))
                        ) {
                            Text(
                                text = it,
                                modifier = Modifier.padding(12.dp),
                                color = Color(0xFFB3261E)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("💡 Suggestions", fontWeight = FontWeight.SemiBold , color = Color.White)
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    viewModel.suggestions.forEach { suggestion ->
                        ListItem(
                            headlineContent = { Text(suggestion ) },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Tip"
                                )
                            }
                        )
                    }
                }

            }
        }
    }
}


fun File.asMultipart(name: String = "file"): MultipartBody.Part {
    val requestFile = this.asRequestBody("application/pdf".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(name, this.name, requestFile)
}



fun uriToFile(uri: Uri, context: Context): File? {
    val inputStream = context.contentResolver.openInputStream(uri) ?: return null
    val file = File(context.cacheDir, "temp_resume.pdf")
    file.outputStream().use { inputStream.copyTo(it) }
    return file
}


fun getFileNameFromUri(context: Context, uri: Uri): String {
    var result: String? = null
    if (uri.scheme == "content") {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            }
        }
    }
    if (result == null) {
        result = uri.path?.substringAfterLast('/')
    }
    return result ?: "File"
}



