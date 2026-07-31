package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.TnpaViewModel
import com.example.ui.theme.UnionGoldBright
import com.example.ui.theme.UnionRedDark
import com.example.ui.theme.UnionRedPrimary

@Composable
fun AdminPanelScreen(
    viewModel: TnpaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isTamil by viewModel.isTamil.collectAsState()
    val pendingMembers by viewModel.pendingMembers.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0 = Approvals, 1 = Post News, 2 = Push Notification

    var newsTitle by remember { mutableStateOf("") }
    var newsContent by remember { mutableStateOf("") }
    var pushMsg by remember { mutableStateOf("") }

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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Admin",
                        tint = UnionGoldBright,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isTamil) "சங்க நிருவாகி மேலாண்மை மையம்" else "Union Admin Management Panel",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = UnionGoldBright
                    )
                }
                Text(
                    text = if (isTamil) "மாநில மற்றும் மாவட்ட நிர்வாகிகளுக்கான ஒப்புதல் மையம்" else "Approval portal & publishing control for State & District Leaders",
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
                text = { Text(text = "ஒப்புதல் (${pendingMembers.size})", fontWeight = FontWeight.Bold) },
                icon = { Icon(imageVector = Icons.Default.HowToReg, contentDescription = "Reg") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text(text = "செய்தி பதிவு", fontWeight = FontWeight.Bold) },
                icon = { Icon(imageVector = Icons.Default.Campaign, contentDescription = "News") }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text(text = "அறிவிப்பு", fontWeight = FontWeight.Bold) },
                icon = { Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notify") }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (selectedTab == 0) {
            if (pendingMembers.isEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "ஒப்புதலுக்கு நிலுவையில் உள்ள புதிய விண்ணப்பங்கள் எதுவுமில்லை ✓",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(pendingMembers) { member ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = member.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "ID: ${member.memberId} • ${member.district}", fontSize = 12.sp, color = UnionRedDark)
                                    Text(text = "தொழில்: ${member.trade} • 📞 ${member.phone}", fontSize = 12.sp)
                                }

                                Row {
                                    IconButton(
                                        onClick = {
                                            viewModel.approveMember(member.id)
                                            Toast.makeText(context, "${member.name} - ஒப்புதல் அளிக்கப்பட்டது ✓", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.background(Color(0xFF2E7D32), RoundedCornerShape(8.dp))
                                    ) {
                                        Icon(imageVector = Icons.Default.Check, contentDescription = "Approve", tint = Color.White)
                                    }

                                    Spacer(modifier = Modifier.width(6.dp))

                                    IconButton(
                                        onClick = {
                                            viewModel.rejectMember(member.id)
                                            Toast.makeText(context, "${member.name} - நிராகரிக்கப்பட்டது", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.background(UnionRedPrimary, RoundedCornerShape(8.dp))
                                    ) {
                                        Icon(imageVector = Icons.Default.Close, contentDescription = "Reject", tint = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (selectedTab == 1) {
            Column {
                OutlinedTextField(
                    value = newsTitle,
                    onValueChange = { newsTitle = it },
                    label = { Text("செய்தி தலைப்பு (News Title) *") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = newsContent,
                    onValueChange = { newsContent = it },
                    label = { Text("செய்தி விவரம் (Content) *") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (newsTitle.isNotBlank() && newsContent.isNotBlank()) {
                            viewModel.postNews(newsTitle, newsContent, "மாநில செய்தி", true)
                            newsTitle = ""
                            newsContent = ""
                            Toast.makeText(context, "செய்தி வெற்றி பெற்று வெளியானது!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UnionRedPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "செய்தியை வெளியிடவும் (Publish News)")
                }
            }
        } else {
            Column {
                OutlinedTextField(
                    value = pushMsg,
                    onValueChange = { pushMsg = it },
                    label = { Text("அனைத்து உறுப்பினர்களுக்கும் புஷ் செய்தி (Push Notification) *") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (pushMsg.isNotBlank()) {
                            Toast.makeText(context, "38 மாவட்ட 42,500+ உறுப்பினர்களுக்கும் செய்தி அனுப்பப்பட்டது! 📲", Toast.LENGTH_LONG).show()
                            pushMsg = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UnionRedPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "அனைவருக்கும் புஷ் செய்தி அனுப்பு (Send Push Alert)")
                }
            }
        }
    }
}
