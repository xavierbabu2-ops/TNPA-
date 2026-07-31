package com.example.data.local

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TnpaRepository(private val dao: TnpaDao) {

    val allMembers: Flow<List<MemberEntity>> = dao.getAllMembers()
    val pendingMembers: Flow<List<MemberEntity>> = dao.getPendingMembers()
    val approvedCount: Flow<Int> = dao.getApprovedMemberCount()

    val allNews: Flow<List<NewsEntity>> = dao.getAllNews()
    val allEvents: Flow<List<EventEntity>> = dao.getAllEvents()
    val districtLeaders: Flow<List<DistrictLeaderEntity>> = dao.getAllDistrictLeaders()
    val allComplaints: Flow<List<ComplaintEntity>> = dao.getAllComplaints()
    val welfareSchemes: Flow<List<WelfareSchemeEntity>> = dao.getAllWelfareSchemes()
    val jobTrainings: Flow<List<JobTrainingEntity>> = dao.getAllJobTrainings()
    val allReceipts: Flow<List<ReceiptEntity>> = dao.getAllReceipts()
    val stateExecutives: Flow<List<StateExecutiveEntity>> = dao.getAllStateExecutives()
    val allWelfareClaims: Flow<List<WelfareClaimEntity>> = dao.getAllWelfareClaims()

    fun getWelfareClaimsForMember(memberId: String): Flow<List<WelfareClaimEntity>> {
        return dao.getWelfareClaimsForMember(memberId)
    }

    suspend fun submitWelfareClaim(claim: WelfareClaimEntity): Long = withContext(Dispatchers.IO) {
        dao.insertWelfareClaim(claim)
    }

    suspend fun findMember(query: String): MemberEntity? = withContext(Dispatchers.IO) {
        dao.findMember(query, query)
    }

    suspend fun addReceipt(receipt: ReceiptEntity): Long = withContext(Dispatchers.IO) {
        dao.insertReceipt(receipt)
    }

    suspend fun addStateExecutive(executive: StateExecutiveEntity) = withContext(Dispatchers.IO) {
        dao.insertStateExecutive(executive)
    }

    suspend fun registerMember(member: MemberEntity): Long = withContext(Dispatchers.IO) {
        dao.insertMember(member)
    }

    suspend fun updateApproval(memberId: Long, status: String) = withContext(Dispatchers.IO) {
        dao.updateMemberApprovalStatus(memberId, status)
    }

    suspend fun addNews(news: NewsEntity) = withContext(Dispatchers.IO) {
        dao.insertNews(news)
    }

    suspend fun addEvent(event: EventEntity) = withContext(Dispatchers.IO) {
        dao.insertEvent(event)
    }

    suspend fun addComplaint(complaint: ComplaintEntity) = withContext(Dispatchers.IO) {
        dao.insertComplaint(complaint)
    }

    suspend fun findComplaint(no: String): ComplaintEntity? = withContext(Dispatchers.IO) {
        dao.findComplaint(no)
    }

    suspend fun seedDefaultDataIfEmpty() = withContext(Dispatchers.IO) {
        // Pre-populate data if needed
        // Sample members
        dao.insertMember(
            MemberEntity(
                memberId = "TNPA-2026-8942",
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
                qrCodeData = "TNPA:TNPA-2026-8942:Karthikeyan:9876543210"
            )
        )

        dao.insertMember(
            MemberEntity(
                memberId = "TNPA-2026-5510",
                name = "செல்வமணி S.",
                fatherName = "சுப்பிரமணி M.",
                phone = "9443322110",
                district = "மதுரை",
                trade = "சுவர் ஓவியர் & டிஜிட்டல் ஆர்ட்டிஸ்ட்",
                aadhaarLast4 = "9012",
                address = "எண் 45, மேல மாசி வீதி, மதுரை - 625001",
                approvalStatus = "Approved",
                joinDate = "05/03/2023",
                validThru = "31/12/2027",
                qrCodeData = "TNPA:TNPA-2026-5510:Selvamani:9443322110"
            )
        )

        // Seed Sample Welfare Claims
        dao.insertWelfareClaim(
            WelfareClaimEntity(
                claimNo = "TNPA-WEL-2026-101",
                memberId = "TNPA-2026-8942",
                memberName = "கார்த்திகேயன் R.",
                phone = "9876543210",
                schemeTitleTamil = "தொழிலாளர் நலவாரிய கல்வி உதவித்தொகை",
                schemeTitleEnglish = "Labor Board Education Scholarship",
                category = "Board Benefit",
                claimedAmount = "₹15,000",
                status = "Approved",
                submissionDate = "10/06/2026",
                remarks = "ஆவணங்கள் பெறப்பட்டு வங்கி கணக்கில் விரைவில் செலுத்தப்படும்"
            )
        )

        dao.insertWelfareClaim(
            WelfareClaimEntity(
                claimNo = "TNPA-WEL-2026-102",
                memberId = "TNPA-2026-8942",
                memberName = "கார்த்திகேயன் R.",
                phone = "9876543210",
                schemeTitleTamil = "சங்க மருத்துவ நிதியுதவி (Safety & Medical Relief)",
                schemeTitleEnglish = "Union Safety & Medical Relief",
                category = "Union Insurance",
                claimedAmount = "₹25,000",
                status = "Under Review",
                submissionDate = "18/07/2026",
                remarks = "சென்னை மாவட்ட சங்க தலைவர் பி. முருகன் பரிசீலனையில் உள்ளது"
            )
        )

        dao.insertWelfareClaim(
            WelfareClaimEntity(
                claimNo = "TNPA-WEL-2026-103",
                memberId = "TNPA-2026-5510",
                memberName = "செல்வமணி S.",
                phone = "9443322110",
                schemeTitleTamil = "மகள் திருமண உதவித்தொகை",
                schemeTitleEnglish = "Daughter Marriage Welfare Aid",
                category = "Marriage Aid",
                claimedAmount = "₹20,000",
                status = "Disbursed",
                submissionDate = "02/05/2026",
                remarks = "தொகை ₹20,000 நேரிடையாக வங்கி கணக்கில் செலுத்தப்பட்டது"
            )
        )

        // Seed News
        dao.insertNews(
            NewsEntity(
                titleTamil = "TNPA² மாநில மாநாடு - 2026 திருச்சியில் மிக பிரம்மாண்டமாக நடைபெறுகிறது!",
                titleEnglish = "TNPA² State Conference 2026 in Trichy",
                contentTamil = "தமிழ்நாடு பெயிண்டர்கள் மற்றும் ஓவியர்கள் முன்னேற்ற சங்கத்தின் 15வது மாநில மாநாடு வரும் ஆகஸ்ட் 25ஆம் தேதி திருச்சியில் நடைபெற உள்ளது. மாநில பொதுச்செயலாளர் சேவியர் பாபு தலைமையில் தொழிலாளர்களின் உரிமைகள் குறித்த முக்கிய தீர்மானங்கள் நிறைவேற்றப்படும்.",
                contentEnglish = "The 15th State Conference will be held in Trichy. Key decisions regarding painter rights will be passed.",
                category = "மாநில செய்தி",
                date = "30 ஜூலை 2026",
                isUrgent = true
            )
        )

        dao.insertNews(
            NewsEntity(
                titleTamil = "ஓவியர்கள் மற்றும் பெயிண்டர்களுக்கு இலவச விபத்து பாதுகாப்பு காப்பீடு திட்டம் அறிமுகம்",
                titleEnglish = "Free Accident Insurance Scheme Launched",
                contentTamil = "சங்க உறுப்பினர்களுக்கு ₹5 லட்சம் வரையிலான புதிய குழு விபத்து காப்பீடு திட்டத்தை மாநில தலைமை அமல்படுத்தியுள்ளது. அனைத்து உறுப்பினர்களும் உடனடியாக அடையாள அட்டையைப் புதுப்பிக்குமாறு கேட்டுக்கொள்ளப்படுகிறார்கள்.",
                contentEnglish = "₹5 Lakh group accident cover launched for all registered union members.",
                category = "நலத்திட்டம்",
                date = "28 ஜூலை 2026",
                isUrgent = false
            )
        )

        dao.insertNews(
            NewsEntity(
                titleTamil = "நலவாரிய புதுப்பித்தல் முகாம் அனைத்து மாவட்டங்களிலும் ஆகஸ்ட் முதல் வாரம் நடைபெறுகிறது",
                titleEnglish = "Welfare Board Renewal Camps in All Districts",
                contentTamil = "தமிழ்நாடு உடலுழைப்பு தொழிலாளர்கள் நலவாரியத்தில் விடுபட்ட பெயிண்டர்கள் மற்றும் ஓவியர்களைச் சேர்க்க சிறப்பு முகாம்கள் மாவட்ட அலுவலகங்களில் நடைபெறும்.",
                contentEnglish = "Special camps for Labor Board renewal across 38 districts.",
                category = "அறிவிப்பு",
                date = "25 ஜூலை 2026",
                isUrgent = false
            )
        )

        // Seed Events
        dao.insertEvent(
            EventEntity(
                titleTamil = "மாநில செயற்குழு மற்றும் மாவட்ட நிர்வாகிகள் கூட்டம்",
                titleEnglish = "State Executive Committee Meeting",
                date = "10 ஆகஸ்ட் 2026",
                time = "காலை 10:00 மணி",
                locationTamil = "சங்க தலைமை அலுவலகம், சென்னை",
                locationEnglish = "Union HQ, Chennai",
                descriptionTamil = "அனைத்து மாவட்டச் செயலாளர்களும், தலைவர்களும் தவறாமல் கலந்து கொள்ளுமாறு கேட்டுக்கொள்ளப்படுகிறார்கள்.",
                organizer = "சேவியர் பாபு (மாநில பொதுச்செயலாளர்)"
            )
        )

        dao.insertEvent(
            EventEntity(
                titleTamil = "நவீன ஏர்லெஸ் ஸ்ப்ரே பெயிண்டிங் மற்றும் பாதுகாப்பு உபகரணங்கள் பயிற்சி",
                titleEnglish = "Modern Airless Spray Painting & Safety Workshop",
                date = "18 ஆகஸ்ட் 2026",
                time = "காலை 09:30 மணி",
                locationTamil = "தொழிற்பயிற்சி மையம், கோயம்புத்தூர்",
                locationEnglish = "Training Center, Coimbatore",
                descriptionTamil = "உயர் மட்ட கட்டிட பெயிண்டர்களுக்கான சர்வதேச தர பாதுகாப்பு உபகரணங்கள் பயன்பாட்டு பயிற்சி.",
                organizer = "TNPA² பயிற்சி மையம்"
            )
        )

        // Seed District Leaders - Complete 38 Districts of Tamil Nadu
        val districts = listOf(
            DistrictLeaderEntity(districtTamil = "சென்னை", districtEnglish = "Chennai", secretaryNameTamil = "P. முருகன்", presidentNameTamil = "K. அன்பு", phone = "98400 11223", totalMembers = 4850, officeAddress = "எண் 10, பாரதி சாலை, சென்னை"),
            DistrictLeaderEntity(districtTamil = "செங்கல்பட்டு", districtEnglish = "Chengalpattu", secretaryNameTamil = "M. ரமேஷ்", presidentNameTamil = "S. பாஸ்கரன்", phone = "98401 22334", totalMembers = 2150, officeAddress = "ஜி.எஸ்.டி சாலை, செங்கல்பட்டு"),
            DistrictLeaderEntity(districtTamil = "திருவள்ளூர்", districtEnglish = "Tiruvallur", secretaryNameTamil = "K. வெங்கடேசன்", presidentNameTamil = "R. சம்பத்", phone = "98402 33445", totalMembers = 1980, officeAddress = "பஸ் நிலையம் எதிரில், திருவள்ளூர்"),
            DistrictLeaderEntity(districtTamil = "காஞ்சிபுரம்", districtEnglish = "Kanchipuram", secretaryNameTamil = "G. நடராஜன்", presidentNameTamil = "T. பரசுராமன்", phone = "98403 44556", totalMembers = 1760, officeAddress = "காந்தி ரோடு, காஞ்சிபுரம்"),
            DistrictLeaderEntity(districtTamil = "வேலூர்", districtEnglish = "Vellore", secretaryNameTamil = "J. சாமுவேல்", presidentNameTamil = "M. வெங்கடேசன்", phone = "98941 22334", totalMembers = 1820, officeAddress = "காட்பாடி மெயின் ரோடு, வேலூர்"),
            DistrictLeaderEntity(districtTamil = "ராணிப்பேட்டை", districtEnglish = "Ranipet", secretaryNameTamil = "A. அருள்மணி", presidentNameTamil = "P. கோவிந்தன்", phone = "98942 33445", totalMembers = 1450, officeAddress = "எம்.பி.டி ரோடு, ராணிப்பேட்டை"),
            DistrictLeaderEntity(districtTamil = "திருப்பத்தூர்", districtEnglish = "Tirupathur", secretaryNameTamil = "S. வடிவேல்", presidentNameTamil = "V. துரைசாமி", phone = "98943 44556", totalMembers = 1320, officeAddress = "ரயில்வே ஸ்டேஷன் ரோடு, திருப்பத்தூர்"),
            DistrictLeaderEntity(districtTamil = "திருவண்ணாமலை", districtEnglish = "Tiruvannamalai", secretaryNameTamil = "N. சீனிவாசன்", presidentNameTamil = "K. ராமச்சந்திரன்", phone = "98944 55667", totalMembers = 1890, officeAddress = "தேரோடும் வீதி, திருவண்ணாமலை"),
            DistrictLeaderEntity(districtTamil = "விழுப்புரம்", districtEnglish = "Viluppuram", secretaryNameTamil = "R. உலகநாதன்", presidentNameTamil = "M. சண்முகம்", phone = "98421 11223", totalMembers = 2100, officeAddress = "நேருஜி சாலை, விழுப்புரம்"),
            DistrictLeaderEntity(districtTamil = "கள்ளக்குறிச்சி", districtEnglish = "Kallakurichi", secretaryNameTamil = "T. செல்வராஜ்", presidentNameTamil = "P. காசிராமன்", phone = "98422 22334", totalMembers = 1540, officeAddress = "சேலம் மெயின் ரோடு, கள்ளக்குறிச்சி"),
            DistrictLeaderEntity(districtTamil = "கடலூர்", districtEnglish = "Cuddalore", secretaryNameTamil = "K. கலியபெருமாள்", presidentNameTamil = "S. சுப்ரமணியன்", phone = "98423 33445", totalMembers = 2210, officeAddress = "பாரதி சாலை, கடலூர்"),
            DistrictLeaderEntity(districtTamil = "தர்மபுரி", districtEnglish = "Dharmapuri", secretaryNameTamil = "M. மாதேஸ்வரன்", presidentNameTamil = "C. சின்னசாமி", phone = "98424 44556", totalMembers = 1670, officeAddress = "நேதாஜி பைபாஸ், தர்மபுரி"),
            DistrictLeaderEntity(districtTamil = "கிருஷ்ணகிரி", districtEnglish = "Krishnagiri", secretaryNameTamil = "P. முனியப்பன்", presidentNameTamil = "R. கிருஷ்ணன்", phone = "98425 55667", totalMembers = 1830, officeAddress = "ராயக்கோட்டை ரோடு, கிருஷ்ணகிரி"),
            DistrictLeaderEntity(districtTamil = "சேலம்", districtEnglish = "Salem", secretaryNameTamil = "S. பழனிசாமி", presidentNameTamil = "C. ராஜேந்திரன்", phone = "98420 77881", totalMembers = 2650, officeAddress = "அரசு மருத்துவமனை எதிர்புறம், சேலம்"),
            DistrictLeaderEntity(districtTamil = "நாமக்கல்", districtEnglish = "Namakkal", secretaryNameTamil = "K. கந்தசாமி", presidentNameTamil = "P. பெரியசாமி", phone = "98426 66778", totalMembers = 1790, officeAddress = "மோகனூர் ரோடு, நாமக்கல்"),
            DistrictLeaderEntity(districtTamil = "ஈரோடு", districtEnglish = "Erode", secretaryNameTamil = "V. சென்னியப்பன்", presidentNameTamil = "M. ஈஸ்வரன்", phone = "98427 77889", totalMembers = 2340, officeAddress = "பெருந்துறை ரோடு, ஈரோடு"),
            DistrictLeaderEntity(districtTamil = "திருப்பூர்", districtEnglish = "Tiruppur", secretaryNameTamil = "A. முத்துக்குமார்", presidentNameTamil = "S. துரைசாமி", phone = "98428 88990", totalMembers = 2950, officeAddress = "அவிநாசி ரோடு, திருப்பூர்"),
            DistrictLeaderEntity(districtTamil = "கோயம்புத்தூர்", districtEnglish = "Coimbatore", secretaryNameTamil = "R. தங்கவேல்", presidentNameTamil = "N. சிவக்குமார்", phone = "98940 33445", totalMembers = 3410, officeAddress = "100 அடி சாலை, காந்திபுரம், கோவை"),
            DistrictLeaderEntity(districtTamil = "நீலகிரி", districtEnglish = "Nilgiris", secretaryNameTamil = "J. ஜான்சன்", presidentNameTamil = "M. ராபின்சன்", phone = "98945 11223", totalMembers = 980, officeAddress = "கமர்ஷியல் ரோடு, உதகமண்டலம்"),
            DistrictLeaderEntity(districtTamil = "திண்டுக்கல்", districtEnglish = "Dindigul", secretaryNameTamil = "P. நாகராஜன்", presidentNameTamil = "K. சுப்பையா", phone = "94432 11223", totalMembers = 1940, officeAddress = "பழனி ரோடு, திண்டுக்கல்"),
            DistrictLeaderEntity(districtTamil = "கரூர்", districtEnglish = "Karur", secretaryNameTamil = "R. பாலசுப்ரமணியன்", presidentNameTamil = "S. பெரியசாமி", phone = "94433 22334", totalMembers = 1620, officeAddress = "கோவை ரோடு, கரூர்"),
            DistrictLeaderEntity(districtTamil = "திருச்சிராப்பள்ளி", districtEnglish = "Tiruchirappalli", secretaryNameTamil = "T. கணேசன்", presidentNameTamil = "A. ஆரோக்கியதாஸ்", phone = "94420 88990", totalMembers = 2980, officeAddress = "தில்லை நகர் 5வது குறுக்கு, திருச்சி"),
            DistrictLeaderEntity(districtTamil = "பெரம்பலூர்", districtEnglish = "Perambalur", secretaryNameTamil = "M. கருப்பையா", presidentNameTamil = "T. மருதமுத்து", phone = "94434 33445", totalMembers = 1120, officeAddress = "துறைமங்கலம் 3வது தெரு, பெரம்பலூர்"),
            DistrictLeaderEntity(districtTamil = "அரியலூர்", districtEnglish = "Ariyalur", secretaryNameTamil = "K. சக்கரவர்த்தி", presidentNameTamil = "S. தர்மலிங்கம்", phone = "94435 44556", totalMembers = 1250, officeAddress = "ஜெயங்கொண்டம் ரோடு, அரியலூர்"),
            DistrictLeaderEntity(districtTamil = "தஞ்சாவூர்", districtEnglish = "Thanjavur", secretaryNameTamil = "V. சுவாமிநாதன்", presidentNameTamil = "G. கலியபெருமாள்", phone = "98424 66778", totalMembers = 1950, officeAddress = "பழைய பஸ் நிலையம் அருகில், தஞ்சாவூர்"),
            DistrictLeaderEntity(districtTamil = "மயிலாடுதுறை", districtEnglish = "Mayiladuthurai", secretaryNameTamil = "S. காசிநாதன்", presidentNameTamil = "M. வைத்தியலிங்கம்", phone = "94436 55667", totalMembers = 1380, officeAddress = "கச்சேரி ரோடு, மயிலாடுதுறை"),
            DistrictLeaderEntity(districtTamil = "நாகப்பட்டினம்", districtEnglish = "Nagapattinam", secretaryNameTamil = "N. மீனாட்சிசுந்தரம்", presidentNameTamil = "P. ஆரோக்கியசாமி", phone = "94437 66778", totalMembers = 1420, officeAddress = "வெளிப்பாளையம், நாகப்பட்டினம்"),
            DistrictLeaderEntity(districtTamil = "திருவாரூர்", districtEnglish = "Tiruvarur", secretaryNameTamil = "R. தியாகராஜன்", presidentNameTamil = "K. சோமசுந்தரம்", phone = "94438 77889", totalMembers = 1510, officeAddress = "நேதாஜி ரோடு, திருவாரூர்"),
            DistrictLeaderEntity(districtTamil = "புதுக்கோட்டை", districtEnglish = "Pudukkottai", secretaryNameTamil = "A. கணபதி", presidentNameTamil = "M. அடைக்கலம்", phone = "94439 88990", totalMembers = 1730, officeAddress = "சத்தியமூர்த்தி நகர், புதுக்கோட்டை"),
            DistrictLeaderEntity(districtTamil = "மதுரை", districtEnglish = "Madurai", secretaryNameTamil = "M. அழகர்சாமி", presidentNameTamil = "S. வேல்முருகன்", phone = "94430 55667", totalMembers = 3920, officeAddress = "எண் 22, சித்திரை வீதி, மதுரை"),
            DistrictLeaderEntity(districtTamil = "தேனி", districtEnglish = "Theni", secretaryNameTamil = "K. முருகன்", presidentNameTamil = "P. பாண்டி", phone = "94430 11223", totalMembers = 1680, officeAddress = "மதுரை மெயின் ரோடு, தேனி"),
            DistrictLeaderEntity(districtTamil = "ராமநாதபுரம்", districtEnglish = "Ramanathapuram", secretaryNameTamil = "S. சேதுபதி", presidentNameTamil = "M. முனியசாண்டி", phone = "94430 22334", totalMembers = 1490, officeAddress = "அரண்மனை வாசல், ராமநாதபுரம்"),
            DistrictLeaderEntity(districtTamil = "சிவகங்கை", districtEnglish = "Sivaganga", secretaryNameTamil = "V. சுப்பையா", presidentNameTamil = "K. காளிமுத்து", phone = "94430 33445", totalMembers = 1560, officeAddress = "காரைக்குடி மெயின் ரோடு, சிவகங்கை"),
            DistrictLeaderEntity(districtTamil = "விருதுநகர்", districtEnglish = "Virudhunagar", secretaryNameTamil = "P. குருசாமி", presidentNameTamil = "M. மாரியப்பன்", phone = "94430 44556", totalMembers = 2180, officeAddress = "மதுரை ரோடு, விருதுநகர்"),
            DistrictLeaderEntity(districtTamil = "திருநெல்வேலி", districtEnglish = "Tirunelveli", secretaryNameTamil = "K. சுடலைமணி", presidentNameTamil = "P. முத்துகிருஷ்ணன்", phone = "94431 44556", totalMembers = 2240, officeAddress = "பாளை ரவுண்டானா, திருநெல்வேலி"),
            DistrictLeaderEntity(districtTamil = "தென்காசி", districtEnglish = "Tenkasi", secretaryNameTamil = "S. ஆறுமுகசாமி", presidentNameTamil = "M. சங்கரன்", phone = "94431 55667", totalMembers = 1640, officeAddress = "சுவாமி சன்னதி வீதி, தென்காசி"),
            DistrictLeaderEntity(districtTamil = "தூத்துக்குடி", districtEnglish = "Thoothukudi", secretaryNameTamil = "A. அந்தோணிசாமி", presidentNameTamil = "P. பொன்ராஜ்", phone = "94431 66778", totalMembers = 2050, officeAddress = "வி.ஓ.சி ரோடு, தூத்துக்குடி"),
            DistrictLeaderEntity(districtTamil = "கன்னியாகுமரி", districtEnglish = "Kanyakumari", secretaryNameTamil = "M. சுப்பிரமணியன்", presidentNameTamil = "T. தாமஸ்", phone = "94431 77889", totalMembers = 1870, officeAddress = "டபிள்யூ.சி.சி ரோடு, நாகர்கோவில்")
        )
        districts.forEach { dao.insertDistrictLeader(it) }

        // Seed Welfare Schemes (மாநில அரசு & மத்திய அரசு & சங்கம்)
        dao.insertWelfareScheme(
            WelfareSchemeEntity(
                titleTamil = "தமிழ்நாடு உடலுழைப்பு தொழிலாளர்கள் நலவாரிய பதிவு & விபத்து காப்பீடு",
                titleEnglish = "TN Manual Workers Welfare Board Registration & Relief",
                category = "மாநில அரசு நலவாரியம்",
                benefitsAmount = "₹ 5,00,000",
                eligibilityTamil = "18 முதல் 60 வயதுடைய பெயிண்டர் / சுவர் ஓவியர் / ஆட்டோமோட்டிவ் பெயிண்டர்.",
                requiredDocsTamil = "ஆதார் கார்டு, ஸ்மார்ட் ரேஷன் கார்டு, வங்கி கணக்கு புத்தகம், பாஸ்போர்ட் சைஸ் போட்டோ.",
                applyProcessTamil = "இந்த செயலியில் 'நலவாரிய ஆன்லைன் விண்ணப்பம்' படிவத்தைப் பூர்த்தி செய்து நேரடியாகச் சமர்ப்பிக்கலாம்."
            )
        )

        dao.insertWelfareScheme(
            WelfareSchemeEntity(
                titleTamil = "ஈ-ஷ்ரம் அட்டை மத்திய அரசு தொழிலாளர் தேசிய பதிவு (e-Shram Portal)",
                titleEnglish = "e-Shram Central Unorganised Workers Portal Registration",
                category = "மத்திய அரசு திட்டம்",
                benefitsAmount = "₹ 2,00,000 இலவச காப்பீடு",
                eligibilityTamil = "16 முதல் 59 வயதுடைய எந்தவொரு அமைப்புசாரா தொழிலாளியும் பெயிண்டரும் விண்ணப்பிக்கலாம்.",
                requiredDocsTamil = "ஆதார் எண், ஆதாரில் இணைக்கப்பட்ட மொபைல் எண், வங்கி கணக்கு எண்.",
                applyProcessTamil = "மத்திய அரசு e-Shram போர்ட்டலில் நேரடிப் பதிவு. இந்த செயலியில் 'மத்திய அரசு திட்டங்கள்' மூலம் விண்ணப்பிக்கலாம்."
            )
        )

        dao.insertWelfareScheme(
            WelfareSchemeEntity(
                titleTamil = "பிரதமர் விஷ்வகர்மா கலைஞர் திட்டம் (PM Vishwakarma Scheme for Painters/Artists)",
                titleEnglish = "PM Vishwakarma Artisan Support & Toolkit Grant",
                category = "மத்திய அரசு திட்டம்",
                benefitsAmount = "₹ 15,000 கருவித்தொகுதி + ₹ 3 இலட்சம் கடன் (5%)",
                eligibilityTamil = "பாரம்பரிய கலைஞர், சுவர் ஓவியர், பெயிண்டர் குடும்பங்கள்.",
                requiredDocsTamil = "ஆதார் அட்டை, ரேஷன் கார்டு, வங்கி பாஸ்புக், தொழில் சான்றிதழ்.",
                applyProcessTamil = "ரூ. 15,000 மதிப்பிலான நவீன பெயிண்டிங் கருவிகள் இலவச வவுச்சர் மற்றும் ரூ. 3 லட்சம் குறைந்த வட்டி கடன் பெற இச்செயலியில் விண்ணப்பிக்கவும்."
            )
        )

        dao.insertWelfareScheme(
            WelfareSchemeEntity(
                titleTamil = "பிரதம மந்திரி ஷ்ரம் யோகி மான்தன் ஓய்வூதிய திட்டம் (PM-SYM Pension)",
                titleEnglish = "PM Shram Yogi Maandhan Monthly Pension",
                category = "மத்திய அரசு திட்டம்",
                benefitsAmount = "₹ 3,000 / மாதம்",
                eligibilityTamil = "மாத வருமானம் ₹15,000-க்கு குறைவான 18 முதல் 40 வயதுடைய தொழிலாளர்கள்.",
                requiredDocsTamil = "ஆதார் அட்டை, சேமிப்பு வங்கி கணக்கு புத்தகம் (Auto-debit வசதியுடன்).",
                applyProcessTamil = "60 வயது நிறைவடைந்த பின் வாழ்நாள் முழுவதும் மாதம் ₹3,000 ஓய்வூதியம் வழங்கும் மத்திய அரசு திட்டம்."
            )
        )

        dao.insertWelfareScheme(
            WelfareSchemeEntity(
                titleTamil = "முதல்வரின் விரிவான மருத்துவக் காப்பீட்டுத் திட்டம் (TN CM Health Insurance)",
                titleEnglish = "Chief Minister's Comprehensive Health Insurance",
                category = "மாநில அரசு நலத்திட்டம்",
                benefitsAmount = "₹ 5,00,000 / குடும்பம் / ஆண்டு",
                eligibilityTamil = "ஆண்டு வருமானம் ₹1,20,000-க்கு குறைவான பெயிண்டர் குடும்பங்கள்.",
                requiredDocsTamil = "ரேஷன் கார்டு, வருமான சான்றிதழ், ஆதார் அட்டைகள்.",
                applyProcessTamil = "தமிழக அரசு அங்கீகரித்த அரசு மற்றும் தனியார் மருத்துவமனைகளில் இலவச சிகிச்சை."
            )
        )

        dao.insertWelfareScheme(
            WelfareSchemeEntity(
                titleTamil = "உறுப்பினர் திருமண நிதி & மகப்பேறு உதவித்தொகை",
                titleEnglish = "Marriage & Maternity Assistance",
                category = "மாநில அரசு நலவாரியம்",
                benefitsAmount = "₹ 20,000 - ₹ 50,000",
                eligibilityTamil = "நலவாரிய உறுப்பினரின் திருமணம் அல்லது பெண் தொழிலாளர் மகப்பேறு உதவி.",
                requiredDocsTamil = "திருமண அழைப்பிதழ் / பிறப்பு சான்றிதழ், VAO சான்றிதழ், அடையாள அட்டை.",
                applyProcessTamil = "நலவாரிய ஆன்லைன் போர்ட்டல் அல்லது சங்கத்தின் மூலமாக விண்ணப்பிக்கலாம்."
            )
        )

        dao.insertWelfareScheme(
            WelfareSchemeEntity(
                titleTamil = "குழந்தைகள் உயர்கல்வி கல்வி உதவித்தொகை",
                titleEnglish = "Children Higher Education Scholarship",
                category = "மாநில அரசு நலவாரியம்",
                benefitsAmount = "₹ 1,500 - ₹ 8,000 / ஆண்டு",
                eligibilityTamil = "10ஆம் வகுப்பு, 12ஆம் வகுப்பு தேர்ச்சி மற்றும் பட்டப்படிப்பு/டிப்ளமோ படிக்கும் தொழிலாளர்களின் பிள்ளைகள்.",
                requiredDocsTamil = "மதிப்பெண் சான்றிதழ், கல்லூரி சேர்க்கை ரசீது, வங்கி கணக்கு புத்தகம்.",
                applyProcessTamil = "கல்வியாண்டு தொடக்கத்தில் ஆன்லைன் அல்லது சங்க அலுவலகத்தில் விண்ணப்பிக்கலாம்."
            )
        )

        dao.insertWelfareScheme(
            WelfareSchemeEntity(
                titleTamil = "மூத்த தொழிலாளர்கள் ஓய்வூதிய திட்டம்",
                titleEnglish = "Senior Painter Pension Scheme",
                category = "மாநில அரசு நலவாரியம்",
                benefitsAmount = "₹ 1,200 / மாதம்",
                eligibilityTamil = "60 வயது நிறைவடைந்த குறைந்தபட்சம் 5 ஆண்டுகள் நலவாரிய உறுப்பினராக இருந்தவர்கள்.",
                requiredDocsTamil = "வயது சான்றிதழ், அடையாள அட்டை, ஆயுள் சான்றிதழ்.",
                applyProcessTamil = "ஆண்டுதோறும் ஆயுள் சான்றிதழ் சமர்ப்பித்து ஆயுட்காலம் வரை பெறலாம்."
            )
        )

        // Seed Job / Training
        dao.insertJobTraining(
            JobTrainingEntity(
                titleTamil = "திருச்சி அரியலூர் நெடுஞ்சாலை பால சுவர் ஓவிய ஒப்பந்த வேலை",
                type = "வேலைவாய்ப்பு",
                location = "திருச்சி - அரியலூர்",
                contactPerson = "S. பெரியசாமி (பொறுப்பாளர்)",
                phone = "98765 11223",
                description = "20 சுவர் ஓவியக் கலைஞர்கள் தேவை. தங்குமிடம் மற்றும் உணவு வசதி உண்டு.",
                salaryOrStipend = "₹ 1,200 - ₹ 1,500 / நாள்",
                date = "01 ஆகஸ்ட் 2026"
            )
        )

        dao.insertJobTraining(
            JobTrainingEntity(
                titleTamil = "3D எபோக்சி புளோர் பெயிண்டிங் மற்றும் டெக்ஸ்சர் ஆர்ட் பயிற்சி",
                type = "பயிற்சி மையம்",
                location = "சென்னை பயிற்சி மையம்",
                contactPerson = "TNPA² திறன் மேம்பாட்டு மையம்",
                phone = "044-24567890",
                description = "3 நாட்கள் தீவிர தொழிற்பயிற்சி. நவீன டெக்ஸ்சர் மெட்டீரியல்ஸ் மற்றும் தொழில் நுட்பங்கள் கற்பிக்கப்படும்.",
                salaryOrStipend = "இலவச பயிற்சி (சான்றிதழ் வழங்கப்படும்)",
                date = "15 ஆகஸ்ட் 2026"
            )
        )

        // Seed Receipts (Subscriptions & Donations)
        dao.insertReceipt(
            ReceiptEntity(
                receiptNo = "TNPA-REC-2026-1001",
                memberName = "கார்த்திகேயன் R.",
                phone = "9876543210",
                district = "சென்னை",
                type = "சந்தா தொகை",
                amount = "500",
                date = "28/07/2026",
                paymentMethod = "UPI",
                transactionId = "UPI/20260728/9812457",
                status = "Verified",
                remarks = "2026 - 2027 ஆண்டு சந்தா தொகை செலுத்துதல்"
            )
        )

        dao.insertReceipt(
            ReceiptEntity(
                receiptNo = "TNPA-REC-2026-1002",
                memberName = "செல்வமணி S.",
                phone = "9443322110",
                district = "மதுரை",
                type = "சந்தா தொகை",
                amount = "500",
                date = "25/07/2026",
                paymentMethod = "Cash",
                transactionId = "CASH-MDU-8842",
                status = "Verified",
                remarks = "ஆண்டு உறுப்பினர் சந்தா கட்டணம்"
            )
        )

        dao.insertReceipt(
            ReceiptEntity(
                receiptNo = "TNPA-DON-2026-5001",
                memberName = "முத்துகிருஷ்ணன் P. (நிர்வாகி)",
                phone = "9443144556",
                district = "திருநெல்வேலி",
                type = "நன்கொடை",
                amount = "2500",
                date = "20/07/2026",
                paymentMethod = "Bank Transfer",
                transactionId = "NEFT/SBI/7740129",
                status = "Verified",
                remarks = "மாநில மாநாட்டு நிதி நன்கொடை வழங்கல்"
            )
        )

        dao.insertReceipt(
            ReceiptEntity(
                receiptNo = "TNPA-DON-2026-5002",
                memberName = "வேல்முருகன் S.",
                phone = "9443055667",
                district = "மதுரை",
                type = "நன்கொடை",
                amount = "1000",
                date = "18/07/2026",
                paymentMethod = "UPI",
                transactionId = "UPI/20260718/4401928",
                status = "Verified",
                remarks = "பாதிக்கப்பட்ட தொழிலாளர் குடும்ப நிதியுதவி"
            )
        )

        // Seed State Executives (மாநில நிர்வாகிகள்)
        val stateExecs = listOf(
            StateExecutiveEntity(
                nameTamil = "ரா. சேவியர் பாபு",
                designationTamil = "மாநில பொதுச்செயலாளர்",
                designationEnglish = "State General Secretary",
                phone = "7010131915",
                district = "மதுரை / சென்னை",
                badgeType = "GENERAL_SECRETARY"
            ),
            StateExecutiveEntity(
                nameTamil = "S. மைக்கேல் ஆல்வின்",
                designationTamil = "மாநில தலைவர்",
                designationEnglish = "State President",
                phone = "98400 98765",
                district = "சென்னை",
                badgeType = "CROWN"
            ),
            StateExecutiveEntity(
                nameTamil = "R. சக்திவேல்",
                designationTamil = "மாநில பொருளாளர்",
                designationEnglish = "State Treasurer",
                phone = "94430 11223",
                district = "திருச்சிராப்பள்ளி",
                badgeType = "TREASURER"
            ),
            StateExecutiveEntity(
                nameTamil = "முத்துக்குமார்",
                designationTamil = "மாநில துணைத் தலைவர்",
                designationEnglish = "State Vice President",
                phone = "98940 12345",
                district = "கோயம்புத்தூர்",
                badgeType = "VICE_PRESIDENT"
            ),
            StateExecutiveEntity(
                nameTamil = "சீனிவாசன்",
                designationTamil = "மாநில நிர்வாக செயலாளர்",
                designationEnglish = "State Executive Secretary",
                phone = "98421 88990",
                district = "தஞ்சாவூர்",
                badgeType = "ORGANIZER"
            ),
            StateExecutiveEntity(
                nameTamil = "சக்கரவர்த்தி",
                designationTamil = "மாநில செய்தித் தொடர்பாளர்",
                designationEnglish = "State Press Secretary",
                phone = "94431 99887",
                district = "திருநெல்வேலி",
                badgeType = "PRESS"
            ),
            StateExecutiveEntity(
                nameTamil = "Adv. C. காமராஜ்",
                designationTamil = "மாநில சட்ட ஆலோசகர்",
                designationEnglish = "State Legal Advisor",
                phone = "98402 33445",
                district = "சென்னை",
                badgeType = "LEGAL"
            )
        )
        stateExecs.forEach { dao.insertStateExecutive(it) }
    }
}
