package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.StateExecutiveEntity
import com.example.ui.TnpaViewModel
import com.example.ui.components.UnionFlagBadge
import com.example.ui.theme.UnionGoldAccent
import com.example.ui.theme.UnionGoldBright
import com.example.ui.theme.UnionRedDark
import com.example.ui.theme.UnionRedPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateExecutivesScreen(
    viewModel: TnpaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isTamil by viewModel.isTamil.collectAsState()
    val stateExecList by viewModel.stateExecutivesList.collectAsState()
    val districtLeadersList by viewModel.districtLeaders.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: மாநில நிர்வாகிகள், 1: மாவட்ட நிர்வாகிகள், 2: ஒன்றிய/பகுதி நிர்வாகிகள்
    var searchQuery by remember { mutableStateOf("") }

    val filteredStateExecs = stateExecList.filter {
        it.nameTamil.contains(searchQuery, ignoreCase = true) ||
                it.designationTamil.contains(searchQuery, ignoreCase = true) ||
                it.district.contains(searchQuery, ignoreCase = true) ||
                it.phone.contains(searchQuery, ignoreCase = true)
    }

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isTamil) "சங்க நிர்வாகிகள் போர்ட்டல்" else "Union Executive Office Bearers Portal",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = UnionGoldBright
                        )
                        Text(
                            text = if (isTamil) "மாநில, மாவட்ட மற்றும் ஒன்றிய நிர்வாகிகள் தொடர்புகள்" else "State, District & Union Zone Executive Leadership Directory",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                    UnionFlagBadge(width = 48.dp, height = 32.dp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // HQ Quick Bar
                Surface(
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhoneInTalk,
                            contentDescription = "TollFree",
                            tint = UnionGoldBright,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isTamil) "இலவச உதவி எண்: 1800-425-7010 | தொடர்பு: 7010131915" else "Toll-Free: 1800-425-7010 | Helpline: 7010131915",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
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
                text = {
                    Text(
                        text = if (isTamil) "மாநில நிர்வாகிகள்" else "State Executives",
                        fontSize = 12.sp,
                        fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = {
                    Text(
                        text = if (isTamil) "மாவட்ட நிர்வாகிகள்" else "District Leaders",
                        fontSize = 12.sp,
                        fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
            Tab(
                selected = selectedTabIndex == 2,
                onClick = { selectedTabIndex = 2 },
                text = {
                    Text(
                        text = if (isTamil) "ஒன்றிய / பகுதி" else "Union / Zone",
                        fontSize = 12.sp,
                        fontWeight = if (selectedTabIndex == 2) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(if (isTamil) "நிர்வாகி பெயர், பதவி அல்லது மாவட்டம்..." else "Search executive name, designation, district...") },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedTabIndex) {
            0 -> {
                // State Executives Page
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        TopThreeLeadersBanner(isTamil = isTamil)
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    items(filteredStateExecs) { exec ->
                        StateExecutiveCard(exec = exec, isTamil = isTamil)
                    }
                }
            }

            1 -> {
                // District Executives List
                val filteredDistricts = districtLeadersList.filter {
                    it.districtTamil.contains(searchQuery, ignoreCase = true) ||
                    it.districtEnglish.contains(searchQuery, ignoreCase = true) ||
                    it.secretaryNameTamil.contains(searchQuery, ignoreCase = true) ||
                    it.presidentNameTamil.contains(searchQuery, ignoreCase = true)
                }
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredDistricts) { district ->
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
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Dist", tint = UnionRedPrimary, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = "${district.districtTamil} (${district.districtEnglish})", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Surface(color = UnionRedPrimary.copy(alpha = 0.15f), shape = RoundedCornerShape(6.dp)) {
                                        Text(text = "${district.totalMembers} ${if (isTamil) "உறுப்பினர்கள்" else "Members"}", fontSize = 11.sp, color = UnionRedDark, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "👤 ${if (isTamil) "மாவட்டச் செயலாளர்" else "Secretary"}: ${district.secretaryNameTamil}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                Text(text = "👤 ${if (isTamil) "மாவட்டத் தலைவர்" else "President"}: ${district.presidentNameTamil}", fontSize = 13.sp)
                                Text(text = "🏢 ${district.officeAddress}", fontSize = 11.5.sp, color = Color.Gray)

                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Call Button
                                    Surface(
                                        onClick = {
                                            val cleanPhone = district.phone.replace(" ", "").replace("-", "")
                                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                                data = Uri.parse("tel:$cleanPhone")
                                            }
                                            context.startActivity(intent)
                                        },
                                        color = UnionRedDark,
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(imageVector = Icons.Default.Call, contentDescription = "Call", tint = Color.White, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(text = if (isTamil) "அழைக்க (${district.phone})" else "Call", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
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
                                        color = Color(0xFF25D366),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(imageVector = Icons.Default.Message, contentDescription = "WhatsApp", tint = Color.White, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(text = "WhatsApp", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // Union / Zone Executives
                val zoneExecutives = listOf(
                    Triple("மேலூர் ஒன்றியத் தலைவர்", "K. கருப்பையா", "98421 11002"),
                    Triple("ஒத்தக்கடை பகுதிச் செயலாளர்", "M. செல்வராஜ்", "94430 22334"),
                    Triple("திருச்சி நகர்ப்புற பகுதி அமைப்பாளர்", "T. ஆரோக்கியசாமி", "98940 33445"),
                    Triple("கோவை தெற்கு ஒன்றியச் செயலாளர்", "S. ரமேஷ்", "98422 44556"),
                    Triple("சென்னை அண்ணா நகர் பகுதி தலைவர்", "R. விஜயகுமார்", "98401 55667"),
                    Triple("சேலம் மத்திய ஒன்றிய அமைப்பாளர்", "P. சண்முகம்", "98420 66778")
                ).filter {
                    it.first.contains(searchQuery, ignoreCase = true) ||
                            it.second.contains(searchQuery, ignoreCase = true) ||
                            it.third.contains(searchQuery, ignoreCase = true)
                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(zoneExecutives) { zone ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = UnionGoldAccent.copy(alpha = 0.2f),
                                    shape = CircleShape,
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(imageVector = Icons.Default.Badge, contentDescription = "Zone", tint = UnionRedDark)
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = zone.second, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                    Text(text = zone.first, fontSize = 12.sp, color = UnionRedDark, fontWeight = FontWeight.SemiBold)
                                    Text(text = "📞 ${zone.third}", fontSize = 11.sp, color = Color.Gray)
                                }
                                Surface(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL).apply {
                                            data = Uri.parse("tel:${zone.third.replace(" ", "")}")
                                        }
                                        context.startActivity(intent)
                                    },
                                    color = UnionRedPrimary,
                                    shape = CircleShape,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(imageVector = Icons.Default.Call, contentDescription = "Call", tint = Color.White, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StateExecutiveCard(
    exec: StateExecutiveEntity,
    isTamil: Boolean
) {
    val context = LocalContext.current

    val (badgeIcon, badgeColor, badgeLabel) = when (exec.badgeType) {
        "GENERAL_SECRETARY" -> Triple(Icons.Default.WorkspacePremium, Color(0xFFD81B60), "மாநில பொதுச்செயலாளர் LOGO")
        "CROWN" -> Triple(Icons.Default.VerifiedUser, UnionRedDark, "மாநில தலைவர் LOGO")
        "TREASURER" -> Triple(Icons.Default.AccountBalance, Color(0xFF2E7D32), "மாநில பொருளாளர் LOGO")
        "VICE_PRESIDENT" -> Triple(Icons.Default.Star, Color(0xFF1976D2), "துணைத் தலைவர் LOGO")
        "JOINT_SECRETARY" -> Triple(Icons.Default.MenuBook, Color(0xFF7B1FA2), "இணைச் செயலாளர் LOGO")
        "ORGANIZER" -> Triple(Icons.Default.Campaign, Color(0xFFE65100), "அமைப்பாளர் LOGO")
        "PRESS" -> Triple(Icons.Default.Newspaper, Color(0xFF00838F), "செய்தித் தொடர்பாளர் LOGO")
        "LEGAL" -> Triple(Icons.Default.Gavel, Color(0xFF424242), "சட்ட ஆலோசகர் LOGO")
        else -> Triple(Icons.Default.Shield, UnionRedPrimary, "நிர்வாகி LOGO")
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Executive Profile Photo or Leader Graphic
                if (exec.nameTamil.contains("சேவியர் பாபு")) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_leader_xavier_babu),
                        contentDescription = exec.nameTamil,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(2.dp, UnionGoldBright, CircleShape)
                    )
                } else if (exec.nameTamil.contains("மைக்கேல் ஆல்வின்")) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_leader_president),
                        contentDescription = exec.nameTamil,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(2.dp, UnionGoldBright, CircleShape)
                    )
                } else if (exec.nameTamil.contains("சக்திவேல்")) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_leader_treasurer),
                        contentDescription = exec.nameTamil,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(2.dp, UnionGoldBright, CircleShape)
                    )
                } else {
                    Surface(
                        color = badgeColor.copy(alpha = 0.15f),
                        shape = CircleShape,
                        modifier = Modifier
                            .size(54.dp)
                            .border(2.dp, badgeColor, CircleShape)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = exec.nameTamil.take(1),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = badgeColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = exec.nameTamil,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        // Designation Role Logo Badge next to Name!
                        Surface(
                            color = badgeColor,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = badgeIcon,
                                    contentDescription = badgeLabel,
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = exec.badgeType.take(4),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = exec.designationTamil,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor
                    )

                    Text(
                        text = "📍 தலைமை / மாவட்டம்: ${exec.district}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))

            // Phone and Call Button Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Phone",
                        tint = UnionRedDark,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "தொலைபேசி: ${exec.phone}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${exec.phone.replace(" ", "")}")
                        }
                        context.startActivity(intent)
                    },
                    color = UnionRedDark,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isTamil) "அழைக்க" else "Call",
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

@Composable
private fun TopThreeLeadersBanner(isTamil: Boolean) {
    Card(
        colors = CardDefaults.cardColors(containerColor = UnionRedDark),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = if (isTamil) "🏆 முதன்மை மாநிலத் தலைவர்கள் (Key State Leadership)" else "🏆 Key State Leadership",
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = UnionGoldBright
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 1. President
                LeaderMiniCard(
                    name = "S. மைக்கேல் ஆல்வின்",
                    role = "மாநில தலைவர்",
                    imageRes = R.drawable.ic_leader_president
                )
                // 2. General Secretary
                LeaderMiniCard(
                    name = "ரா. சேவியர் பாபு",
                    role = "மாநில பொதுச்செயலாளர்",
                    imageRes = R.drawable.ic_leader_xavier_babu
                )
                // 3. Treasurer
                LeaderMiniCard(
                    name = "R. சக்திவேல்",
                    role = "மாநில பொருளாளர்",
                    imageRes = R.drawable.ic_leader_treasurer
                )
            }
        }
    }
}

@Composable
private fun LeaderMiniCard(name: String, role: String, imageRes: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(102.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = name,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .border(2.5.dp, UnionGoldBright, CircleShape)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = name,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = role,
            fontSize = 9.sp,
            color = UnionGoldBright,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
