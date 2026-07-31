package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.TnpaViewModel
import com.example.ui.components.UnionFlagBadge
import com.example.ui.screens.AboutUnionScreen
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.ComplaintsFaqScreen
import com.example.ui.screens.DistrictLeadersScreen
import com.example.ui.screens.DonationContactScreen
import com.example.ui.screens.GalleryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.HtmlWebViewScreen
import com.example.ui.screens.IdCardVerificationScreen
import com.example.ui.screens.JobsTrainingScreen
import com.example.ui.screens.LaborLawsWelfareScreen
import com.example.ui.screens.MembershipRegistrationScreen
import com.example.ui.screens.NewsEventsScreen
import com.example.ui.screens.StateExecutivesScreen
import com.example.ui.screens.SubscriptionReceiptsScreen
import com.example.ui.screens.TollFreeAiHotlineScreen
import com.example.ui.screens.XavierBabuAiScreen
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.ReceiptLong
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.UnionGoldAccent
import com.example.ui.theme.UnionGoldBright
import com.example.ui.theme.UnionRedDark
import com.example.ui.theme.UnionRedPrimary
import kotlinx.coroutines.launch

import com.example.ui.screens.MemberPortalScreen
import androidx.compose.material.icons.filled.AccountBox

class MainActivity : ComponentActivity() {

    private val viewModel: TnpaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkMode by viewModel.isDarkMode.collectAsState()

            MyApplicationTheme(darkTheme = isDarkMode) {
                MainAppScaffold(viewModel = viewModel)
            }
        }
    }
}

enum class Screen(val titleTamil: String, val titleEnglish: String, val icon: ImageVector) {
    HOME("முகப்பு", "Home", Icons.Default.Home),
    MEMBER_PORTAL("உறுப்பினர் போர்ட்டல்", "Member Portal", Icons.Default.AccountBox),
    HTML_PORTAL("HTML போர்ட்டல் (index.html)", "HTML Web Portal", Icons.Default.Language),
    TOLL_FREE_AI("இலவச AI உதவி எண் (1800)", "AI Toll-Free Hotline", Icons.Default.PhoneInTalk),
    XAVIER_AI("சேவியர் பாபு AI", "Xavier Babu AI", Icons.Default.AutoAwesome),
    REGISTER("உறுப்பினர் பதிவு", "Join Union", Icons.Default.PersonAdd),
    RECEIPTS("சந்தா & ரசீதுகள்", "Receipts & Fees", Icons.Default.ReceiptLong),
    STATE_EXECUTIVES("மாநில நிர்வாகிகள்", "State Leadership", Icons.Default.Groups),
    ID_VERIFY("ID சரிபார்ப்பு", "ID Verification", Icons.Default.Badge),
    DISTRICTS("மாவட்ட நிர்வாகிகள்", "District Leaders", Icons.Default.LocationOn),
    WELFARE("நலத்திட்டம் & சட்டங்கள்", "Welfare & Laws", Icons.Default.Shield),
    JOBS("வேலை & பயிற்சி", "Jobs & Skills", Icons.Default.School),
    COMPLAINTS("புகார் & FAQ", "Complaints & FAQ", Icons.Default.ReportProblem),
    ADMIN("நிர்வாகி மையம்", "Admin Panel", Icons.Default.AdminPanelSettings),
    ABOUT("சங்க வரலாறு", "About Union", Icons.Default.Info),
    DONATION("சங்க நிதி & தொடர்பு", "Donations", Icons.Default.VolunteerActivism)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScaffold(viewModel: TnpaViewModel) {
    val isTamil by viewModel.isTamil.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val navItems = listOf(
        Screen.HOME,
        Screen.MEMBER_PORTAL,
        Screen.HTML_PORTAL,
        Screen.TOLL_FREE_AI,
        Screen.XAVIER_AI,
        Screen.REGISTER,
        Screen.RECEIPTS,
        Screen.STATE_EXECUTIVES,
        Screen.ID_VERIFY,
        Screen.DISTRICTS,
        Screen.WELFARE,
        Screen.JOBS,
        Screen.COMPLAINTS,
        Screen.ADMIN,
        Screen.ABOUT,
        Screen.DONATION
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.width(300.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(UnionRedDark)
                        .padding(20.dp)
                ) {
                    UnionFlagBadge(width = 54.dp, height = 36.dp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "TNPA² - தமிழ்நாடு",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = UnionGoldBright
                    )
                    Text(
                        text = "பெயிண்டர்கள் மற்றும் ஓவியர்கள் முன்னேற்ற சங்கம்",
                        fontSize = 11.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 8.dp)
                ) {
                    navItems.forEach { screen ->
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = if (isTamil) screen.titleTamil else screen.titleEnglish,
                                    fontSize = 14.sp,
                                    fontWeight = if (currentScreen == screen) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.name,
                                    tint = if (currentScreen == screen) UnionRedPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            selected = currentScreen == screen,
                            onClick = {
                                currentScreen = screen
                                coroutineScope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = UnionRedPrimary.copy(alpha = 0.12f),
                                selectedTextColor = UnionRedPrimary
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "TNPA²",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = UnionGoldBright
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isTamil) currentScreen.titleTamil else currentScreen.titleEnglish,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        // Toll-Free AI Hotline Action Button
                        IconButton(onClick = { currentScreen = Screen.TOLL_FREE_AI }) {
                            Icon(
                                imageVector = Icons.Default.PhoneInTalk,
                                contentDescription = "Toll-Free AI",
                                tint = UnionGoldBright
                            )
                        }

                        // Language Switcher Toggle
                        Surface(
                            onClick = { viewModel.toggleLanguage() },
                            shape = RoundedCornerShape(16.dp),
                            color = UnionGoldBright,
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = "Language",
                                    tint = Color.Black,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isTamil) "தமிழ்" else "ENG",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }

                        // Dark Mode Switcher
                        IconButton(onClick = { viewModel.toggleDarkMode() }) {
                            Icon(
                                imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Theme",
                                tint = UnionGoldBright
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = UnionRedDark)
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    val bottomItems = listOf(
                        Screen.HOME,
                        Screen.MEMBER_PORTAL,
                        Screen.RECEIPTS,
                        Screen.REGISTER,
                        Screen.XAVIER_AI
                    )
                    bottomItems.forEach { screen ->
                        NavigationBarItem(
                            selected = currentScreen == screen,
                            onClick = { currentScreen = screen },
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.name
                                )
                            },
                            label = {
                                Text(
                                    text = if (isTamil) screen.titleTamil else screen.titleEnglish,
                                    fontSize = 10.sp,
                                    fontWeight = if (currentScreen == screen) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = UnionRedDark,
                                selectedTextColor = UnionRedDark,
                                indicatorColor = UnionGoldBright.copy(alpha = 0.3f)
                            )
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentScreen) {
                    Screen.HOME -> HomeScreen(
                        viewModel = viewModel,
                        onNavigateToRegister = { currentScreen = Screen.REGISTER },
                        onNavigateToAi = { currentScreen = Screen.XAVIER_AI },
                        onNavigateToIdVerification = { currentScreen = Screen.ID_VERIFY },
                        onNavigateToDistrictLeaders = { currentScreen = Screen.DISTRICTS },
                        onNavigateToWelfare = { currentScreen = Screen.WELFARE },
                        onNavigateToComplaints = { currentScreen = Screen.COMPLAINTS },
                        onNavigateToJobs = { currentScreen = Screen.JOBS },
                        onNavigateToEvents = { currentScreen = Screen.ABOUT }
                    )
                    Screen.MEMBER_PORTAL -> MemberPortalScreen(
                        viewModel = viewModel,
                        onNavigateToRegister = { currentScreen = Screen.REGISTER }
                    )
                    Screen.HTML_PORTAL -> HtmlWebViewScreen(viewModel = viewModel)
                    Screen.TOLL_FREE_AI -> TollFreeAiHotlineScreen(viewModel = viewModel)
                    Screen.XAVIER_AI -> XavierBabuAiScreen(viewModel = viewModel)
                    Screen.REGISTER -> MembershipRegistrationScreen(
                        viewModel = viewModel,
                        onNavigateToIdVerification = { currentScreen = Screen.ID_VERIFY }
                    )
                    Screen.RECEIPTS -> SubscriptionReceiptsScreen(viewModel = viewModel)
                    Screen.STATE_EXECUTIVES -> StateExecutivesScreen(viewModel = viewModel)
                    Screen.ID_VERIFY -> IdCardVerificationScreen(viewModel = viewModel)
                    Screen.DISTRICTS -> DistrictLeadersScreen(viewModel = viewModel)
                    Screen.WELFARE -> LaborLawsWelfareScreen(viewModel = viewModel)
                    Screen.JOBS -> JobsTrainingScreen(viewModel = viewModel)
                    Screen.COMPLAINTS -> ComplaintsFaqScreen(viewModel = viewModel)
                    Screen.ADMIN -> AdminPanelScreen(viewModel = viewModel)
                    Screen.ABOUT -> AboutUnionScreen(viewModel = viewModel)
                    Screen.DONATION -> DonationContactScreen(viewModel = viewModel)
                }
            }
        }
    }
}
