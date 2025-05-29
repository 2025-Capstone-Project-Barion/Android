package com.example.menu.component

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * 이미지 업로드 영역 컴포넌트 (실제 이미지 선택 기능 포함)
 * - 갤러리에서 이미지 선택
 * - 카메라로 사진 촬영
 * - Base64로 변환하여 전달
 */
@Composable
fun ImageUploadArea(
    imageUrl: String,
    onImageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // 권한 및 다이얼로그 상태
    var showImagePicker by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }

    // 카메라 촬영용 임시 파일
    val photoFile = remember {
        File.createTempFile(
            "photo_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}",
            ".jpg",
            context.cacheDir
        )
    }

    val photoUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
    }

    // 갤러리 선택 런처
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUri ->
            try {
                println("🖼️ 갤러리 이미지 선택됨: $imageUri")
                val inputStream = context.contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                if (bitmap != null) {
                    // Bitmap을 Base64로 변환
                    val base64String = bitmapToBase64(bitmap)
                    val dataUri = "data:image/jpeg;base64,$base64String"
                    onImageSelected(dataUri)
                    println("🖼️ 갤러리 이미지 Base64 변환 완료: ${base64String.take(50)}...")
                } else {
                    println("❌ 갤러리 이미지 Bitmap 변환 실패")
                }
            } catch (e: Exception) {
                println("❌ 갤러리 이미지 처리 오류: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    // 카메라 촬영 런처
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            try {
                println("🖼️ 카메라 촬영 완료")
                val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                if (bitmap != null) {
                    val base64String = bitmapToBase64(bitmap)
                    val dataUri = "data:image/jpeg;base64,$base64String"
                    onImageSelected(dataUri)
                    println("🖼️ 카메라 이미지 Base64 변환 완료: ${base64String.take(50)}...")
                } else {
                    println("❌ 카메라 이미지 Bitmap 변환 실패")
                }

                // 임시 파일 삭제
                photoFile.delete()
            } catch (e: Exception) {
                println("❌ 카메라 이미지 처리 오류: ${e.message}")
                e.printStackTrace()
            }
        } else {
            println("❌ 카메라 촬영 실패")
        }
    }

    // 권한 요청 런처
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
        val storageGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions[Manifest.permission.READ_MEDIA_IMAGES] ?: false
        } else {
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
        }

        println("🔒 권한 결과 - 카메라: $cameraGranted, 저장소: $storageGranted")

        if (cameraGranted || storageGranted) {
            showImagePicker = true
        } else {
            showPermissionDialog = true
        }
    }

    Box(
        modifier = modifier
            .height(200.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 2.dp,
                color = if (imageUrl.isEmpty()) {
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                } else {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                },
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                println("🖼️ 이미지 영역 클릭됨")
                // 권한 체크
                val cameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                }

                println("🔒 현재 권한 - 카메라: ${cameraPermission == PackageManager.PERMISSION_GRANTED}, 저장소: ${storagePermission == PackageManager.PERMISSION_GRANTED}")

                if (cameraPermission == PackageManager.PERMISSION_GRANTED ||
                    storagePermission == PackageManager.PERMISSION_GRANTED) {
                    showImagePicker = true
                } else {
                    // 권한 요청
                    val permissions = mutableListOf<String>()
                    if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                        permissions.add(Manifest.permission.CAMERA)
                    }
                    if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
                        } else {
                            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }
                    println("🔒 권한 요청: ${permissions.joinToString()}")
                    permissionLauncher.launch(permissions.toTypedArray())
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl.isEmpty()) {
            // 이미지가 없을 때
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "이미지 추가",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "이미지를 추가하려면 탭하세요",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            // 이미지가 있을 때 - Base64와 URL 모두 처리
            if (imageUrl.startsWith("data:image")) {
                // Base64 이미지인 경우
                var bitmap by remember { mutableStateOf<Bitmap?>(null) }
                var hasError by remember { mutableStateOf(false) }

                LaunchedEffect(imageUrl) {
                    try {
                        println("🖼️ Base64 이미지 표시 시도")
                        val base64Data = imageUrl.substringAfter("base64,")
                        val imageBytes = Base64.decode(base64Data, Base64.DEFAULT)
                        val decodedBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                        if (decodedBitmap != null) {
                            println("🖼️ Base64 이미지 표시 성공")
                            bitmap = decodedBitmap
                            hasError = false
                        } else {
                            println("❌ Base64 Bitmap 생성 실패")
                            hasError = true
                        }
                    } catch (e: Exception) {
                        println("❌ Base64 디코딩 오류: ${e.message}")
                        hasError = true
                    }
                }

                if (bitmap != null) {
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = "선택된 이미지",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else if (hasError) {
                    // 에러 상태 표시
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "이미지 오류",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = "이미지 로드 실패",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                } else {
                    // 로딩 상태
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                // 일반 URL인 경우
                println("🖼️ URL 이미지 표시: $imageUrl")
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "메뉴 이미지",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            // 이미지 변경 오버레이
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "탭하여 이미지 변경",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }

    // 이미지 선택 다이얼로그
    ImagePickerDialog(
        isVisible = showImagePicker,
        onDismiss = {
            showImagePicker = false
            println("🖼️ 이미지 선택 다이얼로그 닫힘")
        },
        onCameraSelected = {
            println("🖼️ 카메라 선택됨")
            showImagePicker = false
            cameraLauncher.launch(photoUri)
        },
        onGallerySelected = {
            println("🖼️ 갤러리 선택됨")
            showImagePicker = false
            galleryLauncher.launch("image/*")
        }
    )

    // 권한 거부 시 안내 다이얼로그
    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("권한이 필요합니다") },
            text = { Text("이미지를 선택하려면 카메라 또는 저장소 권한이 필요합니다.") },
            confirmButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text("확인")
                }
            }
        )
    }
}

/**
 * Bitmap을 Base64 문자열로 변환
 */
private fun bitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.NO_WRAP)
}