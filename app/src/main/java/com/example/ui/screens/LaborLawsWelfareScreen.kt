package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.WelfareSchemeEntity
import com.example.ui.TnpaViewModel
import com.example.ui.theme.UnionGoldBright
import com.example.ui.theme.UnionRedDark
import com.example.ui.theme.UnionRedPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaborLawsWelfareScreen(
    viewModel: TnpaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isTamil by viewModel.isTamil.collectAsState()
    val welfareList by viewModel.welfareSchemes.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: அனைத்து, 1: நலவாரிய விண்ணப்பம், 2: மாநில அரசு, 3: மத்திய அரசு
    var selectedSchemeForApply by remember { mutableStateOf<WelfareSchemeEntity?>(null) }

    // Filtered Schemes List
    val filteredSchemes = when (selectedTabIndex) {
        1 -> welfareList.filter { it.category.contains("நலவாரிய", ignoreCase = true) }
        2 -> welfareList.filter { it.category.contains("மாநில", ignoreCase = true) }
        3 -> welfareList.filter { it.category.contains("மத்திய", ignoreCase = true) }
        else -> welfareList
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Header Banner
        Surface(
            color = UnionRedDark,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isTamil) "அரசின் நலத்திட்டங்கள் & நலவாரிய போர்ட்டல்" else "Government Schemes & Welfare Board Portal",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = UnionGoldBright
                )
                Text(
                    text = if (isTamil) "தமிழ்நாடு நலவாரிய ஆன்லைன் விண்ணப்பம், மாநில மற்றும் மத்திய அரசு தொழிலாளர் திட்டங்கள்" else "Online Welfare Board Applications, TN State & Central Govt Labor Schemes",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Navigation Tabs
        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = UnionRedDark,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = { Text("அனைத்தும்", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text("நலவாரிய பதிவு", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTabIndex == 2,
                onClick = { selectedTabIndex = 2 },
                text = { Text("மாநில அரசு", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTabIndex == 3,
                onClick = { selectedTabIndex = 3 },
                text = { Text("மத்திய அரசு", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedTabIndex == 1) {
            // Dedicated Welfare Board Application Form
            WelfareBoardInstantFormCard(
                onSubmitted = { msg ->
                    Toast.makeText(context, "நலவாரிய விண்ணப்பம் வெற்றிகரமாக பதிவு செய்யப்பட்டது!", Toast.LENGTH_LONG).show()
                }
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(filteredSchemes) { scheme ->
                WelfareSchemeCard(
                    scheme = scheme,
                    isTamil = isTamil,
                    onApplyClick = { selectedSchemeForApply = scheme }
                )
            }
        }
    }

    // Scheme Application Modal Dialog
    selectedSchemeForApply?.let { scheme ->
        SchemeApplicationDialog(
            scheme = scheme,
            onDismiss = { selectedSchemeForApply = null }
        )
    }
}

@Composable
private fun WelfareSchemeCard(
    scheme: WelfareSchemeEntity,
    isTamil: Boolean,
    onApplyClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Scheme",
                        tint = UnionRedPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = scheme.titleTamil,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Surface(
                            color = UnionRedDark.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Text(
                                text = scheme.category,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = UnionRedDark,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Surface(
                    color = UnionRedPrimary,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = scheme.benefitsAmount,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = "🎯 தகுதி: ${scheme.eligibilityTamil}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                if (expanded) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "📄 ஆவணங்கள்: ${scheme.requiredDocsTamil}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "📝 விண்ணப்பிக்கும் முறை: ${scheme.applyProcessTamil}",
                        fontSize = 12.sp,
                        color = UnionRedDark,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onApplyClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32), contentColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(imageVector = Icons.Default.HowToReg, contentDescription = "Apply", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "உடனே அப்ளை செய்", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = if (expanded) "சுருக்கு ^" else "முழு விபரம் >",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = UnionRedPrimary,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Composable
private fun WelfareBoardInstantFormCard(onSubmitted: (String) -> Unit) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var aadhaar by remember { mutableStateOf("") }
    var rationCard by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("மதுரை") }
    var trade by remember { mutableStateOf("கட்டிட பெயிண்டர்") }
    var nomineeName by remember { mutableStateOf("") }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(2.dp, UnionRedDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Assignment, contentDescription = "Form", tint = UnionRedDark, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "தமிழ்நாடு உடலுழைப்பு தொழிலாளர்கள் நலவாரிய ஆன்லைன் பதிவு",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = UnionRedDark
                    )
                    Text(
                        text = "உடனடி பதிவு - தேவையான தகவல்களை பூர்த்தி செய்யவும்",
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("விண்ணப்பதாரர் பெயர் *") },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("தொலைபேசி எண் *") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = district,
                        onValueChange = { district = it },
                        label = { Text("மாவட்டம் *") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = aadhaar,
                        onValueChange = { aadhaar = it },
                        label = { Text("ஆதார் எண் *") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = rationCard,
                        onValueChange = { rationCard = it },
                        label = { Text("ஸ்மார்ட் ரேஷன் கார்டு எண்") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = trade,
                    onValueChange = { trade = it },
                    label = { Text("தொழில் வகை (எ.கா: பெயிண்டர் / சுவர் ஓவியர்)") },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = nomineeName,
                    onValueChange = { nomineeName = it },
                    label = { Text("வாரிசுதாரர் பெயர் & உறவு முறை (Nominee Details)") },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (name.isBlank() || phone.isBlank()) {
                            Toast.makeText(context, "தயவுசெய்து பெயர் மற்றும் போன் எண்ணை உள்ளிடவும்", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        val message = "🏛️ *TNPA2 தமிழ்நாடு நலவாரிய புதிய ஆன்லைன் விண்ணப்பம்*\n\n👤 *பெயர்:* $name\n📱 *போன்:* $phone\n📍 *மாவட்டம்:* $district\n🆔 *ஆதார்:* $aadhaar\n💳 *ரேஷன் கார்டு:* $rationCard\n🎨 *தொழில்:* $trade\n👥 *வாரிசுதாரர்:* $nomineeName"
                        val url = "https://api.whatsapp.com/send?phone=917010131915&text=${Uri.encode(message)}"
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        onSubmitted(message)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UnionRedDark, contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "Submit")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "நலவாரிய விண்ணப்பத்தை சமர்ப்பி (Submit Online)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun SchemeApplicationDialog(
    scheme: WelfareSchemeEntity,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("மதுரை") }
    var remarks by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "திட்ட விண்ணப்பம்",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = UnionRedDark
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Surface(
                    color = UnionRedDark.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(text = scheme.titleTamil, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UnionRedDark)
                        Text(text = "பயன்: ${scheme.benefitsAmount}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("விண்ணப்பதாரர் பெயர் *") },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("தொலைபேசி எண் *") },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = district,
                    onValueChange = { district = it },
                    label = { Text("மாவட்டம் *") },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = remarks,
                    onValueChange = { remarks = it },
                    label = { Text("கூடுதல் விபரம் / குறிப்பு") },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (name.isBlank() || phone.isBlank()) {
                            Toast.makeText(context, "தயவுசெய்து பெயர் மற்றும் போன் எண்ணை உள்ளிடவும்", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        val message = "📋 *TNPA2 அரசு நலத்திட்ட விண்ணப்பம்*\n\n🎯 *திட்டம்:* ${scheme.titleTamil}\n👤 *பெயர்:* $name\n📱 *போன்:* $phone\n📍 *மாவட்டம்:* $district\n💬 *குறிப்பு:* $remarks"
                        val url = "https://api.whatsapp.com/send?phone=917010131915&text=${Uri.encode(message)}"
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        Toast.makeText(context, "விண்ணப்பம் அனுப்பப்பட்டது!", Toast.LENGTH_LONG).show()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32), contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "சமர்ப்பி & வாட்ஸ்அப்பில் அனுப்பு", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
