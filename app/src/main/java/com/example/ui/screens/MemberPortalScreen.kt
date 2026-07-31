package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.MemberEntity
import com.example.data.local.WelfareClaimEntity
import com.example.ui.TnpaViewModel
import com.example.ui.components.UnionFlagBadge
import com.example.ui.components.UnionLogoBadge
import com.example.ui.theme.UnionGoldAccent
import com.example.ui.theme.UnionGoldBright
import com.example.ui.theme.UnionRedDark
import com.example.ui.theme.UnionRedPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberPortalScreen(
    viewModel: TnpaViewModel,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isTamil by viewModel.isTamil.collectAsState()
    val loggedInMember by viewModel.loggedInMember.collectAsState()
    val loginError by viewModel.loginError.collectAsState()
    val welfareClaims by viewModel.memberWelfareClaims.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var showNewClaimDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        if (loggedInMember == null) {
            // Member Login Screen
            MemberLoginForm(
                viewModel = viewModel,
                loginError = loginError,
                isTamil = isTamil,
                onNavigateToRegister = onNavigateToRegister
            )
        } else {
            // Member Portal Dashboard
            val member = loggedInMember!!
            Column(modifier = Modifier.fillMaxSize()) {
                // Member Header Card
                MemberPortalHeader(
                    member = member,
                    isTamil = isTamil,
                    onLogout = { viewModel.logoutMember() }
                )

                // Navigation Tabs
                SecondaryTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = UnionRedDark,
                    indicator = {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(selectedTab),
                            color = UnionRedDark
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                text = if (isTamil) "டிஜிட்டல் ID கார்டு" else "Digital ID Card",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Badge,
                                contentDescription = "Digital ID",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )

                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                text = if (isTamil) "விண்ணப்ப நிலை" else "App Status",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Timeline,
                                contentDescription = "Status",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )

                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = {
                            Text(
                                text = if (isTamil) "நலத்திட்ட நிலை" else "Welfare Status",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Welfare",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )
                }

                // Tab Contents
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    when (selectedTab) {
                        0 -> DigitalIdTabContent(member = member, isTamil = isTamil)
                        1 -> ApplicationStatusTabContent(member = member, isTamil = isTamil)
                        2 -> WelfareStatusTabContent(
                            claims = welfareClaims,
                            isTamil = isTamil,
                            onOpenNewClaim = { showNewClaimDialog = true }
                        )
                    }
                }
            }
        }

        // Apply New Welfare Claim Dialog
        if (showNewClaimDialog) {
            NewWelfareClaimDialog(
                isTamil = isTamil,
                onDismiss = { showNewClaimDialog = false },
                onSubmit = { title, cat, amount, remarks ->
                    viewModel.submitMemberWelfareClaim(title, cat, amount, remarks)
                    showNewClaimDialog = false
                }
            )
        }
    }
}

@Composable
private fun MemberLoginForm(
    viewModel: TnpaViewModel,
    loginError: String?,
    isTamil: Boolean,
    onNavigateToRegister: () -> Unit
) {
    var memberIdOrPhone by remember { mutableStateOf("") }
    var aadhaarOrOtp by remember { mutableStateOf("") }
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            // Header Badge
            Card(
                colors = CardDefaults.cardColors(containerColor = UnionRedDark),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(20.dp))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    UnionLogoBadge(size = 72.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isTamil) "உறுப்பினர் பாதுகாப்பான உள்நுழைவு" else "Member Secure Portal Login",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = UnionGoldBright,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = if (isTamil) "TNPA² தமிழ்நாடு பெயிண்டர்கள் & ஓவியர்கள் முன்னேற்ற சங்கம்" else "TNPA² Painter & Artist Welfare Association",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Login Box
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Lock",
                            tint = UnionRedPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isTamil) "உறுப்பினர் கணக்கில் நுழையவும்" else "Login to Member Account",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Member ID / Phone Input
                    OutlinedTextField(
                        value = memberIdOrPhone,
                        onValueChange = { memberIdOrPhone = it },
                        label = { Text(if (isTamil) "உறுப்பினர் எண் அல்லது மொபைல் எண்" else "Member ID or Mobile Number") },
                        placeholder = { Text("e.g. TNPA-2026-8942 / 9876543210") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = UnionRedPrimary) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("member_id_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = UnionRedPrimary,
                            focusedLabelColor = UnionRedPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Aadhaar Last 4 / Security Code Input
                    OutlinedTextField(
                        value = aadhaarOrOtp,
                        onValueChange = { aadhaarOrOtp = it },
                        label = { Text(if (isTamil) "ஆதார் கடைசி 4 இலக்கம் / கடவுச்சொல்" else "Aadhaar Last 4 Digits / Password") },
                        placeholder = { Text("e.g. 5821 அல்லது 1234") },
                        leadingIcon = { Icon(Icons.Default.Fingerprint, contentDescription = null, tint = UnionRedPrimary) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("aadhaar_pass_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = UnionRedPrimary,
                            focusedLabelColor = UnionRedPrimary
                        )
                    )

                    if (!loginError.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = loginError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Main Login Button
                    Button(
                        onClick = {
                            viewModel.loginMember(memberIdOrPhone, aadhaarOrOtp)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = UnionRedDark),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("member_login_button")
                    ) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = UnionGoldBright)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isTamil) "பாதுகாப்பான உள்நுழைவு (Login)" else "Secure Member Login",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Quick Demo Login Button
                    OutlinedButton(
                        onClick = {
                            viewModel.loginAsDemoMember("TNPA-2026-8942")
                            Toast.makeText(context, if (isTamil) "சோதனை உறுப்பினராக உள்நுழைந்துள்ளீர்கள்" else "Logged in as Demo Member", Toast.LENGTH_SHORT).show()
                        },
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, UnionRedPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("demo_login_button")
                    ) {
                        Icon(Icons.Default.AccountBox, contentDescription = null, tint = UnionRedPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isTamil) "⚡ சோதனை கணக்கில் நுழைய (Demo Login)" else "⚡ Quick Demo Member Login",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = UnionRedPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Register Link Card
            Surface(
                onClick = onNavigateToRegister,
                color = UnionGoldBright.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, UnionGoldAccent),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isTamil) "இன்னும் உறுப்பினராகவில்லையா? " else "Not a registered member yet? ",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isTamil) "இங்கே புதிய பதிவு செய்க" else "Register Here",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = UnionRedDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Encryption footer notice
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.Security, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isTamil) "TNPA² மாநில தலைமை பாதுகாப்பு நெறிமுறையால் பாதுகாக்கப்பட்டது" else "Protected by TNPA² State Security Protocol",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
private fun MemberPortalHeader(
    member: MemberEntity,
    isTamil: Boolean,
    onLogout: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = UnionRedDark),
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Profile Avatar
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(UnionGoldBright)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = member.name.take(1).uppercase(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = UnionRedDark
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = member.name,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = if (member.approvalStatus == "Approved") Color(0xFF2E7D32) else Color(0xFFE65100),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (member.approvalStatus == "Approved") "APPROVED" else "PENDING",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "ID: ${member.memberId} • ${member.trade}",
                        fontSize = 11.sp,
                        color = UnionGoldBright
                    )
                    Text(
                        text = "மாவட்டம்: ${member.district}",
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            IconButton(
                onClick = onLogout,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f))
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Logout",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun DigitalIdTabContent(
    member: MemberEntity,
    isTamil: Boolean
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = if (isTamil) "அதிகாரப்பூர்வ டிஜிட்டல் அடையாள அட்டை" else "Official Digital Member ID Card",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Digital ID Card Container
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(2.5.dp, UnionRedDark),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(10.dp, RoundedCornerShape(16.dp))
            ) {
                Column {
                    // Card Gradient Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(UnionRedDark, UnionRedPrimary)
                                )
                            )
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                UnionFlagBadge(width = 40.dp, height = 28.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "TNPA² - தமிழ்நாடு",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Black,
                                        color = UnionGoldBright
                                    )
                                    Text(
                                        text = "பெயிண்டர்கள் & ஓவியர்கள் முன்னேற்ற சங்கம்",
                                        fontSize = 8.5.sp,
                                        color = Color.White
                                    )
                                }
                            }
                            Surface(
                                color = UnionGoldBright,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "STATE REGISTRY",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    // Card Main Details
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Member Photo / Avatar Box
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(UnionRedDark.copy(alpha = 0.1f))
                                    .border(2.dp, UnionRedPrimary, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = UnionRedDark,
                                    modifier = Modifier.size(54.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = member.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "த/பெ: ${member.fatherName}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Surface(
                                    color = UnionRedDark,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = member.trade,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = UnionGoldBright,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Divider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Key Value Pairs Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("உறுப்பினர் எண் (Member ID)", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(member.memberId, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UnionRedDark)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy",
                                        tint = UnionRedPrimary,
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clickable {
                                                clipboardManager.setText(AnnotatedString(member.memberId))
                                                Toast
                                                    .makeText(context, if (isTamil) "உறுப்பினர் எண் நகலெடுக்கப்பட்டது" else "Member ID copied", Toast.LENGTH_SHORT)
                                                    .show()
                                            }
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("மாவட்டம் (District)", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                                Text(member.district, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("மொபைல் எண் (Phone)", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                                Text(member.phone, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("செல்லுபடியாகும் காலம் (Valid Thru)", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                                Text(member.validThru, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // QR Code Section Card
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.QrCode2,
                                        contentDescription = "QR",
                                        tint = UnionRedDark,
                                        modifier = Modifier.size(42.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "அங்கீகரிக்கப்பட்ட டிஜிட்டல் QR",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Verified Union Hash: TNPA-STATE-SEAL",
                                            fontSize = 9.sp,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }
                                }

                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        Toast.makeText(context, if (isTamil) "டிஜிட்டல் ID கார்டு பதிவிறக்கம் தொடங்கப்பட்டது" else "Downloading ID Card PDF...", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UnionRedDark),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isTamil) "பதிவிறக்கம்" else "Download Card", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        Toast.makeText(context, if (isTamil) "ID கார்டு தகவல்கள் பகிரத் தயார்" else "Sharing ID Card info...", Toast.LENGTH_SHORT).show()
                    },
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, UnionRedPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = UnionRedPrimary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isTamil) "பகிர்க (Share)" else "Share Card", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UnionRedPrimary)
                }
            }
        }
    }
}

@Composable
private fun ApplicationStatusTabContent(
    member: MemberEntity,
    isTamil: Boolean
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = if (isTamil) "உறுப்பினர் சேர்க்கை விண்ணப்ப கண்காணிப்பு" else "Membership Application Tracking",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Main Status Banner Card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (member.approvalStatus == "Approved") Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (member.approvalStatus == "Approved") Icons.Default.CheckCircle else Icons.Default.Timeline,
                        contentDescription = null,
                        tint = if (member.approvalStatus == "Approved") Color(0xFF2E7D32) else Color(0xFFE65100),
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (member.approvalStatus == "Approved") "உறுப்பினர் சேர்க்கை அங்கீகரிக்கப்பட்டது!" else "விண்ணப்பம் பரிசீலனையில் உள்ளது",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (member.approvalStatus == "Approved") Color(0xFF1B5E20) else Color(0xFFE65100)
                        )
                        Text(
                            text = "பதிவு எண்: ${member.memberId} • சேர்ந்த நாள்: ${member.joinDate}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = if (isTamil) "செயல்முறை நிலைகள் (Verification Steps)" else "Verification Timeline Steps",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Step 1
            TimelineStepCard(
                stepNo = "1",
                title = if (isTamil) "விண்ணப்பம் சமர்ப்பிக்கப்பட்டது" else "Application Submitted",
                desc = if (isTamil) "உங்கள் விவரங்கள் மற்றும் ஆதார் சரிபார்ப்பு வெற்றிகரமாக முடிந்தது." else "Details & Aadhaar verification submitted successfully.",
                isCompleted = true,
                isCurrent = false
            )

            // Step 2
            TimelineStepCard(
                stepNo = "2",
                title = if (isTamil) "மாவட்ட சங்க தலைவர் சரிபார்ப்பு" else "District Leader Review",
                desc = if (isTamil) "${member.district} மாவட்ட சங்க செயலாளரால் விவரங்கள் சரிபார்க்கப்பட்டன." else "Verified by District Secretary in ${member.district}.",
                isCompleted = true,
                isCurrent = false
            )

            // Step 3
            TimelineStepCard(
                stepNo = "3",
                title = if (isTamil) "மாநில தலைமை அங்கீகாரம்" else "State Headquarters Approval",
                desc = if (isTamil) "மாநில பொதுச்செயலாளர் சேவியர் பாபு ஒப்புதலுடன் மாநில பதிவேட்டில் சேர்க்கப்பட்டது." else "Approved by State General Secretary Xavier Babu into Union Registry.",
                isCompleted = member.approvalStatus == "Approved",
                isCurrent = member.approvalStatus != "Approved"
            )

            // Step 4
            TimelineStepCard(
                stepNo = "4",
                title = if (isTamil) "டிஜிட்டல் ID கார்டு வழங்கப்பட்டது" else "Digital ID Card Issued",
                desc = if (isTamil) "டிஜிட்டல் ID கார்டு தயாராக உள்ளது. செயலியில் உடனடியாக பதிவிறக்கம் செய்து கொள்ளலாம்." else "Digital ID ready for download inside the portal.",
                isCompleted = member.approvalStatus == "Approved",
                isCurrent = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            // District Office Contact Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (isTamil) "உங்கள் மாவட்ட சங்க அலுவலகத் தொடர்பு:" else "Your District Union Office Contact:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = UnionRedDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "TNPA² ${member.district} மாவட்ட அலுவலகம்",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "உதவி எண்: 98400 11223 / 94430 55667",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun TimelineStepCard(
    stepNo: String,
    title: String,
    desc: String,
    isCompleted: Boolean,
    isCurrent: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isCompleted -> Color(0xFF2E7D32)
                        isCurrent -> UnionGoldBright
                        else -> MaterialTheme.colorScheme.outlineVariant
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            } else {
                Text(
                    text = stepNo,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrent) Color.Black else Color.Gray,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isCompleted || isCurrent) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
            )
            Text(
                text = desc,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun WelfareStatusTabContent(
    claims: List<WelfareClaimEntity>,
    isTamil: Boolean,
    onOpenNewClaim: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isTamil) "நலத்திட்ட உதவி விண்ணப்பங்கள்" else "Welfare Scheme Submissions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Button(
                    onClick = onOpenNewClaim,
                    colors = ButtonDefaults.buttonColors(containerColor = UnionRedDark),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("apply_welfare_button")
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isTamil) "புதிய விண்ணப்பம்" else "New Claim", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (claims.isEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = UnionRedPrimary, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isTamil) "நலத்திட்ட விண்ணப்பங்கள் எதுவுமில்லை" else "No welfare claims submitted yet",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isTamil) "தொழிலாளர் நலவாரியம் அல்லது சங்க நிதி உதவி பெற மேலே உள்ள பொத்தானைக் கிளிக் செய்யவும்." else "Click 'New Claim' button above to request welfare aid.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        items(claims) { claim ->
            WelfareClaimCard(claim = claim, isTamil = isTamil)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun WelfareClaimCard(claim: WelfareClaimEntity, isTamil: Boolean) {
    val statusColor = when (claim.status) {
        "Approved" -> Color(0xFF2E7D32)
        "Disbursed" -> UnionRedDark
        "Under Review" -> Color(0xFFE65100)
        else -> Color.Gray
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = UnionRedDark.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = claim.claimNo,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = UnionRedDark,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Surface(
                    color = statusColor,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = when (claim.status) {
                            "Approved" -> "APPROVED (ஒப்புதல் பெறப்பட்டது)"
                            "Disbursed" -> "DISBURSED (தொகை வழங்கப்பட்டது)"
                            "Under Review" -> "UNDER REVIEW (பரிசீலனையில்)"
                            else -> claim.status
                        },
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = claim.schemeTitleTamil,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "தேவைப்படும் தொகை: ${claim.claimedAmount}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = UnionRedPrimary
                )
                Text(
                    text = "தேதி: ${claim.submissionDate}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            if (claim.remarks.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "அதிகாரப்பூர்வ தகவல்: ${claim.remarks}",
                        fontSize = 10.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun NewWelfareClaimDialog(
    isTamil: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, String) -> Unit
) {
    var schemeTitle by remember { mutableStateOf("தொழிலாளர் நலவாரிய கல்வி உதவித்தொகை") }
    var category by remember { mutableStateOf("Board Benefit") }
    var amount by remember { mutableStateOf("15000") }
    var remarks by remember { mutableStateOf("") }

    val schemeOptions = listOf(
        "தொழிலாளர் நலவாரிய கல்வி உதவித்தொகை" to "Board Benefit",
        "சங்க விபத்து & மருத்துவ நிவாரண நிதி" to "Union Insurance",
        "மகள் / மகன் திருமண உதவித்தொகை" to "Marriage Aid",
        "இயற்கை மரண நிதியுதவி & ஈமச்சடங்கு நிதி" to "Medical Relief"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isTamil) "புதிய நலத்திட்ட உதவி பெற விண்ணப்பிக்க" else "Submit New Welfare Claim",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = UnionRedDark
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (isTamil) "நலத்திட்ட வகையைத் தேர்ந்தெடுக்கவும்:" else "Select Welfare Scheme:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))

                schemeOptions.forEach { (title, cat) ->
                    Surface(
                        onClick = {
                            schemeTitle = title
                            category = cat
                        },
                        color = if (schemeTitle == title) UnionRedDark.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        border = if (schemeTitle == title) androidx.compose.foundation.BorderStroke(1.5.dp, UnionRedPrimary) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                    ) {
                        Text(
                            text = title,
                            fontSize = 11.5.sp,
                            fontWeight = if (schemeTitle == title) FontWeight.Bold else FontWeight.Normal,
                            color = if (schemeTitle == title) UnionRedDark else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text(if (isTamil) "தேவைப்படும் தொகை (₹)" else "Claim Amount (₹)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = remarks,
                    onValueChange = { remarks = it },
                    label = { Text(if (isTamil) "கூடுதல் விபரம் / சான்றிதழ் விவரம்" else "Additional Details") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSubmit(schemeTitle, category, "₹$amount", remarks)
                },
                colors = ButtonDefaults.buttonColors(containerColor = UnionRedDark)
            ) {
                Text(if (isTamil) "சமர்ப்பி (Submit)" else "Submit", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isTamil) "ரத்து" else "Cancel")
            }
        }
    )
}
