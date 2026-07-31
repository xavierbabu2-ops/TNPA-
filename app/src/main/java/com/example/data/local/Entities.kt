package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class MemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val memberId: String, // e.g. TNPA-2026-1042
    val name: String,
    val fatherName: String,
    val phone: String,
    val district: String,
    val trade: String, // e.g. "கட்டிட பெயிண்டர்" (Building Painter), "ஓவியர்" (Artist/Mural)
    val aadhaarLast4: String,
    val address: String,
    val photoUri: String = "",
    val approvalStatus: String = "Approved", // Approved, Pending, Rejected
    val joinDate: String,
    val validThru: String,
    val qrCodeData: String
)

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titleTamil: String,
    val titleEnglish: String,
    val contentTamil: String,
    val contentEnglish: String,
    val category: String, // e.g. "அறிவிப்பு", "நலத்திட்டம்", "போராட்டம்"
    val date: String,
    val isUrgent: Boolean = false,
    val author: String = "மாநில தலைமை"
)

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titleTamil: String,
    val titleEnglish: String,
    val date: String,
    val time: String,
    val locationTamil: String,
    val locationEnglish: String,
    val descriptionTamil: String,
    val organizer: String
)

@Entity(tableName = "district_leaders")
data class DistrictLeaderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val districtTamil: String,
    val districtEnglish: String,
    val secretaryNameTamil: String,
    val presidentNameTamil: String,
    val phone: String,
    val totalMembers: Int,
    val officeAddress: String
)

@Entity(tableName = "complaints")
data class ComplaintEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val complaintNo: String,
    val memberName: String,
    val phone: String,
    val district: String,
    val category: String, // Wage Issue, Safety Violation, Board Benefit Delay, Legal Aid
    val subject: String,
    val detail: String,
    val status: String = "Received", // Received, Under Review, Resolved
    val dateSubmitted: String
)

@Entity(tableName = "welfare_schemes")
data class WelfareSchemeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titleTamil: String,
    val titleEnglish: String,
    val category: String, // Board Scheme, Union Insurance, Medical Relief
    val benefitsAmount: String,
    val eligibilityTamil: String,
    val requiredDocsTamil: String,
    val applyProcessTamil: String
)

@Entity(tableName = "job_trainings")
data class JobTrainingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titleTamil: String,
    val type: String, // "வேலைவாய்ப்பு" (Job) or "பயிற்சி மையம்" (Training)
    val location: String,
    val contactPerson: String,
    val phone: String,
    val description: String,
    val salaryOrStipend: String,
    val date: String
)

@Entity(tableName = "receipts")
data class ReceiptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val receiptNo: String, // e.g. TNPA-REC-2026-1001
    val memberName: String,
    val phone: String,
    val district: String,
    val type: String, // "சந்தா தொகை" (Subscription) or "நன்கொடை" (Donation)
    val amount: String, // e.g. "500" or "1000"
    val date: String,
    val paymentMethod: String, // "UPI", "Cash", "Bank Transfer", "Card"
    val transactionId: String,
    val status: String = "Verified", // "Verified", "Pending"
    val remarks: String = "சங்க வளர்ச்சிக்கான நிதி பங்களிப்பு"
)

@Entity(tableName = "state_executives")
data class StateExecutiveEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nameTamil: String,
    val designationTamil: String,
    val designationEnglish: String,
    val phone: String,
    val district: String,
    val badgeType: String, // "CROWN", "GENERAL_SECRETARY", "TREASURER", "VICE_PRESIDENT", "JOINT_SECRETARY", "ORGANIZER", "PRESS", "LEGAL"
    val photoUri: String = ""
)

@Entity(tableName = "welfare_claims")
data class WelfareClaimEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val claimNo: String, // e.g. "TNPA-WEL-2026-701"
    val memberId: String, // e.g. "TNPA-2026-8942"
    val memberName: String,
    val phone: String,
    val schemeTitleTamil: String,
    val schemeTitleEnglish: String,
    val category: String, // "Board Benefit", "Union Insurance", "Medical Relief", "Marriage/Education"
    val claimedAmount: String,
    val status: String = "Under Review", // "Submitted", "Under Review", "Approved", "Disbursed", "Rejected"
    val submissionDate: String,
    val remarks: String = "மாவட்ட சங்கத்தால் சரிபார்க்கப்பட்டு வருகிறது"
)
