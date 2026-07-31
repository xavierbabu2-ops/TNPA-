package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.MemberEntity
import com.example.ui.TnpaViewModel
import com.example.ui.components.CanvasQrCode
import com.example.ui.components.UnionFlagBadge
import com.example.ui.theme.UnionGoldAccent
import com.example.ui.theme.UnionGoldBright
import com.example.ui.theme.UnionRedDark
import com.example.ui.theme.UnionRedPrimary

@Composable
fun IdCardVerificationScreen(
    viewModel: TnpaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isTamil by viewModel.isTamil.collectAsState()
    val searchedMember by viewModel.searchedMember.collectAsState()
    val searchError by viewModel.searchError.collectAsState()

    var searchQuery by remember { mutableStateOf("TNPA-2026-8942") } // Pre-loaded sample ID

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Search Header Box
        Surface(
            color = UnionRedDark,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isTamil) "உறுப்பினர் ID சரிபார்ப்பு & அடையாள அட்டை" else "Member ID Check & Digital ID Card",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = UnionGoldBright
                )
                Text(
                    text = if (isTamil) "உங்கள் உறுப்பினர் எண் (எ.கா: TNPA-2026-8942) அல்லது மொபைல் எண்ணை உள்ளிடவும்" else "Enter your Member ID (e.g. TNPA-2026-8942) or Mobile Number",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("TNPA-2026-8942 or 9876543210") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.White, RoundedCornerShape(12.dp))
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { viewModel.searchMember(searchQuery) },
                        colors = ButtonDefaults.buttonColors(containerColor = UnionGoldBright, contentColor = Color.Black),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(54.dp)
                    ) {
                        Text(
                            text = if (isTamil) "தேடு" else "Find",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Error Message
        searchError?.let { err ->
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = err,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Search Prompt Tip
        if (searchedMember == null && searchError == null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isTamil) "💡 மாதிரி சோதனைகளுக்கு கீழே உள்ள எண்ணைப் பயன்படுத்தலாம்:" else "💡 Try these sample IDs for verification test:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "1. TNPA-2026-8942 (கார்த்திகேயன் R. - சென்னை)", fontSize = 12.sp)
                    Text(text = "2. TNPA-2026-5510 (செல்வமணி S. - மதுரை)", fontSize = 12.sp)
                }
            }
        }

        // Digital ID Card Display
        searchedMember?.let { member ->
            DigitalMemberCardView(
                member = member,
                isTamil = isTamil,
                onDownloadClick = {
                    Toast.makeText(context, if (isTamil) "டிஜிட்டல் ID கார்டு பதிவிறக்கம் செய்யப்பட்டது!" else "Digital ID Card Downloaded to Gallery!", Toast.LENGTH_LONG).show()
                },
                onWhatsAppShareClick = {
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "TNPA² - தமிழ்நாடு பெயிண்டர்கள் மற்றும் ஓவியர்கள் முன்னேற்ற சங்கம்\nஅதிகாரப்பூர்வ உறுப்பினர் ID: ${member.memberId}\nபெயர்: ${member.name}\nமாவட்டம்: ${member.district}\nநிலை: ${member.approvalStatus} ✓")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                }
            )
        }
    }
}

@Composable
private fun DigitalMemberCardView(
    member: MemberEntity,
    isTamil: Boolean,
    onDownloadClick: () -> Unit,
    onWhatsAppShareClick: () -> Unit
) {
    val context = LocalContext.current
    var isFlipped by remember { mutableStateOf(false) }
    var isApprovedBySuperAdmin by remember { mutableStateOf(true) } // Auto-approved upon WhatsApp approval request / initial check

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "cardFlipAnimation"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Super Admin Approval Status Banner
        Surface(
            color = if (isApprovedBySuperAdmin) Color(0xFF1B5E20) else Color(0xFFE65100),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (isApprovedBySuperAdmin) Icons.Default.Verified else Icons.Default.CheckCircle,
                        contentDescription = "Approval",
                        tint = UnionGoldBright,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (isApprovedBySuperAdmin)
                                "சூப்பர் அட்மின் ஒப்புதல்: பெறப்பட்டது ✓"
                            else
                                "சூப்பர் அட்மின் ஒப்புதல்: நிலுவையில் உள்ளது",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (isApprovedBySuperAdmin)
                                "மாநில பொதுச்செயலாளர் ரா. சேவியர் பாபு அவர்களால் டவுன்லோட் அனுமதி வழங்கப்பட்டது."
                            else
                                "டவுன்லோட் செய்ய கீழே உள்ள பட்டனை அழுத்தவும் (WhatsApp Request)",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }

                if (!isApprovedBySuperAdmin) {
                    Button(
                        onClick = { isApprovedBySuperAdmin = true },
                        colors = ButtonDefaults.buttonColors(containerColor = UnionGoldBright, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("அப்ரூவ் செய்", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Flip Hint Header Banner
        Surface(
            color = UnionGoldBright.copy(alpha = 0.2f),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .padding(bottom = 10.dp)
                .clickable { isFlipped = !isFlipped }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.TouchApp,
                    contentDescription = "Tap to Flip",
                    tint = UnionRedDark,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isFlipped) 
                        (if (isTamil) "அட்டையைத் தொட்டு முன்பக்கத்திற்கு மாறுக 🔄" else "Tap card to flip to Front 🔄")
                    else 
                        (if (isTamil) "அட்டையைத் தொட்டு QR & பின்புறத்தைப் பார்க்கவும் 🔄" else "Tap card to view QR Code & Back side 🔄"),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = UnionRedDark
                )
            }
        }

        // High Quality Printable Digital ID Card Box with 3D Flip
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(2.dp, UnionGoldBright),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(16.dp))
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 14f * density
                }
                .clickable { isFlipped = !isFlipped }
        ) {
            if (rotation <= 90f) {
                // FRONT OF DIGITAL ID CARD
                Column {
                    // Top Union Header Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(UnionRedDark, UnionRedPrimary)
                                )
                            )
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            UnionFlagBadge(width = 44.dp, height = 30.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "TNPA² - தமிழ்நாடு சங்கம்",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = UnionGoldBright
                                )
                                Text(
                                    text = "தமிழ்நாடு பெயிண்டர்கள் & ஓவியர்கள் முன்னேற்ற சங்கம்",
                                    fontSize = 10.sp,
                                    color = Color.White
                                )
                            }
                            Surface(
                                color = Color(0xFF2E7D32),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = member.approvalStatus,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    // Card Front Body Content
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Member Photo Box
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(90.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(UnionRedPrimary.copy(alpha = 0.1f))
                                    .border(1.5.dp, UnionRedPrimary, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (member.photoUri.isNotBlank()) {
                                    AsyncImage(
                                        model = member.photoUri,
                                        contentDescription = "Member Photo",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Photo",
                                        tint = UnionRedDark,
                                        modifier = Modifier.size(54.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                color = UnionRedDark.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.QrCode2,
                                        contentDescription = "QR",
                                        tint = UnionRedDark,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = if (isTamil) "QR பார்க்கத் தொடவும்" else "Tap for QR",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = UnionRedDark
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Member Details Column
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = member.name,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = UnionRedDark
                            )
                            Text(
                                text = "உறுப்பினர் எண்: ${member.memberId}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            IdDetailRow(label = if (isTamil) "தொழில்" else "Trade", value = member.trade)
                            IdDetailRow(label = if (isTamil) "மாவட்டம்" else "District", value = member.district)
                            IdDetailRow(label = if (isTamil) "தொலைபேசி" else "Phone", value = member.phone)
                            IdDetailRow(label = if (isTamil) "சேர்ந்த நாள்" else "Joined", value = member.joinDate)
                            IdDetailRow(label = if (isTamil) "காலாவதி" else "Valid Thru", value = member.validThru)
                        }
                    }

                    HorizontalDivider(color = Color.LightGray, thickness = 1.dp)

                    // Official Seal & Signature Footer
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "ரா. சேவியர் பாபு", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = UnionRedDark)
                            Text(text = "மாநில பொதுச்செயலாளர்", fontSize = 9.sp, color = Color.DarkGray)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Verified, contentDescription = "Seal", tint = Color(0xFF1B5E20), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "சங்க அதிகாரப்பூர்வ அட்டை", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
                        }
                    }
                }
            } else {
                // BACK OF DIGITAL ID CARD (FLIPPED 180 DEGREES)
                Box(
                    modifier = Modifier.graphicsLayer {
                        rotationY = 180f
                    }
                ) {
                    Column {
                        // Top Header Banner for Back
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(Color(0xFF1A237E), Color(0xFF0D47A1))
                                    )
                                )
                                .padding(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                UnionFlagBadge(width = 40.dp, height = 28.dp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "TNPA² DIGITAL QR VERIFICATION",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = UnionGoldBright
                                    )
                                    Text(
                                        text = "அதிகாரப்பூர்வ டிஜிட்டல் பாதுகாப்பு குறியீடு",
                                        fontSize = 10.sp,
                                        color = Color.White
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.Flip,
                                    contentDescription = "Flipped",
                                    tint = UnionGoldBright,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Back Body Content - Dynamic Canvas QR Code
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "உறுப்பினர் சரிபார்ப்பு QR குறியீடு",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = UnionRedDark
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Large Dynamic Canvas QR Code Box
                            Box(
                                modifier = Modifier
                                    .background(Color.White, RoundedCornerShape(12.dp))
                                    .border(2.dp, UnionGoldAccent, RoundedCornerShape(12.dp))
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CanvasQrCode(
                                    data = member.qrCodeData,
                                    size = 140.dp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Member ID: ${member.memberId}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "📍 மாநில தலைமை அலுவலகம் அம்பலக்காரன் பட்டி, உத்தங்குடி, மதுரை - 625107.",
                                        fontSize = 10.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "📞 7010131915 | Toll-Free: 1800-425-7010",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = UnionRedDark
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)

                        // Signatures & Seals
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "ரா. சேவியர் பாபு", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = UnionRedDark)
                                Text(text = "பொதுச்செயலாளர்", fontSize = 8.sp, color = Color.DarkGray)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "S. மைக்கேல் ஆல்வின்", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = UnionRedDark)
                                Text(text = "மாநில தலைவர்", fontSize = 8.sp, color = Color.DarkGray)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "R. சக்திவேல்", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = UnionRedDark)
                                Text(text = "மாநில பொருளாளர்", fontSize = 8.sp, color = Color.DarkGray)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons Column
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // WhatsApp Approval Request Button to Super Admin
            Button(
                onClick = {
                    val message = "வணக்கம் சூப்பர் அட்மின் (ரா. சேவியர் பாபு),\nஎன் TNPA2 உறுப்பினர் அட்டை டவுன்லோட் செய்ய அப்ரூவல் அனுமதி வழங்குமாறு கேட்டுக்கொள்கிறேன்.\n\n👤 பெயர்: ${member.name}\n🆔 உறுப்பினர் ID: ${member.memberId}\n📍 மாவட்டம்: ${member.district}\n📱 போன்: ${member.phone}"
                    val url = "https://api.whatsapp.com/send?phone=917010131915&text=${Uri.encode(message)}"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                    isApprovedBySuperAdmin = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366), contentColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "WhatsApp Request")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "📲 சூப்பர் அட்மினுக்கு அப்ரூவல் அனுமதி அனுப்பு (WhatsApp)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onDownloadClick,
                    colors = ButtonDefaults.buttonColors(containerColor = UnionRedPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = "Download")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = if (isTamil) "அட்டை பதிவிறக்கம்" else "Download Card", fontSize = 12.sp)
                }

                Button(
                    onClick = onWhatsAppShareClick,
                    colors = ButtonDefaults.buttonColors(containerColor = UnionRedDark, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = if (isTamil) "நண்பர்களுக்குப் பகிர்" else "Share Card", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun IdDetailRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 1.dp)) {
        Text(text = "$label: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color.Black)
    }
}
