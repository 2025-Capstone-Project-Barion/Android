package com.example.data.di

import com.example.data.api.CategoryApi
import com.example.data.api.MenuApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * 네트워크 관련 의존성 주입을 담당하는 Hilt 모듈
 * Retrofit, OkHttp, JSON 직렬화 등 네트워크 통신에 필요한 모든 객체를 제공
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /** 백엔드 서버의 기본 URL */
    private const val BASE_URL = "http://13.209.99.95:8080/"

    /**
     * Kotlin Serialization용 JSON 설정
     * - ignoreUnknownKeys: 서버에서 추가 필드가 와도 무시
     * - coerceInputValues: null 값을 기본값으로 변환
     * - encodeDefaults: 기본값도 JSON에 포함
     */
    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true    // 알 수 없는 키 무시 (API 호환성)
            coerceInputValues = true    // null 값 처리
            encodeDefaults = true       // 기본값 인코딩
        }
    }

    /**
     * HTTP 로깅 인터셉터 제공
     * 개발 시 API 요청/응답을 로그로 확인할 수 있음
     */
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY  // 요청/응답 본문까지 로깅
        }
    }

    /**
     * OkHttp 클라이언트 설정
     * - 로깅 인터셉터 추가
     * - 타임아웃 설정 (연결, 읽기, 쓰기 각각 30초)
     */
// NetworkModule.kt - 인터셉터 순서 변경 및 강화
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            // 로깅 인터셉터를 먼저 추가
            .addInterceptor(loggingInterceptor)
            // 헤더 인터셉터를 나중에 추가 (더 높은 우선순위)
            .addInterceptor { chain ->
                val original = chain.request()

                // 강제로 로그 출력
                android.util.Log.d("HeaderInterceptor", "🔧 헤더 인터셉터 실행됨!")
                android.util.Log.d("HeaderInterceptor", "원본 URL: ${original.url}")
                android.util.Log.d("HeaderInterceptor", "원본 헤더: ${original.headers}")

                val newRequest = original.newBuilder()
                    .removeHeader("Content-Type")  // 기존 헤더 제거
                    .removeHeader("Accept")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("User-Agent", "Barrion-Android")
                    .build()

                android.util.Log.d("HeaderInterceptor", "수정된 헤더: ${newRequest.headers}")

                val response = chain.proceed(newRequest)
                android.util.Log.d("HeaderInterceptor", "응답 코드: ${response.code}")

                response
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Retrofit 인스턴스 제공
     * - Kotlin Serialization 컨버터 사용
     * - OkHttp 클라이언트 설정 적용
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            // Kotlin Serialization을 JSON 컨버터로 사용
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    /**
     * 메뉴 관련 API 인터페이스 제공
     * Retrofit이 구현체를 자동 생성
     */
    @Provides
    @Singleton
    fun provideMenuApi(retrofit: Retrofit): MenuApi {
        return retrofit.create(MenuApi::class.java)
    }

    /**
     * 카테고리 관련 API 인터페이스 제공
     * Retrofit이 구현체를 자동 생성
     */
    @Provides
    @Singleton
    fun provideCategoryApi(@Named("CategoryRetrofit") retrofit: Retrofit): CategoryApi {
        return retrofit.create(CategoryApi::class.java)
    }
    // NetworkModule.kt에 추가
    @Provides
    @Singleton
    @Named("CategoryRetrofit")
    fun provideCategoryRetrofit(json: Json): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build()

                android.util.Log.d("CategoryRetrofit", "헤더 추가됨: ${request.headers}")
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}