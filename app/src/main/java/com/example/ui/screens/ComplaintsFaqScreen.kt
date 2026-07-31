package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.TnpaViewModel
import com.example.ui.theme.UnionGoldBright
import com.example.ui.theme.UnionRedDark
import com.example.ui.theme.UnionRedPrimary

@Composable
fun ComplaintsFaqScreen(
    viewModel: TnpaViewModel,
    modifier: Modifier = Modifier
) {
    val isTamil by viewModel.isTamil.collectAsState()
    val compSuccessNo by viewModel.complaintSuccessNo.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0 = Complaint Form, 1 = FAQ

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("சென்னை") }
    var category by remember { mutableStateOf("கூலி பாக்கி பிரச்சனை") }
    var subject by remember { mutableStateOf("") }
    var detail by remember { mutableStateOf("") }

    val faqList = listOf(
        FaqItem(
            qTamil = "TNPA² சங்கத்தில் உறுப்பினராக சேர என்ன கட்டணம்?",
            aTamil = "ஆண்டு சந்தா கட்டணம் ₹100 மட்டுமே. பதிவு செய்தவுடன் டிஜிட்டல் ID கார்டு மற்றும் சங்க நலத்திட்ட உரிமைகள் உடனடியாக வழங்கப்படும்."
        ),
        FaqItem(
            qTamil = "விபத்து காப்பீடு உதவித்தொகை பெறுவது எப்படி?",
            aTamil = "விபத்து நேரிட்டால் உடனடியாக சங்க மாவட்டச் செயலாளரை தொடர்பு கொள்ளவும். FIR மற்றும் மருத்துவமனை சான்றிதழ் மூலம் ரூ.5 லட்சம் வரையிலான நிதி பெற உதவப்படும்."
        ),
        FaqItem(
            qTamil = "டிஜிட்டல் அடையாள அட்டையை எவ்வாறு சரிபார்ப்பது?",
            aTamil = "பயன்பாட்டில் உள்ள 'ID சரிபார்ப்பு' பக்கத்தில் உங்கள் உறுப்பினர் எண் அல்லது மொபைல் எண்ணை உள்ளிட்டு சரிபார்க்கலாம்."
        ),
        FaqItem(
            qTamil = "கூலி தராமல் ஏமாற்றும் ஒப்பந்ததாரர் மீது புகார் அளிப்பது எப்படி?",
            aTamil = "இப்பக்கத்தில் உள்ள 'புகார் பதிவு' படிவத்தைப் பூர்த்தி செய்து சமர்ப்பிக்கவும். எங்கள் சங்க சட்டக் குழு நேரடியாகத் தலையிட்டு கூலியைப் பெற்றுத்தரும்."
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Banner Header
        Surface(
            color = UnionRedDark,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isTamil) "தொழிலாளர் புகார் பதிவு & அடிக்கடி கேட்கப்படும் கேள்விகள்" else "Labor Complaint Portal & FAQ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = UnionGoldBright
                )
                Text(
                    text = if (isTamil) "கூலி பிரச்சனை, விபத்து பாதுகாப்பு மற்றும் சங்கம் தொடர்பான உதவிகள்" else "Support for wage disputes, workplace safety, and union queries",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text(text = if (isTamil) "புகார் பதிவு (Complaint)" else "File Complaint", fontWeight = FontWeight.Bold) },
                icon = { Icon(imageVector = Icons.Default.ReportProblem, contentDescription = "Complaint") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text(text = if (isTamil) "கேள்வி & பதில்கள் (FAQ)" else "FAQ", fontWeight = FontWeight.Bold) },
                icon = { Icon(imageVector = Icons.Default.HelpOutline, contentDescription = "FAQ") }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (selectedTab == 0) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                compSuccessNo?.let { ticketNo ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Default.Verified, contentDescription = "Success", tint = Color(0xFF2E7D32), modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "புகார் வெற்றிகரமாகப் பெறப்பட்டது!", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
                            Text(text = "புகார் எண் (Ticket No): $ticketNo", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = UnionRedDark)
                            Text(text = "சங்க மாவட்டச் செயலாளர் உங்களை விரைவில் தொடர்பு கொள்வார்.", fontSize = 12.sp, color = Color.DarkGray)
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { viewModel.resetComplaintState() },
                                colors = ButtonDefaults.buttonColors(containerColor = UnionRedPrimary)
                            ) {
                                Text(text = "புதிய புகார் பதிவு")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("உங்கள் பெயர் *") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("தொலைபேசி எண் *") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = district,
                    onValueChange = { district = it },
                    label = { Text("மாவட்டத்தின் பெயர் *") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("புகாரின் தலைப்பு (Subject) *") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = detail,
                    onValueChange = { detail = it },
                    label = { Text("புகாரின் முழு விபரம் (Detail) *") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.submitComplaint(
                            memberName = name,
                            phone = phone,
                            district = district,
                            category = category,
                            subject = subject,
                            detail = detail
                        )
                    },
                    enabled = name.isNotBlank() && phone.isNotBlank() && subject.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = UnionRedPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "புகாரை சமர்ப்பிக்கவும் (Submit Complaint)", fontWeight = FontWeight.Bold)
                }
            }
        } else {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                faqList.forEach { item ->
                    FaqCard(item = item)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

private data class FaqItem(val qTamil: String, val aTamil: String)

@Composable
private fun FaqCard(item: FaqItem) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        onClick = { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "❓ ${item.qTamil}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = "Expand"
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Text(
                        text = item.aTamil,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
