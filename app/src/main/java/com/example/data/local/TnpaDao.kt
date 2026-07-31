package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TnpaDao {
    // Members
    @Query("SELECT * FROM members ORDER BY id DESC")
    fun getAllMembers(): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members WHERE memberId = :memberId OR phone = :query LIMIT 1")
    suspend fun findMember(memberId: String, query: String): MemberEntity?

    @Query("SELECT * FROM members WHERE approvalStatus = 'Pending' ORDER BY id DESC")
    fun getPendingMembers(): Flow<List<MemberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: MemberEntity): Long

    @Update
    suspend fun updateMember(member: MemberEntity)

    @Query("UPDATE members SET approvalStatus = :status WHERE id = :id")
    suspend fun updateMemberApprovalStatus(id: Long, status: String)

    @Query("SELECT COUNT(*) FROM members WHERE approvalStatus = 'Approved'")
    fun getApprovedMemberCount(): Flow<Int>

    // News
    @Query("SELECT * FROM news ORDER BY id DESC")
    fun getAllNews(): Flow<List<NewsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: NewsEntity)

    // Events
    @Query("SELECT * FROM events ORDER BY id DESC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    // District Leaders
    @Query("SELECT * FROM district_leaders ORDER BY districtTamil ASC")
    fun getAllDistrictLeaders(): Flow<List<DistrictLeaderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistrictLeader(leader: DistrictLeaderEntity)

    // Complaints
    @Query("SELECT * FROM complaints ORDER BY id DESC")
    fun getAllComplaints(): Flow<List<ComplaintEntity>>

    @Query("SELECT * FROM complaints WHERE complaintNo = :no LIMIT 1")
    suspend fun findComplaint(no: String): ComplaintEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaint(complaint: ComplaintEntity)

    @Query("UPDATE complaints SET status = :status WHERE id = :id")
    suspend fun updateComplaintStatus(id: Long, status: String)

    // Welfare Schemes
    @Query("SELECT * FROM welfare_schemes ORDER BY id ASC")
    fun getAllWelfareSchemes(): Flow<List<WelfareSchemeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWelfareScheme(scheme: WelfareSchemeEntity)

    // Job Trainings
    @Query("SELECT * FROM job_trainings ORDER BY id DESC")
    fun getAllJobTrainings(): Flow<List<JobTrainingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobTraining(item: JobTrainingEntity)

    // Receipts & Subscriptions
    @Query("SELECT * FROM receipts ORDER BY id DESC")
    fun getAllReceipts(): Flow<List<ReceiptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceipt(receipt: ReceiptEntity): Long

    // State Executives
    @Query("SELECT * FROM state_executives ORDER BY id ASC")
    fun getAllStateExecutives(): Flow<List<StateExecutiveEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStateExecutive(executive: StateExecutiveEntity)

    // Welfare Claims Submissions
    @Query("SELECT * FROM welfare_claims ORDER BY id DESC")
    fun getAllWelfareClaims(): Flow<List<WelfareClaimEntity>>

    @Query("SELECT * FROM welfare_claims WHERE memberId = :memberId ORDER BY id DESC")
    fun getWelfareClaimsForMember(memberId: String): Flow<List<WelfareClaimEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWelfareClaim(claim: WelfareClaimEntity): Long
}
