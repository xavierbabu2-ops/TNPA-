package com.example.data.remote

import com.example.BuildConfig
import com.squareup.moshi.Json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

data class Part(val text: String? = null)

data class Content(val parts: List<Part>, val role: String? = null)

data class GenerateContentRequest(
    val contents: List<Content>,
    val systemInstruction: Content? = null
)

data class Candidate(val content: Content)

data class GenerateContentResponse(val candidates: List<Candidate>? = null)

interface GeminiApi {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val api: GeminiApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GeminiApi::class.java)
    }
}

class XavierBabuAiRepository {

    private val systemPrompt = """
        You are "சேவியர் பாபு" (Xavier Babu), the State General Secretary (மாநில பொதுச்செயலாளர்) and Official AI Assistant of TNPA² – தமிழ்நாடு பெயிண்டர்கள் மற்றும் ஓவியர்கள் முன்னேற்ற சங்கம் (Tamil Nadu Painters and Artists Progress Association).

        Respond warmly, respectfully, and clearly in fluent Tamil by default (or English if asked in English).
        Your motto: "உரிமையை மீட்போம் – ஒன்றுபடுவோம் – தொழிலாளர்களைக் காப்போம்"

        Core Union Knowledge:
        1. Membership (உறுப்பினர் சேர்க்கை):
           - Annual fee: ₹100 / year
           - Required details: Name, Father/Spouse name, Phone, Address, Photo, Trade (Painter/Artist), District, Aadhaar (optional).
           - Digital ID Card is instantly generated upon approval with QR verification code.

        2. Painter Welfare Board Schemes (தமிழ்நாடு உடலுழைப்பு தொழிலாளர்கள் நல வாரியம்):
           - Accident Death / Permanent Disability: ₹5,00,000
           - Natural Death Assistance: ₹50,000 + Funeral grant ₹5,000
           - Marriage Assistance: ₹20,000
           - Higher Education Scholarship: ₹1,500 to ₹8,000 per year
           - Maternity Relief: ₹6,000
           - Old Age Pension: ₹1,200 / month (Age 60+)
           - Spectacles grant: ₹500

        3. District Leaders & Help:
           - Offices present in all 38 districts of Tamil Nadu (Chennai, Madurai, Coimbatore, Tiruchirappalli, Salem, Tirunelveli, Vellore, Thanjavur, Erode, etc.).
           - Emergency Hotline: 1800-425-TNPA2 (1800-425-86722) / +91 94440 12345.

        4. App Features:
           - Digital ID Card download & QR check
           - Complaints portal with status tracking
           - News, Events, Photo/Video Gallery
           - Skill Training Center & Job Opportunities

        Tone: Encouraging, proud union brotherly tone ("சகோதரரே", "தோழரே"), authoritative yet accessible. Keep answers concise, bulleted when explaining welfare schemes, and always invite them to ask more or register.
    """.trimIndent()

    suspend fun askXavierBabu(userQuery: String, chatHistory: List<Pair<String, String>> = emptyList()): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig::class.java.getField("GEMINI_API_KEY").get(null) as? String ?: ""
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getSmartOfflineResponse(userQuery)
        }

        try {
            val contentList = mutableListOf<Content>()
            
            // Add history
            chatHistory.forEach { (role, text) ->
                contentList.add(Content(parts = listOf(Part(text = text)), role = if (role == "user") "user" else "model"))
            }
            contentList.add(Content(parts = listOf(Part(text = userQuery)), role = "user"))

            val request = GenerateContentRequest(
                contents = contentList,
                systemInstruction = Content(parts = listOf(Part(text = systemPrompt)))
            )

            val response = GeminiClient.api.generateContent(apiKey, request)
            val answer = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            answer ?: getSmartOfflineResponse(userQuery)
        } catch (e: Exception) {
            getSmartOfflineResponse(userQuery)
        }
    }

    private fun getSmartOfflineResponse(query: String): String {
        val q = query.lowercase()
        return when {
            q.contains("உறுப்பினர்") || q.contains("சேர") || q.contains("register") || q.contains("join") -> {
                "தோழரே, TNPA² சங்கத்தில் உறுப்பினராகச் சேர பயன்பாட்டின் 'உறுப்பினர் பதிவு' (Membership Registration) பக்கத்திற்குச் செல்லவும்.\n\n" +
                "• ஆண்டு சந்தா: ₹100\n" +
                "• தேவையானவை: பெயர், தொலைபேசி எண், மாவட்டம், புகைப்படம்.\n" +
                "• ஒப்புதல் பெற்றதும் டிஜிட்டல் அடையாள அட்டை (Digital ID Card) பெறலாம்!"
            }
            q.contains("நலத்திட்டம்") || q.contains("வாரியம்") || q.contains("welfare") || q.contains("பணம்") -> {
                "தோழரே, தமிழ்நாடு பெயிண்டர்கள் மற்றும் ஓவியர்கள் நல வாரியத்தின் முக்கிய நலத்திட்டங்கள்:\n\n" +
                "1. விபத்து மரண நிதி: ₹5,00,000\n" +
                "2. இயற்கை மரண உதவி: ₹50,00, ஈமச்சடங்கு நிதி ₹5,000\n" +
                "3. திருமண உதவித்தொகை: ₹20,000\n" +
                "4. கல்வி உதவித்தொகை: ₹1,500 - ₹8,000\n" +
                "5. ஓய்வூதியம்: மாதம் ₹1,200\n\n" +
                "மேலும் தகவலுக்கு 'நலத்திட்டங்கள்' பக்கத்தைப் பார்க்கவும்."
            }
            q.contains("நிர்வாகி") || q.contains("மாவட்டம்") || q.contains("leader") || q.contains("office") -> {
                "தோழரே, தமிழ்நாட்டின் 38 மாவட்டங்களுக்கும் TNPA² சங்கத்தின் மாவட்ட நிர்வாகிகள் உள்ளனர்.\n\n" +
                "'மாவட்ட நிர்வாகிகள்' பக்கத்தில் உங்கள் மாவட்டத்தைத் தேர்வு செய்து மாவட்டச் செயலாளர் மற்றும் தலைவரின் தொடர்பெண்ணைப் பெறலாம்."
            }
            q.contains("புகார்") || q.contains("பிரச்சனை") || q.contains("complaint") || q.contains("கூலி") -> {
                "தோழரே, கூலி பாக்கி, விபத்து பாதுகாப்பு அல்லது ஒப்பந்ததாரர் பிரச்சனைகள் இருப்பின் 'புகார் பதிவு' பக்கத்தில் உங்கள் புகாரைப் பதிவு செய்யவும். எங்கள் சங்க சட்டக் குழு உடனடியாக உங்களுக்கு உதவும்."
            }
            q.contains("அட்டை") || q.contains("id") || q.contains("card") -> {
                "தோழரே, உங்கள் 'உறுப்பினர் ID சரிபார்ப்பு' பக்கத்தில் உங்கள் உறுப்பினர் எண் அல்லது மொபைல் எண்ணை உள்ளிட்டு டிஜிட்டல் அடையாள அட்டையைப் பதிவிறக்கம் செய்து வாட்ஸ்அப்பில் பகிரலாம்."
            }
            else -> {
                "வணக்கம் தோழரே! நான் சேவியர் பாபு, மாநில பொதுச்செயலாளர்.\n\n" +
                "சங்க உறுப்பினர் சேர்க்கை, நலவாரிய உதவிகள், மாவட்ட நிர்வாகிகள், தொழிலாளர் சட்டங்கள் மற்றும் அடையாள அட்டை தொடர்பான எந்த உதவிக்கும் நான் தயார். உங்கள் கேள்வியைக் கேளுங்கள்!"
            }
        }
    }
}
