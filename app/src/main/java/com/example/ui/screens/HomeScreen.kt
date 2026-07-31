package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Volcano
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.R
import com.example.ui.TnpaViewModel
import com.example.ui.components.EmergencyHelplineCard
import com.example.ui.components.RunningNewsTickerBar
import com.example.ui.components.UnionFlagBadge
import com.example.ui.components.UnionLogoBadge
import com.example.ui.components.WavingUnionFlagBackground
import com.example.ui.theme.UnionGoldAccent
import com.example.ui.theme.UnionGoldBright
import com.example.ui.theme.UnionRedDark
import com.example.ui.theme.UnionRedPrimary

@Composable
fun HomeScreen(
    viewModel: TnpaViewModel,
    onNavigateToRegister: () -> Unit,
    onNavigateToAi: () -> Unit,
    onNavigateToIdVerification: () -> Unit,
    onNavigateToDistrictLeaders: () -> Unit,
    onNavigateToWelfare: () -> Unit,
    onNavigateToComplaints: () -> Unit,
    onNavigateToJobs: () -> Unit,
    onNavigateToEvents: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isTamil by viewModel.isTamil.collectAsState()
    val approvedDbCount by viewModel.liveMemberCount.collectAsState()
    val newsList by viewModel.newsList.collectAsState()
    val eventsList by viewModel.eventsList.collectAsState()
    val districtList by viewModel.districtLeaders.collectAsState()

    val totalLiveCount = 42580 + approvedDbCount
    val tickerText = newsList.firstOrNull()?.titleTamil ?: "TNPA² மாநில மாநாடு திருச்சியில் மிக பிரம்மாண்டமாக நடைபெற உள்ளது!"

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. Running Live News Ticker
        item {
            RunningNewsTickerBar(newsText = tickerText, isTamil = isTamil)
        }

        // 2. Full-Screen / Top Hero Banner
        item {
            HeroBannerSection(
                isTamil = isTamil,
                onJoinClick = onNavigateToRegister,
                onAskAiClick = onNavigateToAi
            )
        }

        // 3. Live Total Member Counter Banner
        item {
            LiveMemberCounterSection(
                totalCount = totalLiveCount,
                isTamil = isTamil
            )
        }

        // 4. Quick Services & Feature Action Cards
        item {
            QuickServicesGrid(
                isTamil = isTamil,
                onNavigateToRegister = onNavigateToRegister,
                onNavigateToIdVerification = onNavigateToIdVerification,
                onNavigateToWelfare = onNavigateToWelfare,
                onNavigateToComplaints = onNavigateToComplaints,
                onNavigateToJobs = onNavigateToJobs
            )
        }

        // 5. District Wise Membership Summary View
        item {
            DistrictSummarySection(
                districtList = districtList,
                isTamil = isTamil,
                onViewAllClick = onNavigateToDistrictLeaders
            )
        }

        // 6. Upcoming Events Section
        item {
            UpcomingEventsSection(
                events = eventsList,
                isTamil = isTamil,
                onViewAllClick = onNavigateToEvents
            )
        }

        // 7. Emergency Helpline Module
        item {
            PaddingValues(horizontal = 16.dp, vertical = 12.dp).let {
                Box(modifier = Modifier.padding(it)) {
                    EmergencyHelplineCard(
                        onCallClick = { /* Emergency dialer */ },
                        isTamil = isTamil
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroBannerSection(
    isTamil: Boolean,
    onJoinClick: () -> Unit,
    onAskAiClick: () -> Unit
) {
    WavingUnionFlagBackground(
        modifier = Modifier.fillMaxWidth(),
        alpha = 0.35f
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            UnionRedDark.copy(alpha = 0.85f),
                            UnionRedPrimary.copy(alpha = 0.85f),
                            Color(0xFF800000).copy(alpha = 0.9f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
            // Logo & Flag Header Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                UnionLogoBadge(size = 56.dp)
                Spacer(modifier = Modifier.width(10.dp))
                UnionFlagBadge(width = 56.dp, height = 38.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "TNPA²",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = UnionGoldBright,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = if (isTamil) "தமிழ்நாடு பெயிண்டர்கள் மற்றும் ஓவியர்கள் முன்னேற்ற சங்கம்" else "Tamil Nadu Painters & Artists Association",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Union Slogan Badge
            Surface(
                color = Color.Black.copy(alpha = 0.35f),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, UnionGoldAccent)
            ) {
                Text(
                    text = if (isTamil) "“ உரிமையை மீட்போம் – ஒன்றுபடுவோம் – தொழிலாளர்களைக் காப்போம் ”" else "“ Reclaim Rights – Unite – Protect Laborers ”",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = UnionGoldBright,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isTamil) "தமிழ்நாட்டின் 38 மாவட்ட பெயிண்டர்கள் மற்றும் ஓவியர்களின் அதிகாரப்பூர்வ சங்கம்" else "Official Union for Painters & Artists across 38 Districts of Tamil Nadu",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Hero Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Join Now Button
                Button(
                    onClick = onJoinClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UnionGoldBright,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = "Join Union",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isTamil) "உறுப்பினராக சேருங்கள்" else "Join Union",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Xavier Babu AI Button
                Button(
                    onClick = onAskAiClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = UnionRedDark
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Ask Xavier Babu AI",
                        tint = UnionRedDark,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isTamil) "சேவியர் பாபு AI" else "Ask Xavier AI",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
}

@Composable
private fun LiveMemberCounterSection(
    totalCount: Int,
    isTamil: Boolean
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(UnionRedPrimary.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = "Members",
                    tint = UnionRedPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color(0xFF4CAF50), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isTamil) "மொத்த உறுப்பினர்கள் (Live Registry)" else "Total Members (Live Counter)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "$totalCount +",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = UnionRedPrimary
                )
            }

            Surface(
                color = UnionGoldBright.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, UnionGoldBright)
            ) {
                Text(
                    text = if (isTamil) "38 மாவட்டங்கள்" else "38 Districts",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5C4000),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun QuickServicesGrid(
    isTamil: Boolean,
    onNavigateToRegister: () -> Unit,
    onNavigateToIdVerification: () -> Unit,
    onNavigateToWelfare: () -> Unit,
    onNavigateToComplaints: () -> Unit,
    onNavigateToJobs: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = if (isTamil) "முக்கிய சேவைகள்" else "Core Union Services",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickServiceCard(
                title = if (isTamil) "உறுப்பினர் ID சரிபார்ப்பு" else "ID Verification",
                subtitle = if (isTamil) "அட்டை பதிவிறக்கம்" else "Download Card",
                icon = Icons.Default.Badge,
                color = UnionRedPrimary,
                onClick = onNavigateToIdVerification,
                modifier = Modifier.weight(1f)
            )
            QuickServiceCard(
                title = if (isTamil) "அரசின் நலத்திட்டங்கள்" else "Welfare Schemes",
                subtitle = if (isTamil) "₹5 லட்சம் காப்பீடு" else "Board Benefits",
                icon = Icons.Default.Shield,
                color = Color(0xFF1E88E5),
                onClick = onNavigateToWelfare,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickServiceCard(
                title = if (isTamil) "புகார் பதிவு" else "Complaints",
                subtitle = if (isTamil) "கூலி & பாதுகாப்பு" else "Labor Rights",
                icon = Icons.Default.ReportProblem,
                color = Color(0xFFD81B60),
                onClick = onNavigateToComplaints,
                modifier = Modifier.weight(1f)
            )
            QuickServiceCard(
                title = if (isTamil) "வேலை & பயிற்சி" else "Jobs & Training",
                subtitle = if (isTamil) "ஒப்பந்த வேலைகள்" else "Skills Board",
                icon = Icons.Default.School,
                color = Color(0xFF43A047),
                onClick = onNavigateToJobs,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickServiceCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun DistrictSummarySection(
    districtList: List<com.example.data.local.DistrictLeaderEntity>,
    isTamil: Boolean,
    onViewAllClick: () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isTamil) "மாவட்ட வாரியான விவரங்கள்" else "District Summary View",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = if (isTamil) "அனைத்தும் >" else "View All >",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = UnionRedPrimary,
                modifier = Modifier.clickable { onViewAllClick() }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(districtList) { district ->
                Card(
                    onClick = onViewAllClick,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.width(160.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "District",
                                tint = UnionRedPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = district.districtTamil,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isTamil) "செயலாளர்: ${district.secretaryNameTamil}" else "Sec: ${district.secretaryNameTamil}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = UnionRedPrimary.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "${district.totalMembers} ${if (isTamil) "உறுப்பினர்கள்" else "Members"}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = UnionRedDark,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UpcomingEventsSection(
    events: List<com.example.data.local.EventEntity>,
    isTamil: Boolean,
    onViewAllClick: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isTamil) "அடுத்தடுத்த நிகழ்வுகள்" else "Upcoming Events",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = if (isTamil) "அனைத்தும் >" else "View All >",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = UnionRedPrimary,
                modifier = Modifier.clickable { onViewAllClick() }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        events.take(2).forEach { event ->
            Card(
                onClick = onViewAllClick,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = UnionGoldAccent.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.size(54.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Date",
                                tint = UnionRedDark,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = event.date.split(" ").take(2).joinToString(" "),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = UnionRedDark,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = event.titleTamil,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "📍 ${event.locationTamil} • ⏰ ${event.time}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
