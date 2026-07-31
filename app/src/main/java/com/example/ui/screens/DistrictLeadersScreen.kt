package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.TnpaViewModel
import com.example.ui.theme.UnionGoldBright
import com.example.ui.theme.UnionRedDark
import com.example.ui.theme.UnionRedPrimary

@Composable
fun DistrictLeadersScreen(
    viewModel: TnpaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isTamil by viewModel.isTamil.collectAsState()
    val leadersList by viewModel.districtLeaders.collectAsState()
    var filterQuery by remember { mutableStateOf("") }

    val filteredList = leadersList.filter {
        it.districtTamil.contains(filterQuery, ignoreCase = true) ||
        it.districtEnglish.contains(filterQuery, ignoreCase = true) ||
        it.secretaryNameTamil.contains(filterQuery, ignoreCase = true) ||
        it.presidentNameTamil.contains(filterQuery, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Header
        Surface(
            color = UnionRedDark,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isTamil) "38 மாவட்ட நிர்வாகிகள் மேலாண்மை" else "38 District Leadership Directory",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = UnionGoldBright
                )
                Text(
                    text = if (isTamil) "தமிழ்நாட்டின் 38 மாவட்டச் செயலாளர்கள் & தலைவர்கள் தொடர்புகள் (தொலைபேசி & WhatsApp)" else "Contact info, Phone & WhatsApp for all 38 District Secretaries & Presidents",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = filterQuery,
                    onValueChange = { filterQuery = it },
                    placeholder = { Text(if (isTamil) "மாவட்டத்தின் பெயர் அல்லது தலைவர் பெயரைத் தேடவும்..." else "Search District or Leader Name...") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .testTag("district_search_input")
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isTamil) "மாவட்ட நிர்வாகிகள் பட்டியல் (${filteredList.size} / 38)" else "District Leaders (${filteredList.size} / 38)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredList) { district ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "District",
                                    tint = UnionRedPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${district.districtTamil} (${district.districtEnglish})",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Surface(
                                color = UnionRedPrimary.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "${district.totalMembers} ${if (isTamil) "உறுப்பினர்கள்" else "Members"}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = UnionRedDark,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "👤 ${if (isTamil) "மாவட்டச் செயலாளர்" else "District Secretary"}: ${district.secretaryNameTamil}",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "👤 ${if (isTamil) "மாவட்டத் தலைவர்" else "District President"}: ${district.presidentNameTamil}",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "🏢 ${district.officeAddress}",
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.outline
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Clickable Action Buttons Row: Call & WhatsApp
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Phone Call Button
                            Surface(
                                onClick = {
                                    val cleanPhone = district.phone.replace(" ", "").replace("-", "")
                                    val intent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:$cleanPhone")
                                    }
                                    context.startActivity(intent)
                                },
                                color = UnionRedDark,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = "Call",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isTamil) "அழைக்க" else "Call Now",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            // WhatsApp Button
                            Surface(
                                onClick = {
                                    val cleanPhone = district.phone.replace(" ", "").replace("-", "")
                                    val formattedPhone = if (cleanPhone.startsWith("91")) cleanPhone else "91$cleanPhone"
                                    val message = Uri.encode("வணக்கம் TNPA² ${district.districtTamil} மாவட்ட தலைவரே / செயலாளரே,")
                                    val waUrl = "https://api.whatsapp.com/send?phone=$formattedPhone&text=$message"
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        data = Uri.parse(waUrl)
                                    }
                                    context.startActivity(intent)
                                },
                                color = Color(0xFF25D366), // Official WhatsApp Green
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Message,
                                        contentDescription = "WhatsApp",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "WhatsApp",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

