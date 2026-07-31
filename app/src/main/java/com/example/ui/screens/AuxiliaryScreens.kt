package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.TnpaViewModel
import com.example.ui.components.CanvasQrCode
import com.example.ui.components.UnionFlagBadge
import com.example.ui.theme.UnionGoldBright
import com.example.ui.theme.UnionRedDark
import com.example.ui.theme.UnionRedPrimary

// About & State Executive Leadership Screen
@Composable
fun AboutUnionScreen(
    viewModel: TnpaViewModel,
    modifier: Modifier = Modifier
) {
    val isTamil by viewModel.isTamil.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Surface(
            color = UnionRedDark,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UnionFlagBadge(width = 72.dp, height = 48.dp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "TNPA² - தமிழ்நாடு பெயிண்டர்கள் மற்றும் ஓவியர்கள் முன்னேற்ற சங்கம்",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = UnionGoldBright,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Text(
                    text = "“ உரிமையை மீட்போம் – ஒன்றுபடுவோம் – தொழிலாளர்களைக் காப்போம் ”",
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "சங்க வரலாறும் நோக்கமும் (About Union)", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UnionRedDark)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "தமிழ்நாடு முழுவதும் உள்ள கட்டிட பெயிண்டர்கள், சுவர் ஓவியர்கள், சைன்போர்டு மற்றும் டிஜிட்டல் ஆர்ட்டிஸ்டுகளின் வாழ்வாதாரத்தைப் பாதுகாக்கவும், நியாயமான கூலி, பணி பாதுகாப்பு மற்றும் அரசு நலவாரிய பயன்களை பெற்றுத் தரவும் தொடங்கப்பட்ட அதிகாரப்பூர்வ சங்கம் TNPA² ஆகும்.",
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "மாநில தலைவர்கள் (State Executive Leadership)", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)

        Spacer(modifier = Modifier.height(10.dp))

        LeaderProfileCard(name = "ரா. சேவியர் பாபு", role = "மாநில பொதுச்செயலாளர்", phone = "7010131915")
        Spacer(modifier = Modifier.height(8.dp))
        LeaderProfileCard(name = "S. மைக்கேல் ஆல்வின்", role = "மாநில தலைவர்", phone = "+91 98400 98765")
        Spacer(modifier = Modifier.height(8.dp))
        LeaderProfileCard(name = "R. சக்திவேல்", role = "மாநில பொருளாளர்", phone = "+91 94430 11223")
        Spacer(modifier = Modifier.height(8.dp))
        LeaderProfileCard(name = "முத்துக்குமார்", role = "மாநில துணைத் தலைவர்", phone = "+91 98940 12345")
        Spacer(modifier = Modifier.height(8.dp))
        LeaderProfileCard(name = "சீனிவாசன்", role = "மாநில நிர்வாக செயலாளர்", phone = "+91 98421 88990")
        Spacer(modifier = Modifier.height(8.dp))
        LeaderProfileCard(name = "சக்கரவர்த்தி", role = "மாநில செய்தித் தொடர்பாளர்", phone = "+91 94431 99887")
    }
}

@Composable
private fun LeaderProfileCard(name: String, role: String, phone: String) {
    val imageRes = when {
        name.contains("மைக்கேல் ஆல்வின்") -> R.drawable.ic_leader_president
        name.contains("சேவியர் பாபு") -> R.drawable.ic_leader_xavier_babu
        name.contains("சக்திவேல்") -> R.drawable.ic_leader_treasurer
        else -> R.drawable.ic_leader_xavier_babu
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .border(2.dp, com.example.ui.theme.UnionGoldBright, CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = name, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(text = role, fontSize = 12.sp, color = UnionRedDark, fontWeight = FontWeight.SemiBold)
                Text(text = "📞 $phone", fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}

// News & Events Combined Screen
@Composable
fun NewsEventsScreen(
    viewModel: TnpaViewModel,
    modifier: Modifier = Modifier
) {
    val newsList by viewModel.newsList.collectAsState()
    val eventsList by viewModel.eventsList.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Surface(
                color = UnionRedDark,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "செய்திகள் & நிகழ்வுகள் (News & Events)", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = UnionGoldBright)
                    Text(text = "சங்கத்தின் அதிகாரப்பூர்வ அறிவிப்புகள்", fontSize = 12.sp, color = Color.White)
                }
            }
        }

        item {
            Text(text = "சமீபத்திய செய்திகள்", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        items(newsList) { news ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(color = UnionRedPrimary, shape = RoundedCornerShape(4.dp)) {
                            Text(text = news.category, fontSize = 10.sp, color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                        Text(text = news.date, fontSize = 11.sp, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = news.titleTamil, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = news.contentTamil, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

// Photo & Video Gallery Screen
@Composable
fun GalleryScreen(
    viewModel: TnpaViewModel,
    modifier: Modifier = Modifier
) {
    val isTamil by viewModel.isTamil.collectAsState()
    val userUploadedPhotos = remember { mutableStateListOf<Uri>() }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            userUploadedPhotos.add(0, uri)
        }
    }

    val defaultGalleryTitles = listOf(
        "மாநில மாநாடு திருச்சி - 2026",
        "சுவர் ஓவியக் கலை பயிற்சி - மதுரை",
        "பாதுகாப்பு உபகரணங்கள் விநியோகம் - கோவை",
        "நலவாரிய கார்டு சிறப்பு முகாம் - சென்னை",
        "ஓவியர்களின் ஓவியக் கண்காட்சி - சேலம்",
        "சங்க மாவட்ட நிர்வாகிகள் ஆலோசனை"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Surface(
            color = UnionRedDark,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isTamil) "புகைப்பட & வீடியோ கேலரி (Gallery)" else "Photo & Video Gallery",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = UnionGoldBright
                )
                Text(
                    text = if (isTamil) "சங்க நிகழ்வுகள் மற்றும் உங்கள் ஓவியப் பணிகளின் புகைப்படங்கள்" else "Union event photos & member artwork collection",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Upload New Photo to Gallery Button
        Button(
            onClick = {
                galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            colors = ButtonDefaults.buttonColors(containerColor = UnionRedPrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.AddAPhoto, contentDescription = "Add Photo")
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isTamil) "உங்கள் ஓவியம் / நிகழ்வு புகைப்படத்தைப் பதிவேற்றுக" else "Upload Your Artwork / Event Photo",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // User Uploaded Photos
            if (userUploadedPhotos.isNotEmpty()) {
                item {
                    Text(
                        text = if (isTamil) "நீங்கள் பதிவேற்றிய புகைப்படங்கள் (${userUploadedPhotos.size})" else "Your Uploaded Photos (${userUploadedPhotos.size})",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = UnionRedDark
                    )
                }

                items(userUploadedPhotos) { uri ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                                    .background(Color.Black.copy(alpha = 0.05f))
                            ) {
                                AsyncImage(
                                    model = uri,
                                    contentDescription = "Uploaded Photo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isTamil) "உறுப்பினர் பதிவேற்றிய புகைப்படம்" else "Member Uploaded Photo",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Surface(
                                    color = Color(0xFF2E7D32),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "UPLOADED ✓",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isTamil) "அதிகாரப்பூர்வ சங்க நிகழ்வுகள்" else "Official Union Archives",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            items(defaultGalleryTitles) { title ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(UnionRedPrimary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = "Gallery",
                                tint = UnionRedDark,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "TNPA² Official Event Archive • 2026",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

// Donation Portal & Contact Screen
@Composable
fun DonationContactScreen(
    viewModel: TnpaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Surface(
            color = UnionRedDark,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(imageVector = Icons.Default.VolunteerActivism, contentDescription = "Donate", tint = UnionGoldBright, modifier = Modifier.size(36.dp))
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "சங்க நிதி & ஆதரவு (Donation Portal)", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = UnionGoldBright)
                Text(text = "பாதிக்கப்பட்ட தொழிலாளர் குடும்பங்களுக்கு நிதியுதவி அளித்திடுங்கள்", fontSize = 12.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "UPI QR Code மூலம் சங்க நிதி வழங்கலாம்", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                CanvasQrCode(data = "upi://pay?pa=tnpa2union@sbi&pn=TNPA2Union", size = 120.dp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "UPI ID: tnpa2union@sbi", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UnionRedDark)
                Text(text = "வங்கி கணக்கு: 39482010921 (SBI Chennai)", fontSize = 12.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "சங்க தலைமை அலுவலகம் (HQ Contact)", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = UnionRedDark)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "📍 மாநில தலைமை அலுவலகம் அம்பலக்காரன் பட்டி, உத்தங்குடி போஸ்ட் அவுட், மேலூர் மெயின் ரோடு, மதுரை - 625107.", fontSize = 13.sp, lineHeight = 18.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "📞 தொடர்புக்கு / தொலைபேசி: 7010131915", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UnionRedDark)
                Text(text = "☎️ இலவச உதவி எண் (Toll-Free): 1800-425-7010", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(text = "✉️ support@tnpa2.org", fontSize = 13.sp)
            }
        }
    }
}
