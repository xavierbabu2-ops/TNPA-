package com.example.ui.screens

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.ReceiptEntity
import com.example.ui.TnpaViewModel
import com.example.ui.components.CanvasQrCode
import com.example.ui.components.UnionFlagBadge
import com.example.ui.theme.UnionGoldAccent
import com.example.ui.theme.UnionGoldBright
import com.example.ui.theme.UnionRedDark
import com.example.ui.theme.UnionRedPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionReceiptsScreen(
    viewModel: TnpaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isTamil by viewModel.isTamil.collectAsState()
    val receiptsList by viewModel.receiptsList.collectAsState()
    val lastGeneratedReceipt by viewModel.lastGeneratedReceipt.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") } // ALL, SUBSCRIPTION, DONATION
    var selectedReceiptForModal by remember { mutableStateOf<ReceiptEntity?>(null) }
    var showNewPaymentDialog by remember { mutableStateOf(false) }

    // If a new receipt was generated, open the modal for it
    if (lastGeneratedReceipt != null && selectedReceiptForModal == null) {
        selectedReceiptForModal = lastGeneratedReceipt
    }

    // Calculations
    val totalSubscriptions = receiptsList.filter { it.type.contains("சந்தா") }
        .sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
    val totalDonations = receiptsList.filter { it.type.contains("நன்கொடை") }
        .sumOf { it.amount.toDoubleOrNull() ?: 0.0 }

    val filteredList = receiptsList.filter { receipt ->
        val matchesQuery = receipt.memberName.contains(searchQuery, ignoreCase = true) ||
                receipt.phone.contains(searchQuery, ignoreCase = true) ||
                receipt.receiptNo.contains(searchQuery, ignoreCase = true) ||
                receipt.district.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            "SUBSCRIPTION" -> receipt.type.contains("சந்தா")
            "DONATION" -> receipt.type.contains("நன்கொடை")
            else -> true
        }

        matchesQuery && matchesFilter
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Top Banner
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
                            text = if (isTamil) "சந்தா & நன்கொடை ரசீதுகள்" else "Subscriptions & Donation Receipts",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = UnionGoldBright
                        )
                        Text(
                            text = if (isTamil) "சங்கத்தில் செலுத்தப்பட்ட அனைத்து ரசீதுகளின் விவரங்கள்" else "Official Record of Receipts & Member Financial Contributions",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                    UnionFlagBadge(width = 48.dp, height = 32.dp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stats Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = if (isTamil) "மொத்த சந்தா தொகை" else "Total Subscriptions",
                                fontSize = 11.sp,
                                color = Color.White
                            )
                            Text(
                                text = "₹ ${totalSubscriptions.toInt()}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = UnionGoldBright
                            )
                        }
                    }

                    Surface(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = if (isTamil) "மொத்த நன்கொடை" else "Total Donations",
                                fontSize = 11.sp,
                                color = Color.White
                            )
                            Text(
                                text = "₹ ${totalDonations.toInt()}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = UnionGoldBright
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Pay New Subscription / Donation Button
        Button(
            onClick = { showNewPaymentDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = UnionRedPrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Receipt")
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isTamil) "புதிய சந்தா / நன்கொடை செலுத்துக (ரசீது பெற)" else "Pay Subscription / Donation (Get Receipt)",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(if (isTamil) "ரசீது எண், பெயர் அல்லது மொபைல் தேட..." else "Search receipt no, name, phone...") },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedFilter == "ALL",
                onClick = { selectedFilter = "ALL" },
                label = { Text(if (isTamil) "அனைத்தும் (${receiptsList.size})" else "All (${receiptsList.size})") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = UnionRedPrimary,
                    selectedLabelColor = Color.White
                )
            )

            FilterChip(
                selected = selectedFilter == "SUBSCRIPTION",
                onClick = { selectedFilter = "SUBSCRIPTION" },
                label = { Text(if (isTamil) "சந்தா தொகை" else "Subscriptions") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = UnionRedPrimary,
                    selectedLabelColor = Color.White
                )
            )

            FilterChip(
                selected = selectedFilter == "DONATION",
                onClick = { selectedFilter = "DONATION" },
                label = { Text(if (isTamil) "நன்கொடை" else "Donations") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = UnionRedPrimary,
                    selectedLabelColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Receipts List
        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isTamil) "ரசீது விவரங்கள் எதுவும் கிடைக்கவில்லை" else "No receipt records found",
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredList) { receipt ->
                    ReceiptCardItem(
                        receipt = receipt,
                        isTamil = isTamil,
                        onViewReceipt = { selectedReceiptForModal = receipt }
                    )
                }
            }
        }
    }

    // Modal Receipt Dialog
    selectedReceiptForModal?.let { receipt ->
        DigitalReceiptModalDialog(
            receipt = receipt,
            isTamil = isTamil,
            onDismiss = {
                selectedReceiptForModal = null
                viewModel.clearLastGeneratedReceipt()
            },
            onShare = {
                val shareText = """
                    TNPA² தமிழ்நாடு பெயிண்டர்கள் மற்றும் ஓவியர்கள் முன்னேற்ற சங்கம்
                    அதிகாரப்பூர்வ ரசீது / OFFICIAL RECEIPT
                    ரசீது எண்: ${receipt.receiptNo}
                    பெயர்: ${receipt.memberName}
                    வகை: ${receipt.type}
                    தொகை: ₹ ${receipt.amount}
                    தேதி: ${receipt.date}
                    செலுத்திய முறை: ${receipt.paymentMethod}
                    ஹெல்ப்லைன்: 1800-425-7010 / 7010131915
                """.trimIndent()

                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }
                context.startActivity(Intent.createChooser(sendIntent, "ரசீதை பகிர்க"))
            }
        )
    }

    // New Payment Entry Dialog
    if (showNewPaymentDialog) {
        NewPaymentEntryDialog(
            isTamil = isTamil,
            onDismiss = { showNewPaymentDialog = false },
            onSubmit = { name, phone, district, type, amount, payMethod, txnId, remarks ->
                viewModel.submitPaymentReceipt(
                    memberName = name,
                    phone = phone,
                    district = district,
                    type = type,
                    amount = amount,
                    paymentMethod = payMethod,
                    transactionId = txnId,
                    remarks = remarks
                )
                showNewPaymentDialog = false
            }
        )
    }
}

@Composable
private fun ReceiptCardItem(
    receipt: ReceiptEntity,
    isTamil: Boolean,
    onViewReceipt: () -> Unit
) {
    val isSubscription = receipt.type.contains("சந்தா")

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
                Surface(
                    color = if (isSubscription) UnionRedDark else Color(0xFF2E7D32),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = receipt.type,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Text(
                    text = receipt.receiptNo,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = receipt.memberName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "📱 ${receipt.phone} • 📍 ${receipt.district}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹ ${receipt.amount}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = UnionRedDark
                    )
                    Text(
                        text = receipt.date,
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified",
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isTamil) "உறுதி செய்யப்பட்டது (${receipt.paymentMethod})" else "Verified (${receipt.paymentMethod})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2E7D32)
                    )
                }

                Button(
                    onClick = onViewReceipt,
                    colors = ButtonDefaults.buttonColors(containerColor = UnionRedDark),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Receipt, contentDescription = "View", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isTamil) "ரசீது காண்க" else "View Receipt",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun DigitalReceiptModalDialog(
    receipt: ReceiptEntity,
    isTamil: Boolean,
    onDismiss: () -> Unit,
    onShare: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                // Close button bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isTamil) "அதிகாரப்பூர்வ டிஜிட்டல் ரசீது" else "Official Digital Receipt",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = UnionRedDark
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Official Receipt Canvas Container
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF5)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, UnionRedDark.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Union Header
                        UnionFlagBadge(width = 40.dp, height = 26.dp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "தமிழ்நாடு பெயிண்டர்கள் மற்றும் ஓவியர்கள் முன்னேற்ற சங்கம்",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = UnionRedDark,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Text(
                            text = "மாநில தலைமை அலுவலகம்: அம்பலக்காரன்பட்டி, ஒத்தக்கடை, மேலூர் மெயின் ரோடு, மதுரை - 625107.",
                            fontSize = 9.sp,
                            color = Color.DarkGray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Text(
                            text = "இலவச உதவி எண்: 1800-425-7010 | தொடர்பு: 7010131915",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = UnionRedDark
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = UnionGoldAccent, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Receipt Title & Number
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "ரசீது எண்: ${receipt.receiptNo}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "வகை: ${receipt.type}",
                                    fontSize = 11.sp,
                                    color = UnionRedDark,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "தேதி: ${receipt.date}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "முறை: ${receipt.paymentMethod}",
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Receipt Details Body Table
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = "பெயர் / Member: ${receipt.memberName}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(text = "தொலைபேசி / Phone: ${receipt.phone}", fontSize = 11.sp)
                                Text(text = "மாவட்டம் / District: ${receipt.district}", fontSize = 11.sp)
                                Text(text = "பரிவர்த்தனை ID: ${receipt.transactionId}", fontSize = 11.sp, color = Color.Gray)
                                Text(text = "குறிப்பு / Remarks: ${receipt.remarks}", fontSize = 11.sp, color = Color.DarkGray)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Amount Big Highlight Box
                        Surface(
                            color = UnionRedDark,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "செலுத்தப்பட்ட தொகை / Amount Paid:",
                                    fontSize = 11.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "₹ ${receipt.amount}.00",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = UnionGoldBright
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // QR Verification & Stamp
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CanvasQrCode(data = "TNPA:RECEIPT:${receipt.receiptNo}:${receipt.amount}:${receipt.memberName}", size = 50.dp)

                            Column(horizontalAlignment = Alignment.End) {
                                Surface(
                                    color = Color(0xFF1B5E20),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "VERIFIED & APPROVED ✓",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "சேவியர் பாபு",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = UnionRedDark
                                )
                                Text(
                                    text = "மாநில பொதுச்செயலாளர்",
                                    fontSize = 9.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Share & Download Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onShare,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = if (isTamil) "பகிர்க" else "Share", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = UnionRedDark),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.Download, contentDescription = "Done", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = if (isTamil) "முடிந்தது" else "Done", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewPaymentEntryDialog(
    isTamil: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (name: String, phone: String, district: String, type: String, amount: String, payMethod: String, txnId: String, remarks: String) -> Unit
) {
    var memberName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("மதுரை") }
    var paymentType by remember { mutableStateOf("சந்தா தொகை") } // சந்தா தொகை or நன்கொடை
    var amount by remember { mutableStateOf("500") }
    var payMethod by remember { mutableStateOf("UPI") }
    var txnId by remember { mutableStateOf("") }
    var remarks by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isTamil) "சந்தா / நன்கொடை செலுத்துக" else "Pay Subscription / Donation",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = UnionRedDark
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // UPI QR Code Section
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF5)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isTamil) "UPI QR Code மூலம் செலுத்தி ரசீது பெறலாம்" else "Pay via UPI QR Code",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        CanvasQrCode(data = "upi://pay?pa=tnpa2union@sbi&pn=TNPA2Union&am=$amount", size = 90.dp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "UPI ID: tnpa2union@sbi", fontSize = 10.sp, color = UnionRedDark, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Type selector
                Row(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        RadioButton(
                            selected = paymentType == "சந்தா தொகை",
                            onClick = {
                                paymentType = "சந்தா தொகை"
                                if (amount == "1000") amount = "500"
                            }
                        )
                        Text(text = if (isTamil) "சந்தா தொகை" else "Subscription", fontSize = 12.sp)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        RadioButton(
                            selected = paymentType == "நன்கொடை",
                            onClick = { paymentType = "நன்கொடை" }
                        )
                        Text(text = if (isTamil) "நன்கொடை" else "Donation", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = memberName,
                    onValueChange = { memberName = it },
                    label = { Text(if (isTamil) "பெயர்" else "Name") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text(if (isTamil) "தொலைபேசி" else "Phone") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text(if (isTamil) "தொகை (₹)" else "Amount (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = district,
                        onValueChange = { district = it },
                        label = { Text(if (isTamil) "மாவட்டம்" else "District") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = txnId,
                        onValueChange = { txnId = it },
                        label = { Text(if (isTamil) "UPI / Ref No" else "UPI / Ref No") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    )
                }

                errorMsg?.let { msg ->
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = msg, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (memberName.isBlank() || phone.isBlank() || amount.isBlank()) {
                            errorMsg = if (isTamil) "தயவுசெய்து அனைத்து விவரங்களையும் நிரப்பவும்" else "Please fill required fields"
                            return@Button
                        }
                        onSubmit(memberName, phone, district, paymentType, amount, payMethod, txnId, remarks)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UnionRedDark),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.ReceiptLong, contentDescription = "Submit")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isTamil) "ரசீது உருவாக்கு (Generate Receipt)" else "Generate Official Receipt",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
