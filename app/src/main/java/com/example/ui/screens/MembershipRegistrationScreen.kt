package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.TnpaViewModel
import com.example.ui.theme.UnionGoldAccent
import com.example.ui.theme.UnionGoldBright
import com.example.ui.theme.UnionRedDark
import com.example.ui.theme.UnionRedPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipRegistrationScreen(
    viewModel: TnpaViewModel,
    onNavigateToIdVerification: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isTamil by viewModel.isTamil.collectAsState()
    val regSuccessId by viewModel.registrationSuccess.collectAsState()

    var name by remember { mutableStateOf("") }
    var fatherName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var aadhaarLast4 by remember { mutableStateOf("") }
    var selectedTrade by remember { mutableStateOf("கட்டிட பெயிண்டர்") }
    var selectedDistrict by remember { mutableStateOf("சென்னை") }
    var selectedPhotoUri by remember { mutableStateOf("") }

    // Photo Picker Launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedPhotoUri = uri.toString()
        }
    }

    var showOtpDialog by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("") }
    var isPhoneVerified by remember { mutableStateOf(false) }

    val tradeList = listOf(
        "கட்டிட பெயிண்டர் (Building Painter)",
        "சுவர் ஓவியர் (Mural Artist)",
        "டிஜிட்டல் ஆர்ட்டிஸ்ட் (Digital Artist)",
        "சைன்போர்டு ஓவியர் (Signboard Artist)",
        "ஸ்ப்ரே பெயிண்டர் (Spray Painter)",
        "மரச்சாமான்கள் வார்னிஷர் (Wood Polish)"
    )

    val districtList = listOf(
        "அரியலூர்", "செங்கல்பட்டு", "சென்னை", "கோயம்புத்தூர்", "கடலூர்",
        "தர்மபுரி", "திண்டுக்கல்", "ஈரோடு", "கள்ளக்குறிச்சி", "காஞ்சிபுரம்",
        "கன்னியாகுமரி", "கரூர்", "கிருஷ்ணகிரி", "மதுரை", "மயிலாடுதுறை",
        "நாகப்பட்டினம்", "நாமக்கல்", "நீலகிரி", "பெரம்பலூர்", "புதுக்கோட்டை",
        "ராமநாதபுரம்", "ராணிப்பேட்டை", "சேலம்", "சிவகங்கை", "தென்காசி",
        "தஞ்சாவூர்", "தேனி", "தூத்துக்குடி", "திருச்சிராப்பள்ளி", "திருநெல்வேலி",
        "திருப்பத்தூர்", "திருப்பூர்", "திருவள்ளூர்", "திருவண்ணாமலை", "திருவாரூர்",
        "வேலூர்", "விழுப்புரம்", "விருதுநகர்"
    )

    var tradeExpanded by remember { mutableStateOf(false) }
    var districtExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
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
                    text = if (isTamil) "உறுப்பினர் சேர்க்கை பதிவு" else "Membership Registration",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = UnionGoldBright
                )
                Text(
                    text = if (isTamil) "TNPA² சங்கத்தில் இணைந்து உங்கள் தொழிலாளர் உரிமைகளைப் பாதுகாத்திடுங்கள்!" else "Join TNPA² Union and protect your labor welfare rights!",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Success Dialog / Card if registered
        regSuccessId?.let { memberId ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF2E7D32)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Success",
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isTamil) "பதிவு வெற்றிகரமாக சமர்ப்பிக்கப்பட்டது!" else "Registration Submitted Successfully!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${if (isTamil) "உங்கள் விண்ணப்ப எண்" else "Application ID"}: $memberId",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = UnionRedDark
                    )
                    Text(
                        text = if (isTamil) "மாவட்ட நிர்வாகி ஒப்புதல் அளித்தவுடன் டிஜிட்டல் ID கார்டு தயாராகும்." else "Digital ID card will be ready upon district leader approval.",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            viewModel.resetRegistrationState()
                            onNavigateToIdVerification()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = UnionRedPrimary)
                    ) {
                        Text(text = if (isTamil) "அடையாள அட்டை பக்கத்திற்குச் செல்" else "Go to ID Card Page")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Photo Upload Box
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            if (selectedPhotoUri.isNotBlank()) Color.Transparent else UnionRedPrimary.copy(alpha = 0.12f)
                        )
                        .border(
                            1.5.dp,
                            if (selectedPhotoUri.isNotBlank()) Color(0xFF2E7D32) else UnionRedPrimary,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedPhotoUri.isNotBlank()) {
                        AsyncImage(
                            model = selectedPhotoUri,
                            contentDescription = "Member Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = "Photo",
                            tint = UnionRedPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isTamil) "உறுப்பினர் புகைப்படம்" else "Member Passport Photo",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (selectedPhotoUri.isNotBlank()) (if (isTamil) "புகைப்படம் இணைக்கப்பட்டது ✓" else "Photo Uploaded ✓")
                        else (if (isTamil) "போனிலிருந்து புகைப்படத்தை தேர்வு செய்யவும்" else "Choose photo from gallery"),
                        fontSize = 11.sp,
                        color = if (selectedPhotoUri.isNotBlank()) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                OutlinedButton(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (selectedPhotoUri.isNotBlank()) (if (isTamil) "மாற்று" else "Change") else (if (isTamil) "பதிவேற்று" else "Upload"),
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Form Fields
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = if (isTamil) "பெயர் (Full Name) *" else "Full Name *") },
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = fatherName,
            onValueChange = { fatherName = it },
            label = { Text(text = if (isTamil) "தந்தை / கணவர் பெயர் *" else "Father / Spouse Name *") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text(text = if (isTamil) "தொலைபேசி எண் (Mobile) *" else "Mobile Number *") },
                leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = "Phone") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { showOtpDialog = true },
                enabled = phone.length >= 10 && !isPhoneVerified,
                colors = ButtonDefaults.buttonColors(containerColor = if (isPhoneVerified) Color(0xFF2E7D32) else UnionRedPrimary)
            ) {
                Text(
                    text = if (isPhoneVerified) (if (isTamil) "சரிபார்க்கப்பட்டது ✓" else "Verified ✓") else (if (isTamil) "OTP அனுப்பு" else "Get OTP"),
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Trade Dropdown
        ExposedDropdownMenuBox(
            expanded = tradeExpanded,
            onExpandedChange = { tradeExpanded = !tradeExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedTrade,
                onValueChange = {},
                readOnly = true,
                label = { Text(text = if (isTamil) "தொழில் பிரிவு (Trade Category) *" else "Trade Category *") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tradeExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = tradeExpanded,
                onDismissRequest = { tradeExpanded = false }
            ) {
                tradeList.forEach { trade ->
                    DropdownMenuItem(
                        text = { Text(text = trade) },
                        onClick = {
                            selectedTrade = trade.split(" (").first()
                            tradeExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // District Dropdown
        ExposedDropdownMenuBox(
            expanded = districtExpanded,
            onExpandedChange = { districtExpanded = !districtExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedDistrict,
                onValueChange = {},
                readOnly = true,
                label = { Text(text = if (isTamil) "மாவட்டம் (District) *" else "District *") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = districtExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = districtExpanded,
                onDismissRequest = { districtExpanded = false }
            ) {
                districtList.forEach { dist ->
                    DropdownMenuItem(
                        text = { Text(text = dist) },
                        onClick = {
                            selectedDistrict = dist
                            districtExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = aadhaarLast4,
            onValueChange = { if (it.length <= 4) aadhaarLast4 = it },
            label = { Text(text = if (isTamil) "ஆதார் எண்ணின் கடைசி 4 எண்கள் (Optional)" else "Aadhaar Last 4 Digits (Optional)") },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Aadhaar") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text(text = if (isTamil) "வீட்டு முகவரி (Address) *" else "Home Address *") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Submit Button
        Button(
            onClick = {
                viewModel.registerMember(
                    name = name,
                    fatherName = fatherName,
                    phone = phone,
                    district = selectedDistrict,
                    trade = selectedTrade,
                    aadhaarLast4 = aadhaarLast4,
                    address = address,
                    photoUri = selectedPhotoUri
                )
            },
            enabled = name.isNotBlank() && phone.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = UnionRedPrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = if (isTamil) "உறுப்பினராக விண்ணப்பிக்கவும் (Submit Registration)" else "Submit Registration",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    // OTP Verification Simulation Dialog
    if (showOtpDialog) {
        AlertDialog(
            onDismissRequest = { showOtpDialog = false },
            title = {
                Text(
                    text = if (isTamil) "மொபைல் எண் சரிபார்ப்பு (OTP Verification)" else "Mobile OTP Verification",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "${if (isTamil) "மொபைல் எண்" else "Mobile Number"}: $phone\n${if (isTamil) "அனுப்பப்பட்ட OTP எண்ணை உள்ளிடவும் (மாதிரி: 1234)" else "Enter sent OTP (Sample: 1234)"}",
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = otpCode,
                        onValueChange = { otpCode = it },
                        label = { Text("Enter OTP (1234)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isPhoneVerified = true
                        showOtpDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UnionRedPrimary)
                ) {
                    Text(text = if (isTamil) "சரிபார்க்கவும்" else "Verify OTP")
                }
            },
            dismissButton = {
                TextButton(onClick = { showOtpDialog = false }) {
                    Text(text = if (isTamil) "ரத்து" else "Cancel")
                }
            }
        )
    }
}

