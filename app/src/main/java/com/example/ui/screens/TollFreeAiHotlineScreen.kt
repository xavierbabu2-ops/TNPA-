package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.TnpaViewModel
import com.example.ui.components.UnionFlagBadge
import com.example.ui.theme.UnionGoldAccent
import com.example.ui.theme.UnionGoldBright
import com.example.ui.theme.UnionGreen
import com.example.ui.theme.UnionRedDark
import com.example.ui.theme.UnionRedLight
import com.example.ui.theme.UnionRedPrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class CallState {
    IDLE,
    DIALING,
    AI_GREETING,
    USER_RECORDING,
    AI_CONFIRMATION,
    COMPLETED
}

data class VoiceHotlineCallRecord(
    val id: String,
    val callerName: String,
    val callerPhone: String,
    val userMessage: String,
    val timestamp: String,
    val whatsappSent: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TollFreeAiHotlineScreen(viewModel: TnpaViewModel) {
    val isTamil by viewModel.isTamil.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var callState by remember { mutableStateOf(CallState.IDLE) }
    var callerName by remember { mutableStateOf("சங்க உறுப்பினர்") }
    var callerPhone by remember { mutableStateOf("98400 12345") }
    var callerDistrict by remember { mutableStateOf("மதுரை") }
    var userRecordedMessage by remember { mutableStateOf("") }
    var activeSpeakerText by remember { mutableStateOf("") }
    var callSeconds by remember { mutableIntStateOf(0) }
    var isTtsSpeaking by remember { mutableStateOf(false) }

    val callHistory = remember { mutableStateListOf<VoiceHotlineCallRecord>() }

    // Android TextToSpeech Setup
    var ttsEngine by remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(context) {
        val tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Try Tamil locale
                val tamilResult = ttsEngine?.setLanguage(Locale("ta", "IN"))
                if (tamilResult == TextToSpeech.LANG_MISSING_DATA || tamilResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    ttsEngine?.language = Locale.getDefault()
                }
            }
        }
        ttsEngine = tts
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    // Function to speak via TTS
    fun speakAiMessage(textTamil: String, onComplete: () -> Unit = {}) {
        activeSpeakerText = textTamil
        isTtsSpeaking = true
        ttsEngine?.speak(textTamil, TextToSpeech.QUEUE_FLUSH, null, "TNPA_AI_CALL_ID")

        coroutineScope.launch {
            // Estimate speech duration or delay
            val durationMs = (textTamil.length * 110L).coerceAtLeast(3000L)
            delay(durationMs)
            isTtsSpeaking = false
            onComplete()
        }
    }

    // Active Call Duration Timer
    LaunchedEffect(callState) {
        if (callState != CallState.IDLE && callState != CallState.COMPLETED) {
            callSeconds = 0
            while (callState != CallState.IDLE && callState != CallState.COMPLETED) {
                delay(1000L)
                callSeconds++
            }
        }
    }

    // Call Workflow Orchestration
    fun startTollFreeAiCall() {
        callState = CallState.DIALING
        activeSpeakerText = "இலவச எண் 1800-425-7010 இணைப்பு செய்யப்படுகிறது..."

        coroutineScope.launch {
            delay(2500L) // Dialing delay
            callState = CallState.AI_GREETING

            val aiGreetingMsg = "வணக்கம்! தமிழ்நாடு பெயிண்டர்கள் மற்றும் ஓவியர்கள் முன்னேற்ற சங்கம் - 24 மணி நேர இலவச AI குரல் சேவைக்கு நல்வரவு. உங்களது தகவல்களையோ அல்லது செய்திகளையோ கூறுங்கள்!"
            
            speakAiMessage(aiGreetingMsg) {
                callState = CallState.USER_RECORDING
                activeSpeakerText = "உங்களது தகவல்களையோ அல்லது செய்தியையோ பேசுங்கள் / தட்டச்சு செய்யுங்கள்..."
            }
        }
    }

    fun submitUserVoiceMessage(message: String) {
        if (message.isBlank()) return
        userRecordedMessage = message

        coroutineScope.launch {
            callState = CallState.AI_CONFIRMATION
            val aiThankYouMsg = "நன்றி! உங்களது தகவல் வெற்றிகரமாக பதிவு செய்யப்பட்டது. விரைவில் எங்களது சங்க நிர்வாகிகள் உங்களை தொலைபேசியில் தொடர்பு கொள்வார்கள்!"

            speakAiMessage(aiThankYouMsg) {
                callState = CallState.COMPLETED
                val recordId = "TNPA-CALL-${(1000..9999).random()}"
                val timeStr = SimpleDateFormat("dd/MM/yyyy, hh:mm a", Locale.getDefault()).format(Date())
                callHistory.add(
                    0,
                    VoiceHotlineCallRecord(
                        id = recordId,
                        callerName = callerName,
                        callerPhone = callerPhone,
                        userMessage = userRecordedMessage,
                        timestamp = timeStr
                    )
                )
            }
        }
    }

    fun sendToExecutiveWhatsapp(record: VoiceHotlineCallRecord? = null) {
        val msgToSend = record?.userMessage ?: userRecordedMessage
        val name = record?.callerName ?: callerName
        val phone = record?.callerPhone ?: callerPhone

        val whatsappText = """
            📞 *TNPA² AI Toll-Free (1800-425-7010) குரல் செய்தி பெறப்பட்டது!*
            
            👤 *அழைப்பாளர் பெயா்:* $name
            📱 *தொலைபேசி எண்:* $phone
            📍 *மாவட்டம்:* $callerDistrict
            
            🎙️ *பதிவு செய்யப்பட்ட குரல் செய்தி:*
            "$msgToSend"
            
            ⏰ *நேரம்:* ${SimpleDateFormat("dd/MM/yyyy, hh:mm a", Locale.getDefault()).format(Date())}
            ----------------------------------
            *சங்க நிர்வாகிகள் விரைவில் தொடர்பு கொள்ளவும்!*
        """.trimIndent()

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://api.whatsapp.com/send?phone=917010131915&text=${Uri.encode(whatsappText)}")
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp செயலியை திறக்க முடியவில்லை", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Banner Header
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(UnionRedDark, UnionRedPrimary)
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            UnionFlagBadge(width = 44.dp, height = 30.dp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "24x7 இலவச AI குரல் சேவை",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = UnionGoldBright
                                )
                                Text(
                                    text = "Toll-Free Hotline: 1800-425-7010",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Surface(
                                color = UnionGreen,
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PhoneInTalk,
                                        contentDescription = "Live",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "LIVE AI",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "இலவச எண்ணிற்கு அழைக்கும் போது தானியங்கி AI குரல் வழிகாட்டி உங்களை வரவேற்று, உங்களது தகவலைப் பதிவு செய்து மாநில / மாவட்ட நிர்வாகிகளின் வாட்ஸ் அப்பிற்கு உடனடியாக அனுப்பி வைக்கும்!",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Active Interactive Call Simulator Window
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (callState != CallState.IDLE && callState != CallState.COMPLETED) Color(0xFF0F172A) else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (callState == CallState.IDLE) {
                        // IDLE STATE - Caller Info Setup & Big Start Button
                        Text(
                            text = "AI குரல் அழைப்பு சோதனை மையம் (Hotline Simulator)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = callerName,
                            onValueChange = { callerName = it },
                            label = { Text("அழைப்பாளர் பெயர்") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = UnionRedPrimary,
                                focusedLabelColor = UnionRedPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = callerPhone,
                                onValueChange = { callerPhone = it },
                                label = { Text("தொலைபேசி எண்") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = callerDistrict,
                                onValueChange = { callerDistrict = it },
                                label = { Text("மாவட்டம்") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { startTollFreeAiCall() },
                            colors = ButtonDefaults.buttonColors(containerColor = UnionGreen),
                            shape = RoundedCornerShape(30.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .shadow(6.dp, CircleShape)
                        ) {
                            Icon(imageVector = Icons.Default.Call, contentDescription = "Call", tint = Color.White)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "1800-425-7010 AI குரல் அழைப்பைத் தொடங்கு",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    } else if (callState == CallState.COMPLETED) {
                        // CALL COMPLETED STATE WITH WHATSAPP DISPATCH
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completed",
                            tint = UnionGreen,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "அழைப்பு முடிந்தது & தகவல் பதிவு செய்யப்பட்டது!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = UnionGreen,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "பதிவு செய்யப்பட்ட செய்தி: \"$userRecordedMessage\"",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Green WhatsApp Action Button
                        Button(
                            onClick = { sendToExecutiveWhatsapp() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = "WhatsApp", tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "வாட்ஸ் அப்பில் மாநில நிர்வாகிக்கு அனுப்பு (7010131915) 💬",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { callState = CallState.IDLE; userRecordedMessage = "" },
                            colors = ButtonDefaults.buttonColors(containerColor = UnionRedPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "புதிய அழைப்பு சோதனை செய்க", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        // ACTIVE CALL SIMULATION SCREEN (DIALING / AI GREETING / RECORDING / AI CONFIRMATION)
                        val callDurationFormatted = String.format("%02d:%02d", callSeconds / 60, callSeconds % 60)

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(color = Color(0xFF1E293B), shape = RoundedCornerShape(12.dp)) {
                                Text(
                                    text = "Toll-Free: 1800-425-7010",
                                    fontSize = 12.sp,
                                    color = UnionGoldBright,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            Text(
                                text = callDurationFormatted,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Animated Pulse / Sound Visualizer Circle
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(120.dp)
                        ) {
                            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                            val pulseScale by infiniteTransition.animateFloat(
                                initialValue = 1f,
                                targetValue = 1.25f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(800, easing = LinearEasing),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "scale"
                            )

                            Box(
                                modifier = Modifier
                                    .size(110.dp)
                                    .scale(if (isTtsSpeaking || callState == CallState.USER_RECORDING) pulseScale else 1f)
                                    .clip(CircleShape)
                                    .background(
                                        if (callState == CallState.USER_RECORDING) UnionRedPrimary.copy(alpha = 0.3f)
                                        else UnionGoldAccent.copy(alpha = 0.3f)
                                    )
                            )

                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (callState == CallState.USER_RECORDING) UnionRedDark else Color(0xFF1E3A8A)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (callState == CallState.USER_RECORDING) Icons.Default.Mic else Icons.Default.SupportAgent,
                                    contentDescription = "AI Voice",
                                    tint = Color.White,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = when (callState) {
                                CallState.DIALING -> "இணைக்கப்படுகிறது..."
                                CallState.AI_GREETING -> "🤖 AI குரல் பேசுகிறது..."
                                CallState.USER_RECORDING -> "🎙️ உங்களது தகவலைப் பேசுங்கள்..."
                                CallState.AI_CONFIRMATION -> "🤖 AI உறுதிப்படுத்துகிறது..."
                                else -> ""
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = UnionGoldBright
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Spoken Text Box Subtitles
                        Surface(
                            color = Color(0xFF1E293B),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = activeSpeakerText,
                                fontSize = 13.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp,
                                modifier = Modifier.padding(14.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Input Box during USER_RECORDING state
                        if (callState == CallState.USER_RECORDING) {
                            var tempSpeechText by remember { mutableStateOf("") }

                            Column(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = tempSpeechText,
                                    onValueChange = { tempSpeechText = it },
                                    placeholder = { Text("அல்லது உங்களது தகவலை தட்டச்சு செய்யவும்...", color = Color.Gray) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = UnionGoldBright,
                                        unfocusedBorderColor = Color.Gray,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Quick Voice Preset Suggestions
                                Text(
                                    text = "விரைவு கோரிக்கை மாதிரி:",
                                    fontSize = 11.sp,
                                    color = Color.LightGray
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.padding(top = 4.dp)
                                ) {
                                    Surface(
                                        color = Color(0xFF334155),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.clickable {
                                            tempSpeechText = "நலவாரிய அட்டை புதுப்பிக்க உதவி தேவை."
                                        }
                                    ) {
                                        Text(
                                            text = "நலவாரிய அட்டை",
                                            fontSize = 11.sp,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                    Surface(
                                        color = Color(0xFF334155),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.clickable {
                                            tempSpeechText = "புதிய உறுப்பினர் சேர்க்கை பற்றி தகவல் வேண்டும்."
                                        }
                                    ) {
                                        Text(
                                            text = "உறுப்பினர் சேர்க்கை",
                                            fontSize = 11.sp,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = {
                                        val textToSubmit = if (tempSpeechText.isNotBlank()) tempSpeechText else "மதுரையில் இருந்து தொழிலாளர் நலத்திட்ட உதவி கோரி அழைத்துள்ளேன்."
                                        submitUserVoiceMessage(textToSubmit)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = UnionGreen),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.Send, contentDescription = "Submit", tint = Color.White)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("பேசி முடித்து விட்டேன் (Submit Voice Message)", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // End Call Button
                        IconButton(
                            onClick = {
                                ttsEngine?.stop()
                                callState = CallState.IDLE
                            },
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFDC2626))
                        ) {
                            Icon(imageVector = Icons.Default.CallEnd, contentDescription = "End Call", tint = Color.White)
                        }
                    }
                }
            }
        }

        // Direct Call Phone Numbers & Executive WhatsApp Hotlines
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "சங்க தலைமை & அவசர உதவி எண்கள்",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = UnionRedDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    HotlineContactRow(
                        title = "இலவச உதவி எண் (Toll-Free Helpline)",
                        phone = "1800-425-7010",
                        subtitle = "24x7 AI குரல் வழிகாட்டி சேவை",
                        onCall = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:18004257010"))
                            context.startActivity(intent)
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    HotlineContactRow(
                        title = "மாநில பொதுச்செயலாளர் (சேவியர் பாபு)",
                        phone = "7010131915",
                        subtitle = "நேரடி தொலைபேசி & வாட்ஸ்அப் உதவி",
                        onCall = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:7010131915"))
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }

        // Call History / Voice Messages Submitted Section
        if (callHistory.isNotEmpty()) {
            item {
                Text(
                    text = "சமீபத்திய AI குரல் பதிவுகள் (Call History Logs)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(callHistory) { record ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = UnionRedDark, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = record.callerName, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(text = record.timestamp, fontSize = 11.sp, color = Color.Gray)
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "\"${record.userMessage}\"",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { sendToExecutiveWhatsapp(record) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "WhatsApp", tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("வாட்ஸ் அப்பில் அனுப்பு (${record.callerPhone})", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HotlineContactRow(
    title: String,
    phone: String,
    subtitle: String,
    onCall: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = "📞 $phone", fontSize = 14.sp, fontWeight = FontWeight.Black, color = UnionRedDark)
            Text(text = subtitle, fontSize = 11.sp, color = Color.Gray)
        }

        Button(
            onClick = onCall,
            colors = ButtonDefaults.buttonColors(containerColor = UnionGreen),
            shape = RoundedCornerShape(20.dp)
        ) {
            Icon(Icons.Default.Call, contentDescription = "Call", tint = Color.White, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("அழைக்குக", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
