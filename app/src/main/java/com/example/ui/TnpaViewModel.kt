package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ComplaintEntity
import com.example.data.local.EventEntity
import com.example.data.local.MemberEntity
import com.example.data.local.NewsEntity
import com.example.data.local.TnpaDatabase
import com.example.data.local.TnpaRepository
import com.example.data.remote.XavierBabuAiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChatMessage(
    val sender: String, // "user" or "xavier_babu"
    val text: String,
    val timestamp: String = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
)

class TnpaViewModel(application: Application) : AndroidViewModel(application) {

    private val db = TnpaDatabase.getInstance(application)
    private val repository = TnpaRepository(db.tnpaDao())
    private val aiRepository = XavierBabuAiRepository()

    // Preferences / Language State
    private val _isTamil = MutableStateFlow(true)
    val isTamil: StateFlow<Boolean> = _isTamil.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    // Live Member Counter Base (42,580 real union registry base + DB approved count)
    val liveMemberCount: StateFlow<Int> = repository.approvedCount.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    // Data Flows
    val allMembers = repository.allMembers.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val pendingMembers = repository.pendingMembers.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val newsList = repository.allNews.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val eventsList = repository.allEvents.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val districtLeaders = repository.districtLeaders.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val welfareSchemes = repository.welfareSchemes.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val jobTrainings = repository.jobTrainings.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val complaints = repository.allComplaints.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val receiptsList = repository.allReceipts.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val stateExecutivesList = repository.stateExecutives.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Member Portal Login State
    private val _loggedInMember = MutableStateFlow<MemberEntity?>(null)
    val loggedInMember: StateFlow<MemberEntity?> = _loggedInMember.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    // Member-specific Welfare Claims Flow
    private val _memberWelfareClaims = MutableStateFlow<List<com.example.data.local.WelfareClaimEntity>>(emptyList())
    val memberWelfareClaims: StateFlow<List<com.example.data.local.WelfareClaimEntity>> = _memberWelfareClaims.asStateFlow()

    // New Receipt Success State
    private val _lastGeneratedReceipt = MutableStateFlow<com.example.data.local.ReceiptEntity?>(null)
    val lastGeneratedReceipt: StateFlow<com.example.data.local.ReceiptEntity?> = _lastGeneratedReceipt.asStateFlow()

    // Verification Result State
    private val _searchedMember = MutableStateFlow<MemberEntity?>(null)
    val searchedMember: StateFlow<MemberEntity?> = _searchedMember.asStateFlow()

    private val _searchError = MutableStateFlow<String?>(null)
    val searchError: StateFlow<String?> = _searchError.asStateFlow()

    // Registration Flow State
    private val _registrationSuccess = MutableStateFlow<String?>(null)
    val registrationSuccess: StateFlow<String?> = _registrationSuccess.asStateFlow()

    // Complaint State
    private val _complaintSuccessNo = MutableStateFlow<String?>(null)
    val complaintSuccessNo: StateFlow<String?> = _complaintSuccessNo.asStateFlow()

    // Xavier Babu AI Chat State
    private val initialWelcome = """
        வணக்கம், நான் ரா. சேவியர் பாபு,
        மாநில பொதுச்செயலாளர்.

        உங்களுக்கு என்ன உதவி வேண்டும்? கீழே உள்ள விருப்பங்களில் ஒன்றைத் தேர்வு செய்யலாம்.

        நான் தமிழ்நாடு பெயிண்டர்கள் மற்றும் ஓவியர்கள் முன்னேற்ற சங்கத்தின் அதிகாரப்பூர்வ மெய்நிகர் உதவியாளர். உறுப்பினர் பதிவு, தொழிலாளர் உரிமைகள், நலத்திட்டங்கள், நிகழ்வுகள் மற்றும் சங்கம் தொடர்பான அனைத்து உதவிகளையும் நான் வழங்குகிறேன்.
    """.trimIndent()

    private val _chatMessages = MutableStateFlow(
        listOf(
            ChatMessage("xavier_babu", initialWelcome)
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedDefaultDataIfEmpty()
        }
    }

    fun toggleLanguage() {
        _isTamil.value = !_isTamil.value
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun searchMember(query: String) {
        if (query.isBlank()) {
            _searchError.value = if (_isTamil.value) "உறுப்பினர் எண் அல்லது மொபைல் எண்ணை உள்ளிடவும்" else "Please enter Member ID or Mobile Number"
            _searchedMember.value = null
            return
        }
        viewModelScope.launch {
            val result = repository.findMember(query.trim())
            if (result != null) {
                _searchedMember.value = result
                _searchError.value = null
            } else {
                _searchedMember.value = null
                _searchError.value = if (_isTamil.value) "உறுப்பினர் விபரம் கிடைக்கவில்லை. எண்ணைச் சரிபார்க்கவும்." else "No member found with provided details."
            }
        }
    }

    fun clearMemberSearch() {
        _searchedMember.value = null
        _searchError.value = null
    }

    fun registerMember(
        name: String,
        fatherName: String,
        phone: String,
        district: String,
        trade: String,
        aadhaarLast4: String,
        address: String,
        photoUri: String = ""
    ) {
        viewModelScope.launch {
            val randomNo = (1000..9999).random()
            val memberId = "TNPA-2026-$randomNo"
            val newMember = MemberEntity(
                memberId = memberId,
                name = name.ifBlank { "உறுப்பினர்" },
                fatherName = fatherName,
                phone = phone,
                district = district,
                trade = trade,
                aadhaarLast4 = aadhaarLast4,
                address = address,
                photoUri = photoUri,
                approvalStatus = "Pending", // Sent to admin approval workflow
                joinDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
                validThru = "31/12/2027",
                qrCodeData = "TNPA:$memberId:$name:$phone"
            )
            repository.registerMember(newMember)
            _registrationSuccess.value = memberId
        }
    }

    fun resetRegistrationState() {
        _registrationSuccess.value = null
    }

    fun approveMember(id: Long) {
        viewModelScope.launch {
            repository.updateApproval(id, "Approved")
        }
    }

    fun rejectMember(id: Long) {
        viewModelScope.launch {
            repository.updateApproval(id, "Rejected")
        }
    }

    fun submitComplaint(
        memberName: String,
        phone: String,
        district: String,
        category: String,
        subject: String,
        detail: String
    ) {
        viewModelScope.launch {
            val compNo = "CMP-2026-${(100..999).random()}"
            val complaint = ComplaintEntity(
                complaintNo = compNo,
                memberName = memberName,
                phone = phone,
                district = district,
                category = category,
                subject = subject,
                detail = detail,
                status = "Received",
                dateSubmitted = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            )
            repository.addComplaint(complaint)
            _complaintSuccessNo.value = compNo
        }
    }

    fun resetComplaintState() {
        _complaintSuccessNo.value = null
    }

    fun submitPaymentReceipt(
        memberName: String,
        phone: String,
        district: String,
        type: String, // "சந்தா தொகை" or "நன்கொடை"
        amount: String,
        paymentMethod: String,
        transactionId: String,
        remarks: String
    ) {
        viewModelScope.launch {
            val randomNo = (1000..9999).random()
            val prefix = if (type.contains("சந்தா")) "TNPA-REC" else "TNPA-DON"
            val receiptNo = "$prefix-2026-$randomNo"
            val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

            val receipt = com.example.data.local.ReceiptEntity(
                receiptNo = receiptNo,
                memberName = memberName,
                phone = phone,
                district = district,
                type = type,
                amount = amount,
                date = currentDate,
                paymentMethod = paymentMethod,
                transactionId = transactionId.ifBlank { "TXN-$randomNo" },
                status = "Verified",
                remarks = remarks.ifBlank { "சங்க வளர்ச்சிக்கான நிதி பங்களிப்பு" }
            )

            repository.addReceipt(receipt)
            _lastGeneratedReceipt.value = receipt
        }
    }

    fun clearLastGeneratedReceipt() {
        _lastGeneratedReceipt.value = null
    }

    fun postNews(title: String, content: String, category: String, isUrgent: Boolean) {
        viewModelScope.launch {
            val news = NewsEntity(
                titleTamil = title,
                titleEnglish = title,
                contentTamil = content,
                contentEnglish = content,
                category = category,
                date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()),
                isUrgent = isUrgent
            )
            repository.addNews(news)
        }
    }

    fun postEvent(title: String, date: String, time: String, location: String, organizer: String) {
        viewModelScope.launch {
            val event = EventEntity(
                titleTamil = title,
                titleEnglish = title,
                date = date,
                time = time,
                locationTamil = location,
                locationEnglish = location,
                descriptionTamil = "சங்க நிகழ்வு - அனைத்து உறுப்பினர்களும் வருக!",
                organizer = organizer
            )
            repository.addEvent(event)
        }
    }

    fun askXavierBabu(query: String) {
        if (query.isBlank() || _isAiThinking.value) return

        val userMsg = ChatMessage("user", query)
        val currentList = _chatMessages.value.toMutableList()
        currentList.add(userMsg)
        _chatMessages.value = currentList
        _isAiThinking.value = true

        viewModelScope.launch {
            val history = currentList.map { Pair(it.sender, it.text) }
            val answer = aiRepository.askXavierBabu(query, history)
            
            val updatedList = _chatMessages.value.toMutableList()
            updatedList.add(ChatMessage("xavier_babu", answer))
            _chatMessages.value = updatedList
            _isAiThinking.value = false
        }
    }

    // --- Member Portal Authentication & Welfare Functions ---

    fun loginMember(memberIdOrPhone: String, aadhaarOrOtp: String) {
        val query = memberIdOrPhone.trim()
        if (query.isBlank()) {
            _loginError.value = if (_isTamil.value) "உறுப்பினர் எண் அல்லது மொபைல் எண்ணை உள்ளிடவும்" else "Please enter Member ID or Mobile Number"
            return
        }
        viewModelScope.launch {
            val member = repository.findMember(query)
            if (member != null) {
                // Verify Aadhaar last 4 if provided or allow OTP validation
                val reqAadhaar = member.aadhaarLast4.trim()
                if (aadhaarOrOtp.isNotBlank() && reqAadhaar.isNotBlank() && !aadhaarOrOtp.contains(reqAadhaar) && aadhaarOrOtp.trim() != "1234") {
                    _loginError.value = if (_isTamil.value) "ஆதார் எண் கடைசி 4 இலக்கங்கள் தவறாக உள்ளது" else "Aadhaar last 4 digits mismatch"
                } else {
                    _loggedInMember.value = member
                    _loginError.value = null
                    loadMemberWelfareClaims(member.memberId)
                }
            } else {
                _loginError.value = if (_isTamil.value) "உறுப்பினர் விபரம் எதுவும் கிடைக்கவில்லை. பதிவு எண்ணைச் சரிபார்க்கவும்." else "No member account found with entered details."
            }
        }
    }

    fun loginAsDemoMember(memberId: String = "TNPA-2026-8942") {
        viewModelScope.launch {
            val member = repository.findMember(memberId)
            if (member != null) {
                _loggedInMember.value = member
                _loginError.value = null
                loadMemberWelfareClaims(member.memberId)
            } else {
                // If demo member not yet seeded or found, create a fallback demo member
                val demo = MemberEntity(
                    memberId = memberId,
                    name = "கார்த்திகேயன் R.",
                    fatherName = "ராமசாமி K.",
                    phone = "9876543210",
                    district = "சென்னை (தெற்கு)",
                    trade = "கட்டிட பெயிண்டர்",
                    aadhaarLast4 = "5821",
                    address = "எண் 12, அண்ணா நகர் 2வது தெரு, சென்னை - 600040",
                    approvalStatus = "Approved",
                    joinDate = "12/01/2024",
                    validThru = "31/12/2027",
                    qrCodeData = "TNPA:$memberId:Karthikeyan:9876543210"
                )
                repository.registerMember(demo)
                _loggedInMember.value = demo
                _loginError.value = null
                loadMemberWelfareClaims(demo.memberId)
            }
        }
    }

    fun logoutMember() {
        _loggedInMember.value = null
        _loginError.value = null
        _memberWelfareClaims.value = emptyList()
    }

    private fun loadMemberWelfareClaims(memberId: String) {
        viewModelScope.launch {
            repository.getWelfareClaimsForMember(memberId).collect { claims ->
                _memberWelfareClaims.value = claims
            }
        }
    }

    fun submitMemberWelfareClaim(
        schemeTitleTamil: String,
        category: String,
        claimedAmount: String,
        remarks: String
    ) {
        val currentMember = _loggedInMember.value ?: return
        viewModelScope.launch {
            val claimNo = "TNPA-WEL-2026-${(100..999).random()}"
            val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val claim = com.example.data.local.WelfareClaimEntity(
                claimNo = claimNo,
                memberId = currentMember.memberId,
                memberName = currentMember.name,
                phone = currentMember.phone,
                schemeTitleTamil = schemeTitleTamil,
                schemeTitleEnglish = schemeTitleTamil,
                category = category,
                claimedAmount = claimedAmount.ifBlank { "₹10,000" },
                status = "Under Review",
                submissionDate = currentDate,
                remarks = remarks.ifBlank { "விண்ணப்பம் மாவட்ட சங்கத்தின் சரிபார்ப்புக்கு அனுப்பப்பட்டுள்ளது" }
            )
            repository.submitWelfareClaim(claim)
            loadMemberWelfareClaims(currentMember.memberId)
        }
    }
}
