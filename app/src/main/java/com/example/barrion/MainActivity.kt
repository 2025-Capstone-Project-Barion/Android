package com.example.barrion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BarrionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DesignSystemShowcase(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
fun DesignSystemShowcase(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Spacing.Medium)
    ) {
        // 제목
        Text(
            text = "디자인 시스템 테스트",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(Spacing.Medium))

        // 타이포그래피 섹션
        SectionTitle(title = "타이포그래피")

        Text(text = "헤드라인 라지", style = MaterialTheme.typography.headlineLarge)
        Text(text = "헤드라인 미디엄", style = MaterialTheme.typography.headlineMedium)
        Text(text = "헤드라인 스몰", style = MaterialTheme.typography.headlineSmall)
        Text(text = "타이틀 라지", style = MaterialTheme.typography.titleLarge)
        Text(text = "바디 라지", style = MaterialTheme.typography.bodyLarge)
        Text(text = "라벨 미디엄", style = MaterialTheme.typography.labelMedium)

        Spacer(modifier = Modifier.height(Spacing.Large))

        // 색상 섹션
        SectionTitle(title = "색상 시스템")

        // 기본 Material 색상
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {
            ColorSwatch(
                name = "Primary",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            ColorSwatch(
                name = "Secondary",
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            )
            ColorSwatch(
                name = "Tertiary",
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(Spacing.Small))

        // 커스텀 색상
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {
            ColorSwatch(
                name = "BluePale",
                color = MaterialTheme.barrionColors.bluePale,
                modifier = Modifier.weight(1f)
            )
            ColorSwatch(
                name = "GrayMedium",
                color = MaterialTheme.barrionColors.grayMedium,
                modifier = Modifier.weight(1f)
            )
            ColorSwatch(
                name = "Error",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(Spacing.Large))

        // 간격 섹션
        SectionTitle(title = "간격 시스템")

        // 다양한 간격을 시각적으로 표시
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "XSmall", modifier = Modifier.width(80.dp))
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .width(Spacing.XSmall)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Text(text = " ${Spacing.XSmall.value.toInt()}dp")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Small", modifier = Modifier.width(80.dp))
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .width(Spacing.Small)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Text(text = " ${Spacing.Small.value.toInt()}dp")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Medium", modifier = Modifier.width(80.dp))
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .width(Spacing.Medium)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Text(text = " ${Spacing.Medium.value.toInt()}dp")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Large", modifier = Modifier.width(80.dp))
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .width(Spacing.Large)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Text(text = " ${Spacing.Large.value.toInt()}dp")
        }

        Spacer(modifier = Modifier.height(Spacing.Large))

        // 버튼 예시
        SectionTitle(title = "버튼 컴포넌트")

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("기본 버튼")
        }

        Spacer(modifier = Modifier.height(Spacing.Small))

        ElevatedButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("입체 버튼")
        }

        Spacer(modifier = Modifier.height(Spacing.Small))

        OutlinedButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("외곽선 버튼")
        }

        Spacer(modifier = Modifier.height(Spacing.Small))

        TextButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("텍스트 버튼")
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(Spacing.Small))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.secondary
        )
        Divider(modifier = Modifier.padding(vertical = Spacing.Small))
    }
}

@Composable
fun ColorSwatch(
    name: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.height(Spacing.XSmall))
        Text(
            text = name,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DesignSystemShowcasePreview() {
    BarrionTheme {
        DesignSystemShowcase()
    }
}